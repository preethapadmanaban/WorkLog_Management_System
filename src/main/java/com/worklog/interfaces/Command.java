package com.worklog.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
	
	boolean execute(HttpServletRequest request, HttpServletResponse response);

}
