package com.worklog.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.worklog.db.DataSourceFactory;
import com.worklog.entities.TimeSheet;

/**
 * 
 * TimeSheetDAO - This class is used for creating time sheets in the database.
 * 
 * @author Vasudevan Tamizharasan, Preetha
 * @since 20-02-2026
 * 
 */

public class TimeSheetDAO {

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

	public Optional<List<TimeSheet>> getPendingTimesheet() {

		String sql = "select id, employee_id, work_date, total_hours, status, manager_id, manager_comment, "
				+ "approved, created_at from timesheets where status = 'pending' order by work_date desc";

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


}
