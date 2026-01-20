package com.worklog.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {
	
	private static HikariDataSource datasource;
	
	static {
		
		HikariConfig config = new HikariConfig();
		
		config.setDriverClassName("org.postgresql.Driver");
		
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/worklog_db");
		config.setUsername("worklog");
		config.setPassword("worklog");
		
		config.setMaximumPoolSize(5);
		config.setMinimumIdle(1);
		config.setConnectionTimeout(20000);
		config.setMaxLifetime(180000);
		
		//config.setDataSourceJNDI("hikaricp");
		
		config.setPoolName("hikari-datasource");
		
		datasource = new HikariDataSource(config);
		
	}

	public static Connection getConnectionInstance() {
		
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			System.out.println("Exception while getting connection: " + e.getMessage());
			return null;
		}
		
	}
}


