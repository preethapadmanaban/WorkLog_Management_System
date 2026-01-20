package com.worklog.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.worklog.db.DataSourceFactory;
import com.worklog.entities.Employee;

/**
 * This a employee dao class that adds employee list
 * @author Preetha
 * @since 19-01-2026
 */

public class EmployeeDAO {
	
public static List<Employee> getAllMembers(){
		
		
		List<Employee> list = new ArrayList<>();
		
		String sql = "select id,name,role from employees where role = 'Employee' ";
		
		try(Connection con = DataSourceFactory.getConnectionInstance(); PreparedStatement pstmt = con.prepareStatement(sql)){
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				
				list.add(new Employee(id,name,role));
			}
			 
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

}
