package com.worklog.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.db.DataSourceFactory;
import com.worklog.dto.ReportEmployeeDTO;
import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.DuplicateTimesheetCreationException;
import com.worklog.utils.CustomDateFormatter;


public class TimeSheetDAO {
	
	private static final Logger logger = LogManager.getLogger(TimeSheetDAO.class);

	private TimeSheet mapToTimeSheet(ResultSet rs) throws SQLException {
	    TimeSheet timeSheet = new TimeSheet();

	    timeSheet.setApproved(rs.getBoolean("approved"));
		timeSheet.setCreated_at(CustomDateFormatter.toLocalFormat(rs.getString("created_at"), true));
	    timeSheet.setEmployee_id(rs.getInt("employee_id"));
	    timeSheet.setId(rs.getInt("id"));
	    timeSheet.setStatus(rs.getString("status"));
	    timeSheet.setManager_comment(rs.getString("manager_comment")); // correct column name
	    timeSheet.setTotal_hours(rs.getDouble("total_hours"));
	    timeSheet.setManager_id(rs.getInt("manager_id"));
		timeSheet.setWork_date(CustomDateFormatter.toLocalFormat(rs.getString("work_date")));
	    return timeSheet;
	}


	public int getTimeSheetId(TimeSheet timesheet) {
		String sql = "SELECT id FROM timesheets where employee_id = ? and work_date = ? and total_hours = ? and status = ? and manager_id = ?";
		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, timesheet.getEmployee_id());
			pstmt.setDate(2, Date.valueOf(timesheet.getWork_date()));
			pstmt.setDouble(3, timesheet.getTotal_hours());
			pstmt.setString(4, timesheet.getStatus());
			pstmt.setInt(5, timesheet.getManager_id());

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}

			return -1;

		} catch (SQLException e) {
			logger.error("Error fetching timesheet ID for employee {}", timesheet.getEmployee_id(), e);
			return -1;
		}
	}

	public boolean createTimeSheet(TimeSheet timesheet) throws DuplicateTimesheetCreationException {
		String sql = "INSERT INTO timesheets(employee_id, work_date, total_hours, status, manager_id) VALUES(?, ?, ?, ?, ?)";
		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, timesheet.getEmployee_id());
			pstmt.setDate(2, Date.valueOf(timesheet.getWork_date()));
			pstmt.setDouble(3, timesheet.getTotal_hours());
			pstmt.setString(4, timesheet.getStatus());
			pstmt.setInt(5, timesheet.getManager_id());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows <= 0) {
				throw new SQLException("Timesheet insertion failed.");
			}

			return true;

		} catch (SQLException e) {
			logger.error("Error creating timesheet for employee {}", timesheet.getEmployee_id(), e);
			if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique_timesheet")) {
				throw new DuplicateTimesheetCreationException("Timesheet Already Found for this Work date.", e);
			}
			return false;
		}
	}
	
//	public Optional<List<TimeSheet>> getAllTimeSheetsForEmployee(int id) {
//	    String sql = "select * from timesheets where employee_id=?";
//

public Optional<List<TimeSheet>> getAllTimeSheetsForEmployee(int id, String status, int pageNumber) {
		String sql;
		if (status.equalsIgnoreCase("all")) {
			sql = "select * from timesheets where employee_id=? order by work_date";
		} else {
			sql = "select * from timesheets where employee_id=? and status ilike ? order by work_date";
		}

		if (pageNumber == 0)
			pageNumber = 1; // default 1st page
		int offset = (pageNumber - 1) * TaskDAO.rowsPerPage;

		sql = sql + " LIMIT " + TaskDAO.rowsPerPage + " OFFSET " + offset;

		try(Connection conn=DataSourceFactory.getConnectionInstance();
			PreparedStatement pstmt=conn.prepareStatement(sql);
			){

				List<TimeSheet> timeSheets = new ArrayList<>();

			pstmt.setInt(1, id);
			if (!status.equalsIgnoreCase("all")) {
				pstmt.setString(2, "%" + status + "%");
			}

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				timeSheets.add(mapToTimeSheet(rs));
			}

			return Optional.of(timeSheets);
		} catch (SQLException e) {
			logger.error("DB error in getAllTimeSheetsForEmployee(employeeId={}, status={})", id, status, e);
			return Optional.empty();
		}
	}


	public Optional<List<TimeSheet>> getPendingTimesheet() {

		String sql = "select id, employee_id, work_date, total_hours, status, manager_id, manager_comment, approved, created_at "
						+ "from timesheets " + "where status ILIKE 'pending' " + "order by work_date DESC, created_at desc";

		List<TimeSheet> list = new ArrayList<>();

		try (Connection conn = DataSourceFactory.getConnectionInstance();
						PreparedStatement pstmt = conn.prepareStatement(sql);
						ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				TimeSheet timesheet = new TimeSheet(rs.getInt("id"), rs.getInt("employee_id"),
								CustomDateFormatter.toLocalFormat(rs.getString("work_date")), rs.getDouble("total_hours"),
								rs.getString("status"), rs.getInt("manager_id"), rs.getString("manager_comment"), rs.getBoolean("approved"),
								CustomDateFormatter.toLocalFormat(rs.getString("created_at"), true));
				list.add(timesheet);
			}

			return Optional.of(list);

		} catch (SQLException e) {
			logger.error("DB error in getPendingTimesheet()", e);
			return Optional.empty();
		}
	}


	public Optional<List<ReportEmployeeDTO>> getReportDailyworkhours(LocalDate fromDate, LocalDate toDate, int manager_id) {
		String sql = """
						select t.assigned_to as "employee_id",e.name as "employee_name", ts.work_date as "work_date" , t.id as "task_id",
						t.title as title,sum(tse.hours_spent) as "task_duration", tse.notes as notes
						from tasks t inner join timesheet_entries tse on t.id=tse.task_id
						inner join timesheets ts on tse.timesheet_id=ts.id
						inner join employees e on t.assigned_to=e.id
						where t.created_by=? or ts.manager_id = ? and ts.work_date >= ? or ts.work_date < ?
						group by t.id, t.title, e.name, tse.notes, ts.work_date
						order by ts.work_date;
						""";
		try(Connection conn=DataSourceFactory.getConnectionInstance();
			PreparedStatement pstmt=conn.prepareStatement(sql)
						){
							pstmt.setInt(1, manager_id);
							pstmt.setInt(2, manager_id);
							pstmt.setDate(3, Date.valueOf(fromDate));
							pstmt.setDate(4, Date.valueOf(toDate));
			ResultSet rs=pstmt.executeQuery();
			List<ReportEmployeeDTO> list = new ArrayList<>();
			while(rs.next()) {
				ReportEmployeeDTO reportEmployeeDTO = new ReportEmployeeDTO();
				reportEmployeeDTO.setEmp_id(rs.getInt("employee_id"));
				reportEmployeeDTO.setEmp_name(rs.getString("employee_name"));
				reportEmployeeDTO.setWork_date(rs.getDate("work_date").toLocalDate());
				reportEmployeeDTO.setTask_id(rs.getInt("task_id"));
				reportEmployeeDTO.setTitle(rs.getString("title"));
				reportEmployeeDTO.setNotes(rs.getString("notes"));
				reportEmployeeDTO.setTask_duration(rs.getDouble("task_duration"));
				list.add(reportEmployeeDTO);
			}
			
			return Optional.ofNullable(list);

		}catch(SQLException e) {
			logger.error("Error generating work hours report for manager {}", manager_id, e);
			return Optional.ofNullable(null);
		}
	}

	public Optional<TimeSheet> getTimesheetByid(int id){
		
		String sql = "select * from timesheets where id=?";
		
		try (Connection conn = DataSourceFactory.getConnectionInstance();
						PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				TimeSheet ts = new TimeSheet(
								rs.getInt("id"),
								rs.getInt("employee_id"),
								CustomDateFormatter.toLocalFormat(rs.getString("work_date")),
								rs.getDouble("total_hours"),
								rs.getString("status"),
								rs.getInt("manager_id"),
								rs.getString("manager_comment"),
								rs.getBoolean("approved"),
								CustomDateFormatter.toLocalFormat(rs.getString("created_at"), true)
								);
								return Optional.ofNullable(ts);
				
			}
			return Optional.empty();
			
		}catch(SQLException e) {
			logger.error("DB error in getTimesheetByid(id={})", id, e);
			return Optional.ofNullable(null);
		}
		
	}
	
	public boolean updateTimesheetStatus(int timesheetId, String status, int managerId, String comment, boolean approved) {

		String sql = "update timesheets set status=?, manager_id=?, manager_comment=?, approved=? where id=?";

		try (Connection conn = DataSourceFactory.getConnectionInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setInt(2, managerId);
			pstmt.setString(3, comment);
			pstmt.setBoolean(4, approved);
			pstmt.setInt(5, timesheetId);

			int rows = pstmt.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
			logger.error("DB error while updating timesheet status. timesheetId={}", timesheetId, e);
			return false;
		}
	}

	public int getTimesheetCount(int employeeId, boolean isManager) {

		String sql;
		if (isManager == true) {
			sql = "SELECT * FROM timesheets WHERE manager_id = ?";
		} else {
			sql = "SELECT * FROM timesheets WHERE employee_id = ?";
		}

		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, employeeId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("row_count");
			}

			return 0;

		} catch (SQLException e) {
			logger.error("DB error while getting timesheet count", e);
			return -1;
		}

	}
	
}
