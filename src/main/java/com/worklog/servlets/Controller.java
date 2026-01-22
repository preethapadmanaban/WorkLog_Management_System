package com.worklog.servlets;

import java.io.IOException;

import com.worklog.config.CommandXMLConfig;
import com.worklog.factories.CommandXMLFactory;
import com.worklog.interfaces.Command;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/controller/*")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("QueryString: " + request.getQueryString());
		String action = request.getParameter("action");

		// System.out.println("current action: " + action);
		if (action == null) {
			response.sendRedirect("/worklog/");
			return;
		}

		Command cmd = CommandXMLFactory.getCommand(action);
		CommandXMLConfig cmdConfig = CommandXMLFactory.configMap.get(action);

		try {
			boolean flag = cmd.execute(request, response);
			String forward;
			if (flag) {
				forward = cmdConfig.getSuccessPage();
			} else {
				forward = cmdConfig.getFailurePage();
			}
			request.getRequestDispatcher(forward).forward(request, response);
		} catch (Exception e) {
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher(cmdConfig.getFailurePage()).include(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


}
