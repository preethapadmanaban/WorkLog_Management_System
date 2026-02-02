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

	// written by preetha (pagination + dynamic filters)
	/*
	 * public Optional<PagedResult<Task>> getTasksCreatedByManager(int managerId, Integer empId, String status, LocalDate fromDate,
	 * LocalDate toDate, int page, int size) {
	 * 
	 * List<Task> tasks = new ArrayList<>();
	 * 
	 * if (page < 1) page = 1; if (size < 1) size = 10;
	 * 
	 * int offset = (page - 1) * size;
	 * 
	 * StringBuilder sql = new StringBuilder(); sql.append("select t.*, count(*) over() as total_count "); sql.append("from tasks t ");
	 * sql.append("where t.created_by = ? ");
	 * 
	 * if (empId != null) { sql.append(" and t.assigned_to = ? "); }
	 * 
	 * if (status != null && !status.isBlank() && !status.equalsIgnoreCase("all")) { sql.append(" and t.status ilike ? "); }
	 * 
	 * if (fromDate != null && toDate != null) { sql.append("and t.created_at::date between ? and ? "); }
	 * 
	 * sql.append(" order by t.deadline "); sql.append(" limit ? offset ? ");
	 * 
	 * try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
	 * 
	 * int idx = 1;
	 * 
	 * pstmt.setInt(idx++, managerId);
	 * 
	 * if (empId != null) { pstmt.setInt(idx++, empId); }
	 * 
	 * if (status != null && !status.isBlank() && !status.equalsIgnoreCase("all")) { pstmt.setString(idx++, "%" + status + "%"); }
	 * 
	 * if (fromDate != null && toDate != null) { pstmt.setDate(idx++, java.sql.Date.valueOf(fromDate)); pstmt.setDate(idx++,
	 * java.sql.Date.valueOf(toDate)); }
	 * 
	 * pstmt.setInt(idx++, size); pstmt.setInt(idx++, offset);
	 * 
	 * ResultSet rs = pstmt.executeQuery();
	 * 
	 * int totalCount = 0;
	 * 
	 * while (rs.next()) { tasks.add(mapToTask(rs)); totalCount = rs.getInt("total_count"); // same value for every row }
	 * 
	 * return Optional.of(new PagedResult<>(tasks, totalCount));
	 * 
	 * } catch (SQLException e) {
	 * logger.error("DB error in getTasksCreatedByManager(managerId={}, empId={}, status={}, from={}, to={}, page={}, size={})", managerId,
	 * empId, status, fromDate, toDate, page, size, e); return Optional.empty(); } }
	 */

	public Optional<List<Task>> getTasksCreatedByManager(int managerId, Integer empId, String status, LocalDate fromDate,
					LocalDate toDate) {

		List<Task> tasks = new ArrayList<>();

		String sql = "select * from tasks " + "where created_by = ? " + "and (? is null or assigned_to = ?) "
						+ "and (? is null or status ilike ?) " + "and ( (? is null or ? is null) or created_at::date between ? and ? ) "
						+ "order by deadline";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			int idx = 1;

			pstmt.setInt(idx++, managerId);

			// emp filter
			pstmt.setObject(idx++, empId);
			pstmt.setObject(idx++, empId);

			// status filter
			pstmt.setString(idx++, status);
			pstmt.setString(idx++, status);

			// date filter
			java.sql.Date f = (fromDate == null) ? null : java.sql.Date.valueOf(fromDate);
			java.sql.Date t = (toDate == null) ? null : java.sql.Date.valueOf(toDate);

			pstmt.setDate(idx++, f);
			pstmt.setDate(idx++, t);
			pstmt.setDate(idx++, f);
			pstmt.setDate(idx++, t);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					tasks.add(mapToTask(rs));
				}
			}

			return Optional.of(tasks);

		} catch (SQLException e) {
			logger.error("DB error in getTasksCreatedByManager(managerId={}, empId={}, status={}, from={}, to={})", managerId, empId,
							status, fromDate, toDate, e);
			return Optional.empty();
		}
	}

	public Optional<List<Task>> getTasksCreatedByManager(int managerId) {

		List<Task> tasks = new ArrayList<>();
		String sql = " select * from tasks where created_by = ? ";

		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, managerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					tasks.add(mapToTask(rs));
				}
			}

			return Optional.of(tasks);

		} catch (SQLException e) {
			logger.error("DB error in getTasksCreatedByManager(managerId={})", managerId, e);
			return Optional.empty();
		}
	}

}
