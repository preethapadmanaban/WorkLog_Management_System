package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import com.worklog.db.DataSourceFactory;
import com.worklog.entities.Employee;

public class LoginDAO {
	Employee employee=null;
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
				
				employee= new Employee.Builder()
								.withId(id)
								.withName(name)
								.withEmail(email)
								.withPassword(password)
								.withrole(role)
								.withActive(active)
								.withCreatedAt(created_at)
								.build();
								

			}
			return Optional.ofNullable(employee);
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			return Optional.ofNullable(null);
			
		}
		
	}

}
