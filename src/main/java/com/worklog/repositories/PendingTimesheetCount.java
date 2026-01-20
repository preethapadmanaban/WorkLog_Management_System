package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.worklog.db.DataSourceFactory;

/**
 * PendingTimesheetCount- this class has the query to fetch the count of pending timesheet
 * @author Preetha
 * @since 20-01-2026
 */

public class PendingTimesheetCount {
	
	public static int pendingTimeSheet() {
		
		int count = 0;
		
		String sql = "select count(*) from timesheets where status = 'pending' ";
		
		try(Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
			    count = rs.getInt(1);
			}

			// System.out.println("Pending Timesheets Count = " + count);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
