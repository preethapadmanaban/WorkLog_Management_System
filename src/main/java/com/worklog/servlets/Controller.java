package com.worklog.servlets;

import java.io.IOException;

import com.worklog.config.CommandConfig;
import com.worklog.factories.CommandFactory;
import com.worklog.interfaces.Command;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * Controller - Act as front controller servlet for this application and as a unit in command pattern
 * 
 * @author Vasudevan Tamizharasan
 * @since 20-01-2026
 * 
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Controller() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		CommandConfig cmdConfig = null;
		try {
			String action = request.getParameter("action");
			Command cmd = CommandFactory.getCommand(action);
			boolean flag = cmd.execute(request, response);
			cmdConfig = CommandFactory.configMap.get(action);
			String forward;
			if (flag) {
				forward = cmdConfig.getSuccessPage();
			} else {
				forward = cmdConfig.getFailurePage();
			}
			request.getRequestDispatcher(forward).forward(request, response);
		} catch (Exception e) {
			request.getRequestDispatcher(cmdConfig.getFailurePage()).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
