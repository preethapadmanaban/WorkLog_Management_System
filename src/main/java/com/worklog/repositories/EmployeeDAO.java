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


import com.worklog.commands.dashboard.ManagerDashboardCommand;
import com.worklog.entities.Employee;
import com.worklog.factories.DataSourceFactory;

public class EmployeeDAO {

	private static final Logger logger = LogManager.getLogger(EmployeeDAO.class);
	public static final int rowsPerPage = 5;

	public boolean createEmployee(Employee employee) throws SQLException {

		String sql = "INSERT INTO employees(name, email, password, role) VALUES(?, ?, ?, ?)";

		try (Connection conn = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, employee.getName());
			pstmt.setString(2, employee.getEmail());
			pstmt.setString(3, employee.getPassword());
			pstmt.setString(4, employee.getRole());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows <= 0)
				throw new SQLException("Employee creation failed.");

			return true;

		}
		// catch (PSQLException e) {
		// /*
		// * Analyze the SQLState for confirming the error occurs is Unique constraint violation. For that im checking the PostgreSQL docs
		// * to see the error code thrown by PostgreSQL server. PostgreSQL docs shows that the SQLState starts with "23" is the constraint
		// * violation error and "23505" is unique_constraint_violation error.
		// *
		// * @see - https://www.postgresql.org/docs/18/errcodes-appendix.html
		// */

		// } catch (SQLException e) {
		// // old version
		// // if (e.getMessage().contains("employees_email_key") || e.getMessage().contains(("duplicate"))) {
		// // logger.warn("Attempt to create duplicate employee with email: {}", employee.getEmail());
		// // throw new DuplicateUserException("Email already exists");
		// // }

		// logger.error("Error while creating employee with email: {}", employee.getEmail(), e);
		// throw e;
		// }

	}

	// written by preetha
	public static Optional<List<Employee>> getAllMembers(String manager,int offset) {

		List<Employee> employeeList = new ArrayList<>();

		String sql = "";
		int limit=0;
		if(manager.equalsIgnoreCase("managerdashboard")) {
			 limit= ManagerDashboardCommand.PAGE_SIZE;
			sql="SELECT * FROM employees LIMIT ? OFFSET ?";
		}else {
			sql="select id,name,role from employees where role ilike 'Employee' ";
		}
		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			if(manager.equalsIgnoreCase("managerDashboard")) {
				
				pstmt.setInt(1, limit);
				pstmt.setInt(2,offset);
			}
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				// list.add(new Employee(id,name,role));
				employeeList.add(new Employee.Builder().withId(id).withName(name).withrole(role).build());
			}
			System.out.println("employeeList"+employeeList);

			return Optional.ofNullable(employeeList);

		} catch (SQLException e) {
			logger.error("Error fetching employee members list", e);
			return Optional.ofNullable(null);
		}

	}

	// written by preetha
	public static Optional<String> getEmployeeNameById(int id) {

		String sql = " select name from employees where id = ?";
		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return Optional.ofNullable(rs.getString("name"));
			}

		} catch (SQLException e) {
			logger.error("Error fetching employee names", e);
			return Optional.ofNullable(null);
		}
		return Optional.empty();

	}
	public static int getEmployeeCount() {
		String sql="select count(*) as count from employees";
		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()) {
				return rs.getInt("count");
			}
			return 0;
		} catch (SQLException e) {
			logger.error("Error fetching employee names", e);
			return 0;
			
		}
		
	}
	

}
