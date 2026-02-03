package com.worklog.factories;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.config.AppConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {
	
	private static final Logger logger = LogManager.getLogger(DataSourceFactory.class);
	
	private static HikariDataSource datasource;
	
	static {
		
		HikariConfig config = new HikariConfig();
		
		config.setDriverClassName(AppConfig.getProperty("app.database.driver-name"));
		
		config.setJdbcUrl(AppConfig.getProperty("app.database.url"));
		config.setUsername(AppConfig.getProperty("app.database.username"));
		config.setPassword(AppConfig.getProperty("app.database.password"));
		
		config.setMaximumPoolSize(AppConfig.getPropertyInt("app.database.max-connections"));
		config.setMinimumIdle(AppConfig.getPropertyInt("app.database.idle-connections"));
		config.setConnectionTimeout(AppConfig.getPropertyInt("app.database.connection-timeout"));
		config.setMaxLifetime(AppConfig.getPropertyInt("app.database.connection-max-life-time"));
		
		//config.setDataSourceJNDI("hikaricp");
		
		config.setPoolName("hikari-datasource");
		
		datasource = new HikariDataSource(config);
		
		logger.info("HikariCP DataSource initialized successfully.");
		
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


