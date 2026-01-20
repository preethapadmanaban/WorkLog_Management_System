package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.worklog.db.DataSourceFactory;
import com.worklog.dto.TimeSheetEntryDTO;

public class TimeSheetEntryDAO {

	private String createQuery(TimeSheetEntryDTO[] entries) {

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO timesheet_entries(timesheet_id, task_id, notes, hours_spent) values ");
		for (int i = 0; i < entries.length; i++) {
			builder.append("(?, ?, ?, ?),");
		}
		if (builder.length() > 1)
			builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}

	private PreparedStatement bindQuery(int timeSheetId, PreparedStatement pstmt, TimeSheetEntryDTO[] entries) throws SQLException {

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

	public boolean createTimeSheetEntries(int timeSheetId, TimeSheetEntryDTO[] entries) {

		String sql = createQuery(entries);

		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			PreparedStatement binded_pstmt = bindQuery(timeSheetId, pstmt, entries);

			int affectedRows = binded_pstmt.executeUpdate();

			if (affectedRows <= 0) {
				throw new SQLException("Time sheet entries creation failed.");
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
