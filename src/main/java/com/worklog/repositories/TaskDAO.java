package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.worklog.db.DataSourceFactory;
import com.worklog.entities.Task;

/**
 * 
 * TaskDAO - used for managing crud operations on tasks in this application on database.
 * 
 * @author Vasudevan Tamizharasan
 * @since 20-01-2026
 * 
 */

public class TaskDAO {

	private Task mapToTask(ResultSet rs) throws SQLException {
		return new Task.Builder().withId(rs.getInt("id")).withTitle(rs.getString("title")).withDescription(rs.getString("description"))
						.assignedTo(rs.getInt("assigned_to")).setStatus(rs.getString("status"))
						.withDeadline(LocalDate.parse(rs.getDate("deadline").toString())).createdBy(rs.getInt("created_by"))
						.createdAt(rs.getTimestamp("created_at")).updatedAt(rs.getTimestamp("updated_at")).build();
	}

	public Optional<List<Task>> getAllTasksForEmployee(int employeeId) {
		
		String sql = "SELECT * FROM tasks where assigned_to = ?";
		
		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, employeeId);

			ResultSet rs = pstmt.executeQuery();

			List<Task> tasks = new ArrayList<Task>();

			while (rs.next()) {
				tasks.add(mapToTask(rs));
			}

			return Optional.ofNullable(tasks);

		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
		}

	}
	
	public Optional<Map<String, Integer>> getTaskCountByStatus() {
		
		String sql = "select status, count(*) from tasks group by status";
		
		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			ResultSet rs = pstmt.executeQuery();
			
			Map<String, Integer> map = new HashMap<>();
			
			while(rs.next()) {
				
				String status = rs.getString("status");
				int count = rs.getInt(2);
				
				map.put(status,count);
			}
			
			return Optional.of(map);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
		}
	}
}
