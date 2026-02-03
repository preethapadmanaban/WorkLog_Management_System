package com.worklog.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public static final int rowsPerPage = 5;

	// written by vasudevan
	private Task mapToTask(ResultSet rs) throws SQLException {
		return new Task.Builder().withId(rs.getInt("id")).withTitle(rs.getString("title")).withDescription(rs.getString("description"))
						.assignedTo(rs.getInt("assigned_to")).setStatus(TaskStatus.valueOf(rs.getString("status").toUpperCase()))
						.withDeadline(CustomDateFormatter.toLocalFormat(rs.getString("deadline"))).createdBy(rs.getInt("created_by"))
						.createdAt(CustomDateFormatter.toLocalFormat(rs.getString("created_at"), true))
						.updatedAt(CustomDateFormatter.toLocalFormat(rs.getString("updated_at"), true)).build();
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
	public Optional<List<Task>> getAllTasksForEmployee(int employeeId, boolean isPending, int pageNumber) {

		String sql;
		if (pageNumber == 0)
			pageNumber = 1; // default 1st page
		int offset = (pageNumber - 1) * rowsPerPage;

		if (isPending == true) {
			sql = "select * from tasks where assigned_to = " + employeeId + " and ((status ilike " + "'%" + TaskStatus.ASSIGNED.toString()
							+ "%' or status ilike " + "'%" + TaskStatus.IN_PROGRESS.toString() + "%'"
							+ ") or to_char(updated_at, 'YYYY-MM-DD') = to_char(current_timestamp, 'YYYY-MM-DD')) order by created_at";
		} else {
			sql = "SELECT * FROM tasks where assigned_to = " + employeeId + " LIMIT " + rowsPerPage + " OFFSET " + offset;
		}

		return getTasksWithQuery(sql);

	}

	public Optional<List<Task>> filterTasksByStatus(int employeeId, String status, int pageNumber) {

		if (pageNumber == 0)
			pageNumber = 1; // default 1st page
		int offset = (pageNumber - 1) * rowsPerPage;

		String sql = "SELECT * FROM tasks WHERE assigned_to =" + employeeId + " AND status ILIKE '%" + status + "%' LIMIT " + rowsPerPage
						+ " OFFSET " + offset;
		return getTasksWithQuery(sql);

	}

	// written by preetha
	public Optional<Map<String, Integer>> getTaskCountByStatus(int managerId) {

		String sql = "select status, count(*) from tasks where created_by = ? group by status";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, managerId);

			ResultSet rs = pstmt.executeQuery();

			Map<String, Integer> map = new HashMap<>();

			while (rs.next()) {

				String status = rs.getString("status");
				int count = rs.getInt(2);

				map.put(status, count);
			}

			return Optional.of(map);
		} catch (SQLException e) {
			logger.error("DB error in getTaskCountByStatus(managerId={})", managerId, e);
			return Optional.ofNullable(null);
		}
	}

	public boolean createTask(String title, String description, int assignedTo, String status, Date deadline, int createdBy) {

		String sql = "insert into tasks (title, description, assigned_to, status, deadline, created_by, created_at, updated_at) values (?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp)";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

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

	// changed by vasu for manager only tasks.
	public Optional<List<Task>> getAllTasks(int employeeId) {

		List<Task> task = new ArrayList<>();

		String sql = "select * from tasks where assigned_to = ?";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, employeeId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				task.add(mapToTask(rs));
			}
			return Optional.of(task);

		} catch (SQLException e) {
			logger.error("DB error in getAllTasks(employeeId={})", employeeId, e);
			return Optional.ofNullable(null);
		}
	}

	public Optional<Task> getTaskById(int id) {

		String sql = "select * from tasks where id = ?";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Task task = mapToTask(rs);
				return Optional.of(task);
			}

			return Optional.empty();

		} catch (SQLException e) {
			logger.error("DB error in getTaskById(id={})", id, e);
			return Optional.ofNullable(null);
		}

	}

	public boolean updateTask(int id, String title, String description, int assigned_to, String status, Date deadline) {

		String sql = "update tasks set title=?, description=?, assigned_to=?, status=?, deadline=?, updated_at = current_timestamp where id = ?";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, title);
			pstmt.setString(2, description);
			pstmt.setInt(3, assigned_to);
			pstmt.setString(4, status);
			pstmt.setDate(5, deadline);
			pstmt.setInt(6, id);

			int rows = pstmt.executeUpdate();

			return rows > 0;

		} catch (SQLException e) {
			logger.error("DB error in updateTask(id={})", id, e);
			return false;
		}
	}

	public Optional<List<Task>> getAllCompletedTakEmployeeId(int id) {

		List<Task> tasks = new ArrayList<>();
		String sql = "select * from task where assigned_to=? and status='COMPLETED' ";
		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				tasks.add(mapToTask(rs));

			}
			return Optional.ofNullable(tasks);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("DB error in getAllCompletedTakEmployeeId(employeeId={})", id, e);
			return null;
		}
	}

	public boolean updateTask(int id, String status) {

		String sql = "update tasks set status=?, updated_at = current_timestamp where id = ?";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setInt(2, id);

			int affectedRows = pstmt.executeUpdate();

			return affectedRows > 0;

		} catch (SQLException e) {
			logger.error("DB error in updateTask(id={}, status={})", id, status, e);
			return false;
		}
	}

	public Optional<Map<String, Object>> getTasksCreatedByManager(int managerId, Integer empId, String status, String fromDate,
					String toDate,
					int pageNumber) {

		if (pageNumber == 0)
			pageNumber = 1; // default 1st page
		int offset = (pageNumber - 1) * rowsPerPage;

		String selectCount = "SELECT COUNT(*) AS row_count ";
		String selectData = "SELECT * ";
		String query;

		query = " FROM tasks WHERE created_by = " + managerId;

		if (empId != 0 && empId != -1) {
			query = query + " AND assigned_to = " + empId;
		}
		TaskStatus statusEnum;
		try {
			statusEnum = TaskStatus.valueOf(status);
		} catch (IllegalArgumentException | NullPointerException e) {
			logger.error("Status was null while filtering.", e);
			statusEnum = null;
		}

		if (statusEnum != null) {
			query = query + " AND status ILIKE '" + statusEnum.toString() + "'";
		}

		if (fromDate != null && toDate != null && !fromDate.strip().isEmpty() && !toDate.strip().isEmpty()) {
			query = query + " AND created_at::DATE BETWEEN '" + fromDate + "' AND '" + toDate + "'";
		}

		query = query + " LIMIT " + TaskDAO.rowsPerPage + " OFFSET " + offset;

		// String sql = "select * from tasks where created_by =" + managerId + "and (" + empId + "is null or assigned_to =" + empId
		// + ") and ( " + status + " is null or status ilike " + status + ") and ( (" + fromDate + "is null or" + toDate
		// + "is null) or created_at::date between " + fromDate + " and " + toDate + ") order by deadline limit " + rowsPerPage
		// + " offset " + offset;

		List<Task> tasks = getTasksWithQuery(selectData + query).orElse(new ArrayList<Task>());
		int rowCount = getRowCountForQuery(selectCount + query);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tasks", tasks);
		map.put("rowCount", rowCount);
		return Optional.ofNullable(map);
	}

	// public static void main(String[] args) {
	// // List<Task> tasks = new TaskDAO().getTasksCreatedByManager(17, 18, "all", "2026-01-01", "2026-02-03", 1).orElse(null);
	// List<Task> tasks = new TaskDAO().getTasksCreatedByManager(17, 18, "ASSIGNED", null, null, 1).orElse(null);
	// System.out.println("tasks" + tasks);
	// if (tasks != null) {
	// for (Task task : tasks) {
	// System.out.println(task);
	// }
	// }
	//
	// }

	public Optional<List<Task>> getTasksCreatedByManager(int managerId, int pageNumber) {
		
		if (pageNumber == 0)
			pageNumber = 1; // default 1st page
		int offset = (pageNumber - 1) * rowsPerPage;
						
		String sql = " select * from tasks where created_by = " + managerId + " LIMIT " + rowsPerPage + " OFFSET " + offset;

		return getTasksWithQuery(sql);
	}

	public int getRowCountForQuery(String query) {

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(query)) {

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("row_count");
			}

			return 0;

		} catch (SQLException e) {
			logger.error("DB error in get task count ", e);
			return -1;
		}
	}

}
