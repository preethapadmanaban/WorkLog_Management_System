package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.entities.Employee;
import com.worklog.factories.DataSourceFactory;

// written by renganathan
public class LoginDAO {
	
	private static final Logger logger = LogManager.getLogger(LoginDAO.class);


	public Optional<Employee> getDetails(String emailId) {

		String sql="select * from employees where email=?";
		try(Connection conn=DataSourceFactory.getConnectionInstance();
			PreparedStatement pstmt=conn.prepareStatement(sql)){
			pstmt.setString(1, emailId);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				int id=rs.getInt("id");
				String name=rs.getString("name");
				String email=rs.getString("email");
				String password=rs.getString("password");
				String role=rs.getString("role");
				boolean active=rs.getBoolean("active");
				Timestamp created_at=rs.getTimestamp("created_at");
				
				Employee employee = new Employee.Builder()
								.withId(id)
								.withName(name)
								.withEmail(email)
								.withPassword(password)
								.withrole(role)
								.withActive(active)
								.withCreatedAt(created_at)
								.build();
								
				return Optional.ofNullable(employee);
			} else {
				return Optional.ofNullable(null);
			}
			

		}catch(SQLException e) {
			
		    logger.error("Database error while fetching login details for email: {}", emailId, e);
			return Optional.ofNullable(null);
			
		}
		
	}

}
