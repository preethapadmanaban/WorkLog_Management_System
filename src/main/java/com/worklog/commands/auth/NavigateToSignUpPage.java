package com.worklog.commands.auth;

import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NavigateToSignUpPage implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		return true;
	}

}
