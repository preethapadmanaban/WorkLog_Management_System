package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.dto.TimeSheetEntryDTO;
import com.worklog.dto.TimeSheetEntryForReviewDTO;
import com.worklog.entities.TimeSheetEntry;
import com.worklog.factories.DataSourceFactory;

public class TimeSheetEntryDAO {
	
	private static final Logger logger = LogManager.getLogger(TimeSheetEntryDAO.class);


	private TimeSheetEntry getTimeSheetEntry(ResultSet rs) throws SQLException{
		TimeSheetEntry timeSheetEntry=new TimeSheetEntry();
		timeSheetEntry.setId(rs.getInt("id"));
		timeSheetEntry.setHours_spent(rs.getDouble("hours_spent"));
		timeSheetEntry.setNotes(rs.getString("notes"));
		timeSheetEntry.setTask_id(rs.getInt("task_id"));
		timeSheetEntry.setTimesheet_id(rs.getInt("timesheet_id"));
		return timeSheetEntry;
	}

	private String createQuery(List<TimeSheetEntryDTO> entries) {
		

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO timesheet_entries(timesheet_id, task_id, notes, hours_spent) values ");
		for (int i = 0; i < entries.size(); i++) {
			builder.append("(?, ?, ?, ?),");
		}
		if (builder.length() > 1)
			builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}

	private PreparedStatement bindQuery(int timeSheetId, PreparedStatement pstmt, List<TimeSheetEntryDTO> entries) throws SQLException {

		// initially set to 1 to bind the value to the sql query. increase to place dynamiclly with respect to the size of the entries
		// array.
		int binder = 1;

		for (TimeSheetEntryDTO entry : entries) {
			// here we increment the binder to place the value in the correct position.
			pstmt.setInt(binder++, timeSheetId);
			pstmt.setInt(binder++, entry.getTask_id());
			pstmt.setString(binder++, entry.getNotes());
			pstmt.setDouble(binder++, entry.getHours_spent());
			// here one entry is finished, still the binder increments to place the next entry values(if present).
		}

		return pstmt;
	}

	public boolean createTimeSheetEntries(int timeSheetId, List<TimeSheetEntryDTO> entries) {

		String sql = createQuery(entries);

		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			PreparedStatement binded_pstmt = bindQuery(timeSheetId, pstmt, entries);

			int affectedRows = binded_pstmt.executeUpdate();

			if (affectedRows <= 0) {
				throw new SQLException("Time sheet entries creation failed.");
			}

			return true;
		} catch (SQLException e) {
			logger.error("Error creating timesheet entries for timesheet {}", timeSheetId, e);
			return false;
		}
	}

	public Optional<List<TimeSheetEntry>> getTimeSheetEntries(int timeSheetId){
		String sql="select * from timesheet_entries where timesheet_id=?";
		try(Connection conn=DataSourceFactory.getConnectionInstance();
			 PreparedStatement pstmt=conn.prepareStatement(sql);){
			pstmt.setInt(1, timeSheetId);
			ResultSet rs=pstmt.executeQuery();
			List<TimeSheetEntry> timeSheetEntries=new ArrayList<>();
			while(rs.next()) {
				TimeSheetEntry timeSheetEntry=getTimeSheetEntry(rs);
				timeSheetEntries.add(timeSheetEntry);
			}
			return Optional.ofNullable(timeSheetEntries);
		} catch (SQLException e) {
			 logger.error("Error fetching timesheet entries for timesheet {}", timeSheetId, e);
			return Optional.ofNullable(null);
		}
	}

	// changed by vasudevan, to provide exact result for the jsp.
	public Optional<List<TimeSheetEntryForReviewDTO>> getEntriesByTimesheetId(int timesheetId) {
		
		// old
		// String sql = "select * from timesheet_entries where timesheetId = ?";
		
		String sql = "select tse.id, t.title, tse.hours_spent, tse.notes from timesheet_entries tse inner join tasks t on tse.task_id = t.id where tse.timesheet_id = ?;";
				
		List<TimeSheetEntryForReviewDTO> list = new ArrayList<>();
		
		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, timesheetId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(new TimeSheetEntryForReviewDTO.Builder().withId(rs.getInt("id")).withTaskTitle(rs.getString("title"))
								.hoursSpent(rs.getDouble("hours_spent")).withTaskNotes(rs.getString("notes")).build());
			}
			
			return Optional.ofNullable(list);
			
		}catch(SQLException e) {
			logger.error("Error fetching review entries for timesheet {}", timesheetId, e);
			return Optional.ofNullable(null);
		}
	}

}
