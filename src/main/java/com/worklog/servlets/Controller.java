package com.worklog.servlets;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.config.CommandXMLConfig;
import com.worklog.exceptions.CommandNotFoundException;
import com.worklog.exceptions.RequiredResourceNotFoundException;
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
	private static final Logger logger = LogManager.getLogger(Controller.class);

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// System.out.println("QueryString: " + request.getQueryString());
		String action = request.getParameter("action");

		boolean isAjax = "fetch".equalsIgnoreCase(request.getHeader("X-Requested-With"));
		boolean isApiRequest = request.getRequestURI().contains("/api");
		boolean isDownloadRequest = request.getRequestURI().contains("/download");
		// System.out.println("current action: " + action);
		if (action == null) {
			logger.error("Exception in controller : action not found on the url - " + request.getContextPath() + request.getQueryString());
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

			if (isAjax || isApiRequest) {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				String msg = (String) request.getAttribute("message");
				if (msg != null) {
					msg = msg.replace("\"", "\\\"");
				}
				String status = flag == true ? "success" : "error";

				response.getWriter().write("{\"status\": \"" + status + "\",\"message\":\"" + msg + "\"}");
				return;
			} else if (isDownloadRequest) {
				response.setContentType("text/csv");
				response.setCharacterEncoding("UTF-8");
				return;
			}

			String forward;
			if (flag) {
				forward = cmdConfig.getSuccessPage();
			} else {
				forward = cmdConfig.getFailurePage();
			}

			request.getRequestDispatcher(forward).forward(request, response);

		} catch (CommandNotFoundException e) {
			// e.printStackTrace();
			logger.error("Command not found for query String :" + request.getQueryString(), e);
			request.getRequestDispatcher("/worklog/").forward(request, response);
		} catch (UnAuthorizedException e) {
			logger.error("Authorization error: ", e);
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher(CommandXMLFactory.configMap.get("access_denied").getSuccessPage()).include(request, response);
		} catch (RequiredResourceNotFoundException e) {
			response.sendError(503, "Service Unavailable!");
		} catch (Exception e) {
			logger.error("General exception: ", e);
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher(cmdConfig.getFailurePage()).include(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
