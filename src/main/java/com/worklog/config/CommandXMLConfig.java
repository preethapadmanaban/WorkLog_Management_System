package com.worklog.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CommandXMLConfig {
	private String commandClass;
	private String successPage;
	private String failurePage;

	public CommandXMLConfig() {

	}

	public CommandXMLConfig(String commandClass, String successPage, String failurePage) {
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

	public static Map<String, CommandXMLConfig> loadConfigurations(InputStream commandConfigFile) {
		Map<String, CommandXMLConfig> configMap = new HashMap<String, CommandXMLConfig>();

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(commandConfigFile);

			NodeList commandList = doc.getElementsByTagName("command");

			for (int i = 0; i < commandList.getLength(); i++) {
				Element command = (Element) commandList.item(i);

				String commandKey = command.getAttribute("name");

				String commandClass = command.getElementsByTagName("class").item(0).getTextContent();

				String successPage = command.getElementsByTagName("success").item(0).getTextContent();

				String failurePage = command.getElementsByTagName("failure").item(0).getTextContent();

				CommandXMLConfig cmdConfig = new CommandXMLConfig(commandClass, successPage, failurePage);

				configMap.put(commandKey, cmdConfig);

			}

			return configMap;

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}


	}
}
