package com.worklog.factories;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.worklog.config.CommandConfig;
import com.worklog.interfaces.Command;

/**
 * 
 * CommandFactory - this class is used for produce command class objects to perform response for client requests with based on the action
 * provided by the client. action is key point here, because the object were created using the action defined in the command.properties in
 * the com.worklog.resources.command.properties.
 * 
 * @see com.worklog.resources.command.properties for clarity
 * 
 * @author Vasudevan
 * @since 20-02-2026
 */
public class CommandFactory {

	public static Properties commandMappings = new Properties();
	public static Map<String, CommandConfig> configMap = null;
	static {
		// Load properties only once when class is loaded
		try (InputStream is = CommandFactory.class.getClassLoader().getResourceAsStream("com/worklog/resources/command.properties")) {

			if (is == null) {
				throw new RuntimeException("navigation.properties file not found in classpath");
			}

			commandMappings.load(is);
			// logger.debug("Porperty file loaded : "+is);
			if (commandMappings != null) {
				configMap = CommandConfig.loadConfigurations(commandMappings);

			}
			// logger.debug("configuration maping created : "+configMap);
		} catch (Exception e) {
			// logger.error("Failed to load command mappings : "+e.getMessage());
			throw new RuntimeException("Failed to load command mappings", e);
		}
	}

	private CommandFactory() {
		// Prevent object creation
	}

	public static Command getCommand(String action) {

		try {
			if (action == null) {
				System.out.println("Missing action...");
				return null;
			}

			String commandClassName = configMap.get(action).getCommandClass();
			System.out.println("commandClassName : " + commandClassName);
			if (commandClassName == null) {
				return null;
			}

			Class<?> clazz = Class.forName(commandClassName);
			System.out.println("clazz : " + (Command) clazz.getDeclaredConstructor().newInstance());
			return (Command) clazz.getDeclaredConstructor().newInstance();

		} catch (Exception e) {
			// logger.error("Unable to create command for action: " + action, e.getMessage());
			throw new RuntimeException("Unable to create command for action: " + action, e);
		}
	}
}