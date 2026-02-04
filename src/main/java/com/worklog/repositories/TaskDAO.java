package com.worklog.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.config.AppConfig;
import com.worklog.constants.TaskPriority;
import com.worklog.constants.TaskStatus;
import com.worklog.dto.ListResultWithRowCount;
import com.worklog.entities.Task;
import com.worklog.factories.DataSourceFactory;
import com.worklog.utils.CustomDateFormatter;

public class TaskDAO {

	public static int rowsPerPage = AppConfig.getPropertyInt("app.pagination.rows-per-page");
	private static final Logger logger = LogManager.getLogger(TaskDAO.class);

	// written by vasudevan
	private Task mapToTask(ResultSet rs) throws SQLException {
		return new Task.Builder().withId(rs.getInt("id")).withTitle(rs.getString("title")).withDescription(rs.getString("description"))
						.assignedTo(rs.getInt("assigned_to")).setStatus(TaskStatus.valueOf(rs.getString("status").toUpperCase()))
						.withDeadline(CustomDateFormatter.toLocalFormat(rs.getString("deadline"))).createdBy(rs.getInt("created_by"))
						.createdAt(CustomDateFormatter.toLocalFormat(rs.getString("created_at"), true))
						.updatedAt(CustomDateFormatter.toLocalFormat(rs.getString("updated_at"), true)).build();
	}

	private Optional<List<Task>> getTasksWithQuery(String query) {

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

	public Optional<List<Task>> getWorkingTasksByEmployee(int employeeId) {

		String query = String.format(
						"SELECT * FROM tasks WHERE assigned_to = %d AND ( status ILIKE \'%s\' OR (status ILIKE \'%s\' AND updated_at::DATE = CURRENT_DATE) ) ORDER BY updated_at",
						employeeId, TaskStatus.IN_PROGRESS.toString(), TaskStatus.COMPLETED.toString());

		return getTasksWithQuery(query);

	}

	// written by vasudevan
	public Optional<Map<String, List<Task>>> getAllTasksForEmployee(int employeeId) {

		String query = String.format("SELECT id,title, description, deadline, status," + " CASE"
						+ " WHEN status = 'ASSIGNED' AND deadline > CURRENT_DATE THEN 'LOW'"
						+ " WHEN status = 'ASSIGNED' AND deadline <= CURRENT_DATE THEN 'HIGH'"
						+ " WHEN status = 'IN_PROGRESS' AND deadline <= CURRENT_DATE THEN 'HIGH'"
						+ " WHEN status = 'IN_PROGRESS' AND deadline > CURRENT_DATE THEN 'LOW'"
						+ " ELSE 'LOW'"
						+ " END AS priority"
						+ " FROM tasks WHERE assigned_to = %d AND ((status ILIKE \'%s\' or status ILIKE \'%s\')"
						+ " OR updated_at::DATE = CURRENT_DATE)" + " ORDER BY deadline", employeeId, TaskStatus.ASSIGNED.toString(),
						TaskStatus.IN_PROGRESS.toString());


		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			ResultSet rs = pstmt.executeQuery();

			// List<Task> tasks = new ArrayList<Task>();
			Map<String, List<Task>> tasks = new HashMap<String, List<Task>>();

			while (rs.next()) {

				TaskStatus status;

				try {
					status = TaskStatus.valueOf(rs.getString("status").toUpperCase());
				} catch (IllegalArgumentException | NullPointerException e) {
					status = TaskStatus.UNKNOWN;
				}
				TaskPriority priority;
				try {
					priority = TaskPriority.valueOf(rs.getString("priority").toUpperCase());
				} catch (IllegalArgumentException | NullPointerException e) {
					priority = TaskPriority.UNKNOWN;
				}

				Task task = new Task.Builder().withId(rs.getInt("id")).withTitle(rs.getString("title"))
								.withDescription(rs.getString("description"))
								.withDeadline(CustomDateFormatter.toLocalFormat(rs.getString("deadline")))
								.setStatus(status).setPriority(priority).build();

				if (status.equals(TaskStatus.ASSIGNED)) {
					List<Task> assignedTasks = tasks.get(TaskStatus.ASSIGNED.name());
					if (assignedTasks == null) {
						tasks.put(TaskStatus.ASSIGNED.name(), new ArrayList<Task>());
						assignedTasks = tasks.get(TaskStatus.ASSIGNED.name());
					}
					assignedTasks.add(task);

				} else if (status.equals(TaskStatus.IN_PROGRESS)) {
					List<Task> inProgressTasks = tasks.get(TaskStatus.IN_PROGRESS.name());
					if (inProgressTasks == null) {
						tasks.put(TaskStatus.IN_PROGRESS.name(), new ArrayList<Task>());
						inProgressTasks = tasks.get(TaskStatus.IN_PROGRESS.name());
					}
					inProgressTasks.add(task);
				} else {
					List<Task> completedTasks = tasks.get(TaskStatus.COMPLETED.name());
					if (completedTasks == null) {
						tasks.put(TaskStatus.COMPLETED.name(), new ArrayList<Task>());
						completedTasks = tasks.get(TaskStatus.COMPLETED.name());
					}
					completedTasks.add(task);
				}

			}

			return Optional.of(tasks);

		} catch (SQLException e) {
			logger.error("Exception while fetch the task.", e);
			return Optional.empty();
		}
	}

	public Optional<ListResultWithRowCount<Task>> filterTasksByStatus(int employeeId, String status, int pageNumber) {

		if (pageNumber == 0)
			pageNumber = 1; // default 1st page
		int offset = (pageNumber - 1) * rowsPerPage;

		String selectCount = "SELECT COUNT(*) AS row_count ";
		String selectData = "SELECT * ";
		String query;


		TaskStatus statusEnum;
		try {
			statusEnum = TaskStatus.valueOf(status);
		} catch (IllegalArgumentException | NullPointerException e) {
			statusEnum = null;
		}
		
		if (statusEnum != null) {
			query = " FROM tasks WHERE assigned_to =" + employeeId + " AND status ILIKE '%" + status + "%' LIMIT " + rowsPerPage
							+ " OFFSET " + offset;
		}
		else {
			query = " FROM tasks where assigned_to = " + employeeId + " LIMIT " + rowsPerPage + " OFFSET " + offset;
		}

		List<Task> tasks = getTasksWithQuery(selectData + query).orElse(Collections.emptyList());
		int rowCount = getRowCountForQuery(selectCount + query);
		return Optional.of(new ListResultWithRowCount<Task>(tasks, rowCount));

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

	public Optional<ListResultWithRowCount<Task>> getTasksCreatedByManager(int managerId, Integer empId, String status, String fromDate,
					String toDate, int pageNumber) {

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
		return Optional.of(new ListResultWithRowCount<Task>(tasks, rowCount));

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
