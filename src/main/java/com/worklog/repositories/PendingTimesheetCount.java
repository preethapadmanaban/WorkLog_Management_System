package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.db.DataSourceFactory;

/**
 * PendingTimesheetCount- this class has the query to fetch the count of pending timesheet
 * @author Preetha
 * @since 20-01-2026
 */

public class PendingTimesheetCount {
	
	private static final Logger logger = LogManager.getLogger(PendingTimesheetCount.class);
	
	public static int pendingTimeSheet(int managerId) {
		
		int count = 0;
		
		String sql = "select count(*) from timesheets where status = 'pending' and manager_id = ? ";
		
		try(Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, managerId);

			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
			    count = rs.getInt(1);
			}

			// System.out.println("Pending Timesheets Count = " + count);
		}
		catch(SQLException e) {
		    logger.error("Error fetching pending timesheet count for manager {}", managerId, e);
		}
		return count;
	}

}
