package com.worklog.servlets;

import java.io.IOException;

import com.worklog.config.CommandXMLConfig;
import com.worklog.exceptions.CommandNotFoundException;
import com.worklog.exceptions.UnAuthorizedException;
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

		// System.out.println("QueryString: " + request.getQueryString());
		String action = request.getParameter("action");

		// System.out.println("current action: " + action);
		if (action == null) {
			response.sendRedirect("/worklog/");
			return;
		}

		CommandXMLConfig cmdConfig = CommandXMLFactory.configMap.get(action);

		try {
			if (cmdConfig == null) {
				throw new CommandNotFoundException("Undeifned action.");
			}

			Command cmd = CommandXMLFactory.getCommand(action);
			boolean flag = cmd.execute(request, response);
			String forward;
			if (flag) {
				forward = cmdConfig.getSuccessPage();
			} else {
				forward = cmdConfig.getFailurePage();
			}

			// we implement the download option here instead of putting it as an separate servlet
			if (request.getParameter("download") == null) {
				request.getRequestDispatcher(forward).forward(request, response);
			} else {
				// when the request comes here, then it send as an download not a response. See the respective command for more details.
				return;
			}

		} catch (CommandNotFoundException e) {
			e.printStackTrace();
			request.getRequestDispatcher("/worklog/").forward(request, response);
		} catch (UnAuthorizedException e) {
			System.out.println(e);
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher(CommandXMLFactory.configMap.get("access_denied").getSuccessPage()).include(request, response);
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher(cmdConfig.getFailurePage()).include(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


}
