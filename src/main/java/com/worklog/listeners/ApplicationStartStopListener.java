package com.worklog.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.exceptions.RequiredResourceNotFoundException;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Application Life cycle Listener implementation class ApplicationStartStopListener
 *
 */
@WebListener
public class ApplicationStartStopListener implements ServletContextListener {
	private final Logger logger = LogManager.getLogger(ApplicationStartStopListener.class);

    public ApplicationStartStopListener() {
        // TODO Auto-generated constructor stub
    }


	public void contextInitialized(ServletContextEvent sce) {
		try {
			System.out.println("Servlet context loaded.");
			Class.forName("com.worklog.factories.CommandXMLFactory");
			logger.info("Command factory and commands are loaded into the application successfully.");
			Class.forName("com.worklog.db.DataSourceFactory");
			logger.info("Hikari datasource factory loaded into the application successfully.");
		} catch (ClassNotFoundException e) {
			logger.error("Exception while loading resources, ", e);
			sce.getServletContext().setAttribute("STARTUP_FAILED", true);
			throw new RequiredResourceNotFoundException("Exception while loading resource.", e);
		}
    }


    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }
	
}
