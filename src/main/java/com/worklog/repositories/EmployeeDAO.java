package com.worklog.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.Employee;

public class EmployeeDAO {
	
public static List<Employee> getAllMembers(){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		List<Employee> list = new ArrayList<>();
		
		try {
			
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/worklog_db", "worklog", "worklog");
			
			String sql = "select id,name,role from employees where role = 'Employee' ";
			
			pstmt = con.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				
				list.add(new Employee(id,name,role));
			}
			 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;

	}

}
