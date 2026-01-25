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

import com.worklog.db.DataSourceFactory;
import com.worklog.entities.Report;
import com.worklog.entities.TimeSheet;

/**
 * 
 * TimeSheetDAO - This class is used for creating time sheets in the database.
 * 
 * @author Vasudevan Tamizharasan, Preetha, Renganathan
 * @since 20-02-2026
 * 
 */

public class TimeSheetDAO {

	private TimeSheet mapToTimeSheet(ResultSet rs) throws SQLException {
		TimeSheet timeSheet = new TimeSheet();
		while(rs.next()) {
			timeSheet.setApproved(rs.getBoolean("approved"));
			timeSheet.setCreated_at(rs.getTimestamp("created_at"));
			timeSheet.setEmployee_id(rs.getInt("employee_id"));
			timeSheet.setId(rs.getInt("id"));
			timeSheet.setStatus(rs.getString("status"));
			timeSheet.setManager_comment(rs.getString("manager_comment"));
			timeSheet.setTotal_hours(rs.getDouble("total_hours"));
			timeSheet.setManager_id(rs.getInt("manager_id"));
			Date date=rs.getDate("work_date");
			LocalDate localDate=date.toLocalDate();
			timeSheet.setWork_date(localDate);
		}
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
			e.printStackTrace();
			return -1;
		}
	}

	public boolean createTimeSheet(TimeSheet timesheet) {
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
			e.printStackTrace();
			return false;
		}
	}
	
	public Optional<List<TimeSheet>> getAllApprovedTimeSheet(int id){
		String sql="select * from Timesheet where employee_id=? and approved=?";
		try(Connection conn=DataSourceFactory.getConnectionInstance();
			PreparedStatement pstmt=conn.prepareStatement(sql);
			){
			pstmt.setInt(1, id);
			pstmt.setBoolean(2,true);
			List<TimeSheet> timeSheets=new ArrayList<>();
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				TimeSheet timeSheet=mapToTimeSheet(rs);
				timeSheets.add(timeSheet);
				
			}
			return Optional.ofNullable(timeSheets);
		}catch(SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
		}
		
	}

	public Optional<List<TimeSheet>> getPendingTimesheet() {

		String sql = "select id, employee_id, work_date, total_hours, status, manager_id, manager_comment, "
				+ "approved, created_at from timesheets where status = 'PENDING' order by work_date desc";

		List<TimeSheet> list = new ArrayList<>();

		try (Connection conn = DataSourceFactory.getConnectionInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				TimeSheet timesheet = new TimeSheet(
						rs.getInt("id"),
						rs.getInt("employee_id"),
						rs.getDate("work_date").toLocalDate(),   
						rs.getDouble("total_hours"),
						rs.getString("status"),
						rs.getInt("manager_id"),
						rs.getString("manager_comment"),
						rs.getBoolean("approved"),
						rs.getTimestamp("created_at")
				);

				list.add(timesheet);
			}

			return Optional.of(list);

		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
		}
	}
	// tommorrow seen this reportdao
	public Optional<Report> getReportDailyworkhours(LocalDate fromDate,LocalDate toDate,int id){
		String sql="select  employee_id, work_date,total_hours,status from timesheet where (work_date=? between work_date=? )and manager_id=?";
		try(Connection conn=DataSourceFactory.getConnectionInstance();
			PreparedStatement pstmt=conn.prepareStatement(sql)
						){
			pstmt.setDate(1, Date.valueOf(fromDate));
			pstmt.setDate(1, Date.valueOf(toDate));
			pstmt.setInt(3, id);
			Report report=new Report();
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				report.setEmp_id(rs.getInt("employee_id"));
				report.setTotalHours(rs.getDouble("totalHours"));
				report.setWorkDate(rs.getDate("work_date").toLocalDate());
				
			}
			return Optional.of(report);
			
		}catch(SQLException e) {
			e.printStackTrace();
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
								rs.getDate("work_date").toLocalDate(),   
								rs.getDouble("total_hours"),
								rs.getString("status"),
								rs.getInt("manager_id"),
								rs.getString("manager_comment"),
								rs.getBoolean("approved"),
								rs.getTimestamp("created_at")
								);
				return Optional.of(ts);
				
			}
			return Optional.empty();
			
		}catch(SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return false;
		}
	}
	
}
