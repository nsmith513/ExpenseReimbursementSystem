package com.project.one.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.one.Support;
import com.project.one.controller.ForwardingController;

/**
 * Helper for requests received by {@code ForwardingMasterServlet}.
 * 
 * @author Nicholas Smith
 */
public class ForwardingRequestHelper {

	public static String process(HttpServletRequest request, HttpServletResponse response) {
		switch (request.getRequestURI()) {
		
		case "/ExpenseReimbursementSystem/login":
			return ForwardingController.getLoginPage(request);
		case "/ExpenseReimbursementSystem/account/home":
			return ForwardingController.getHome(request);
		case "/ExpenseReimbursementSystem/account/new-ticket":
			return ForwardingController.getReimbForm(request);
		case "/ExpenseReimbursementSystem/account/resolve-tickets":
			return ForwardingController.getReimbResolve(request);
			
		}
		
		Support.logError("Get request URI " + request.getRequestURI() + " was not recognized, forwarding index.");
		return "/index.html";
	}

}
