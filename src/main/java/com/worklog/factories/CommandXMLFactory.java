package com.worklog.factories;

import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.config.CommandXMLConfig;
import com.worklog.exceptions.CommandNotFoundException;
import com.worklog.exceptions.RequiredResourceNotFoundException;
import com.worklog.interfaces.Command;

public class CommandXMLFactory {
	
	private static final Logger logger = LogManager.getLogger(CommandXMLFactory.class);

	public static Map<String, CommandXMLConfig> configMap = null;
	static {
		// Load properties only once when class is loaded
		// try (InputStream is = CommandFactory.class.getClassLoader().getResourceAsStream("com/worklog/resources/commands.properties")) {
		try (InputStream is = CommandXMLFactory.class.getClassLoader().getResourceAsStream("com/worklog/resources/commands.xml")) {

			if (is == null) {
				logger.error("commands.xml file not found in classpath");
				throw new RuntimeException("commands.xml file not found in classpath");
			}

			// logger.debug("Porperty file loaded : "+is);
			configMap = CommandXMLConfig.loadConfigurations(is);

			if (configMap == null) {
				throw new RequiredResourceNotFoundException("Exception while load the commands config.");
			}

			// logger.debug("configuration maping created : "+configMap);
		} catch (ClassNotFoundException e) {
			throw new RequiredResourceNotFoundException("Exception while finding command class.", e);
		} catch (Exception e) {
			// logger.error("Failed to load command mappings : "+e.getMessage());
			logger.error("Failed to load command mappings from commands.xml", e);
			throw new RequiredResourceNotFoundException("Failed to load command mappings", e);
		}
	}

	private CommandXMLFactory() {
		// Prevent object creation
	}

	public static Command getCommand(String action) throws Exception {

		try {
			if (action == null) {
				logger.error("Received null action while fetching command");
				return null;
			}

			String commandClassName = configMap.get(action).getCommandClass();
			// System.out.println("commandClassName : " + commandClassName);
			if (commandClassName == null) {
				return null;
			}

			Class<?> clazz = Class.forName(commandClassName);
			// System.out.println("clazz : " + (Command) clazz.getDeclaredConstructor().newInstance());
			return (Command) clazz.getDeclaredConstructor().newInstance();

		} catch (ClassNotFoundException e) {
			throw new CommandNotFoundException("Exception while finding the command class", e);
		}
		catch (Exception e) {
			logger.error("Exception in getting the respective command class for the given action.", e);
			throw e;
		}

	}
}
