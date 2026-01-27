package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.worklog.db.DataSourceFactory;
import com.worklog.entities.Employee;
import com.worklog.exceptions.DuplicateUserException;

/**
 * This a employee dao class that adds employee list
 * 
 * @author Preetha, Vasudevan
 * @since 19-01-2026
 */

public class EmployeeDAO {

	public boolean createEmployee(Employee employee) throws DuplicateUserException {

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
		} catch (SQLException e) {
			if (e.getMessage().contains("employees_email_key") || e.getMessage().contains(("duplicate"))) {
				throw new DuplicateUserException("Username already exists");
			}
			e.printStackTrace();
			return false;
		}

	}
	
	public static Optional<List<Employee>> getAllMembers() {

		List<Employee> employeeList = new ArrayList<>();
			
		String sql = "select id,name,role from employees where role ilike 'Employee' ";
			
		try (Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				// list.add(new Employee(id,name,role));
				employeeList.add(new Employee.Builder().withId(id).withName(name).withrole(role).build());
			}

			return Optional.ofNullable(employeeList);

		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);
			}

		}

}
