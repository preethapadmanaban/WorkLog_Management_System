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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.commands.constants.TaskStatus;
import com.worklog.db.DataSourceFactory;
import com.worklog.entities.Task;
import com.worklog.utils.CustomDateFormatter;


public class TaskDAO {
	
	private static final Logger logger = LogManager.getLogger(TaskDAO.class);

	// written by vasudevan
	private Task mapToTask(ResultSet rs) throws SQLException {
		return new Task.Builder().withId(rs.getInt("id")).withTitle(rs.getString("title")).withDescription(rs.getString("description"))
						.assignedTo(rs.getInt("assigned_to")).setStatus(TaskStatus.valueOf(rs.getString("status").toUpperCase()))
						.withDeadline(CustomDateFormatter.toLocalFormat(rs.getString("deadline"))).createdBy(rs.getInt("created_by"))
						.createdAt(CustomDateFormatter.toLocalFormat(rs.getString("created_at"), true))
						.updatedAt(CustomDateFormatter.toLocalFormat(rs.getString("updated_at"), true))
						.build();
	}

	public Optional<List<Task>> getTasksWithQuery(String query) {
		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			ResultSet rs = pstmt.executeQuery();

			List<Task> tasks = new ArrayList<Task>();

			while (rs.next()) {
				tasks.add(mapToTask(rs));
			}

			return Optional.ofNullable(tasks);

		} catch (SQLException e) {
			logger.error("Exception while fetch the task.", e);
			return Optional.ofNullable(null);
		}
	}

	// written by vasudevan
	public Optional<List<Task>> getAllTasksForEmployee(int employeeId, boolean isPending) {

		String sql;

		if (isPending == true) {
			sql = "select * from tasks where assigned_to = " + employeeId + " and ((status ilike " + "'%" + TaskStatus.ASSIGNED.toString()
							+ "%' or status ilike " + "'%" + TaskStatus.IN_PROGRESS.toString() + "%'"
							+ ") or to_char(updated_at, 'YYYY-MM-DD') = to_char(current_timestamp, 'YYYY-MM-DD')) order by created_at";
		} else {
			sql = "SELECT * FROM tasks where assigned_to = " + employeeId;
		}
		
		return getTasksWithQuery(sql);

	}
	
	public Optional<List<Task>> filterTasksByStatus(int employeeId, String status) {

		String sql = "SELECT * FROM tasks WHERE assigned_to =" + employeeId + " AND status ILIKE '%" + status + "%'";
		return getTasksWithQuery(sql);

	}

	// written by preetha
	public Optional<Map<String, Integer>> getTaskCountByStatus(int managerId) {
		
		String sql = "select status, count(*) from tasks where created_by = ? group by status";
		
		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, managerId);

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
		    logger.error("DB error in getTaskCountByStatus(managerId={})", managerId, e);
			return Optional.ofNullable(null);
		}
	}
	
	public boolean createTask(String title, String description, int assignedTo, Date deadline, int createdBy) {

		String sql = "insert into tasks (title, description, assigned_to, status, deadline, created_by, created_at, updated_at) values (?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp)";

		try (Connection con = DataSourceFactory.getConnectionInstance(); 
			 	PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, title);
			pstmt.setString(2, description);
			pstmt.setInt(3, assignedTo);
			pstmt.setString(4, TaskStatus.ASSIGNED.toString());
			pstmt.setDate(5, deadline);
			pstmt.setInt(6, createdBy);

			int rows = pstmt.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
		    logger.error("DB error in createTask(createdBy={}, assignedTo={}, title={})", createdBy, assignedTo, title, e);
			return false;
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
			 logger.error("DB error in getTaskById(id={})", id, e);
			return Optional.ofNullable(null);
		}
		
	}

	public boolean updateTask(int id, String title, String description, int assigned_to, String status, Date deadline) {
		
		String sql = "update tasks set title=?, description=?, assigned_to=?, status=?, deadline=?, updated_at = current_timestamp where id = ?";
		
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
			logger.error("DB error in updateTask(id={})", id, e);
			return false;
		}
	}

	public boolean updateTask(int id, String status) {

		String sql = "UPDATE tasks SET status=?, updated_at = current_timestamp WHERE id = ?";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setInt(2, id);

			int affectedrows = pstmt.executeUpdate();

			return affectedrows > 0;

		} catch (SQLException e) {
			logger.error("DB error in updateTask(id={})", id, e);
			return false;
		}
	}

	public Optional<List<Task>> getTasksCreatedByManager(int managerId, int empId, String status, LocalDate fromDate, LocalDate toDate) {

		List<Task> tasks = new ArrayList<>();
		String sql;
		if (status.equalsIgnoreCase("all")) {
			sql = "select * from tasks where (created_by = ? and assigned_to = ?) and TO_CHAR(created_at,'YYYY-MM-DD') between ? and ? order by deadline";
		} else {
			sql = "select * from tasks where (created_by = ? and assigned_to = ?) and status ilike ? and TO_CHAR(created_at,'YYYY-MM-DD') between ? and ? order by deadline";
		}

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, managerId);
			pstmt.setInt(2, empId);

			if (status.equalsIgnoreCase("all")) {
				pstmt.setString(3, fromDate.toString());
				pstmt.setString(4, toDate.toString());
			} else {
				pstmt.setString(3, status);
				pstmt.setString(4, fromDate.toString());
				pstmt.setString(5, toDate.toString());
			}

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tasks.add(mapToTask(rs));
			}

			return Optional.ofNullable(tasks);

		} catch (SQLException e) {
			logger.error("DB error in getTasksCreatedByManager(managerId={}, empId={}, status={}, from={}, to={})", managerId, empId,
							status, fromDate, toDate, e);
			return Optional.ofNullable(null);
		}
	}

	public Optional<List<Task>> getTasksCreatedByManager(int managerId) {

		List<Task> tasks = new ArrayList<>();

		String sql = "select * from tasks where created_by = ? ";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, managerId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tasks.add(mapToTask(rs));
			}

			return Optional.of(tasks);

		} catch (SQLException e) {
			logger.error("DB error in getTasksCreatedByManager(managerId={})", managerId, e);
			return Optional.empty();
		}
	}
}
