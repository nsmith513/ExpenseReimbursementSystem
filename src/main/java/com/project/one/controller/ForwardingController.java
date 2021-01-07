package com.project.one.controller;

import javax.servlet.http.HttpServletRequest;

import com.project.one.Support;
import com.project.one.model.User;
import com.project.one.model.UserRole;

/**
 * Controller layer for requests received by {@code ForwardingRequestHelper}.
 * 
 * @author Nicholas Smith
 */
public class ForwardingController {
	
	/**
	 * Returns the login page resource URI, or index.html if a method other
	 * than GET was used to make the request.
	 * 
	 * @param request - The request sent to the servlet.
	 * @return An HTML URI.
	 */
	public static String getLoginPage(HttpServletRequest request) {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("GET")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return "/index.html";
		}
		
		Support.logInfo("Forwarding login page.");
		return "/resources/html/login.html";
	}

	/**
	 * Returns the HTML resource associated with the home of the user currently
	 * stored in {@code request}'s session, or index.html if the session
	 * has no user.
	 * 
	 * @param request - The request sent to the servlet.
	 * @return An HTML URI.
	 */
	public static String getHome(HttpServletRequest request) {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("GET")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return "/index.html";
		}
		
		// Get user from session
		User user = (User)request.getSession().getAttribute("user");
		
		// User does not exist
		if (user == null) {
			Support.logInfo("Unable to go to home page, no user for the session.");
			return "/index.html";
		}
		
		Support.logInfo("Found user " + user.getUsername() + " for session, forwarding home page.");
		return (user.getRole() == UserRole.EMPLOYEE) ?
				"/resources/html/home-empl.html" :
				"/resources/html/home-fman.html";
	}

	/**
	 * Returns the HTML resource for creating new reimbursement tickets,
	 * or index.html if the session has no user or the user is not an employee.
	 * 
	 * @param request - The request sent to the servlet.
	 * @return An HTML URI.
	 */
	public static String getReimbForm(HttpServletRequest request) {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("GET")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return "/index.html";
		}
		
		User user = (User)request.getSession().getAttribute("user");
		
		// No user for session
		if (user == null) {
			Support.logInfo("Unable to go to new ticket page, no user for the session.");
			return "/index.html";
		}
		
		// User is not an employee
		if (user.getRole() != UserRole.EMPLOYEE) {
			Support.logInfo("Unable to go to new ticket page, user is not an employee.");
			return "/index.html";
		}
		
		return "/resources/html/new-ticket-empl.html";
	}

	/**
	 * Returns the HTML resource for resolving reimbursement tickets,
	 * or index.html if the session has no user or the user is not a finance manager.
	 * 
	 * @param request - The request sent to the servlet.
	 * @return An HTML URI.
	 */
	public static String getReimbResolve(HttpServletRequest request) {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("GET")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return "/index.html";
		}
		
		User user = (User)request.getSession().getAttribute("user");
		
		// No user for session
		if (user == null) {
			Support.logInfo("Unable to go to new ticket page, no user for the session.");
			return "/index.html";
		}
		
		// User is not a finance manager
		if (user.getRole() != UserRole.FINANCEMAN) {
			Support.logInfo("Unable to go to new ticket page, user is not a finance manager.");
			return "/index.html";
		}
		
		return "/resources/html/resolve-tickets-fman.html";
	}

}
