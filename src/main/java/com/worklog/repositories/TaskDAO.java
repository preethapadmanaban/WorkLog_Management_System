package com.worklog.repositories;

import java.sql.Connection;
import java.sql.Date;
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
 * @author Vasudevan Tamizharasan, Preetha
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
	
	public boolean createTask(String title, String description, int assignedTo, String status, Date deadline, int createdBy) {

		String sql = "insert into tasks (title, description, assigned_to, status, deadline, created_by, created_at, updated_at) "
				+ "values (?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp)";

		try (Connection con = DataSourceFactory.getConnectionInstance();
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, title);
			pstmt.setString(2, description);
			pstmt.setInt(3, assignedTo);
			pstmt.setString(4, status);
			pstmt.setDate(5, deadline);
			pstmt.setInt(6, createdBy);

			int rows = pstmt.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Optional<List<Task>> getAllTasks() {
		
		List<Task> task = new ArrayList<>();
		
		String sql = "select * from tasks";
		
		try(Connection con = DataSourceFactory.getConnectionInstance();
						PreparedStatement pstmt = con.prepareStatement(sql)){
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				task.add(mapToTask(rs));
			}
			return Optional.of(task);
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
		}
	}
	
	public Optional<Task> getTaskById(int id){
		
		String sql = "select * from tasks where id = ?";
		
		try(Connection con = DataSourceFactory.getConnectionInstance();
						PreparedStatement pstmt = con.prepareStatement(sql)){
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Task task = mapToTask(rs);
				return Optional.of(task);
			}
			
			return Optional.empty();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
		}
		
	}

	public boolean updateTask(int id, String title, String description, int assigned_to, String status, Date deadline) {
		
		String sql = "update task set title=?, description=?, assigned_to=?, status=?, deadline=? where id = ?";
		
		try(Connection con = DataSourceFactory.getConnectionInstance();
						PreparedStatement pstmt = con.prepareStatement(sql)){
			
			pstmt.setString(1, title);
			pstmt.setString(2, description);
			pstmt.setInt(3, assigned_to);
			pstmt.setString(4, status);
			pstmt.setDate(5, deadline);
			pstmt.setInt(6, id);
			
			int rows = pstmt.executeUpdate();
			
			return rows > 0;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


}
