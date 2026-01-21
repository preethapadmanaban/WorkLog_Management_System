package com.worklog.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * CommandConfig - This class is used as in-memory storing object for the command.properties file to product the action based objects.
 * 
 * @see com.worklog.factories.CommandFactory;
 * 
 * @author Vasudevan
 * @since 20-01-2026
 * 
 */
public class CommandConfig {
	private String commandClass;
	private String successPage;
	private String failurePage;

	public CommandConfig() {

	}

	public CommandConfig(String commandClass, String successPage, String failurePage) {
		this.commandClass = commandClass;
		this.successPage = successPage;
		this.failurePage = failurePage;
	}

	public String getCommandClass() {
		return commandClass;
	}

	public String getSuccessPage() {
		return successPage;
	}

	public String getFailurePage() {
		return failurePage;
	}

	public static Map<String, CommandConfig> loadConfigurations(Properties commandMappings) {
		Map<String, CommandConfig> configMap = new HashMap<String, CommandConfig>();
		if (commandMappings != null) {

			for (String key : commandMappings.stringPropertyNames()) {
				String value = commandMappings.getProperty(key);

				String[] tokens = value.split(",");
				String commandClass = tokens[0];
				String success = "";
				String failure = "";
				for (int i = 0; i < tokens.length; i++) {
					String[] pair = tokens[i].split("=");
					if ("success".equals(pair[0])) {
						success = pair[1];
					} else if ("failure".equals(pair[0])) {
						failure = pair[1];
					}
				}

				CommandConfig cmdConfig = new CommandConfig(commandClass, success, failure);
				configMap.put(key, cmdConfig);
			}
		}
		return configMap;
	}
}
