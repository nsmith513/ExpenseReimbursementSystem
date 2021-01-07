package com.project.one.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.one.Money;
import com.project.one.Support;
import com.project.one.model.ReimbStatus;
import com.project.one.model.ReimbType;
import com.project.one.model.Reimbursement;
import com.project.one.model.User;
import com.project.one.model.UserRole;
import com.project.one.service.ReimbService;
import com.project.one.service.UserService;

/**
 * Controller layer for requests received by {@code JsonRequestHelper}.
 * 
 * @author Nicholas Smith
 */
public class JsonController {

	/**
	 * Checks credentials posted to the servlet from the login page. If the credentials are
	 * valid, a new session is created for the user.
	 * 
	 * @param request - The request sent to the servlet.
	 * @param response - The response sent to the servlet.
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("POST")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return;
		}
		
		// Get writer for response
		PrintWriter writer = response.getWriter();
		
		// Get username and password entered into form
		String uname = request.getParameter("username");
		String pwd   = request.getParameter("password");
		
		// Get user from DB
		User user;
		try {
			user = UserService.getInstance().getUser(uname);
		} catch (SQLException e) {
			Support.logError("SQL exception! " + e.getMessage());
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Error querying database.")));
			return;
		}
		
		// User does not exist
		if (user == null) {
			Support.logInfo("Unable to login, user " + uname + " does not exist.");
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Username not registered.")));
			return;
		}
		
		// Wrong password
		if (!pwd.contentEquals(user.getPassword())) {
			Support.logInfo("Unable to login, incorrect password for " + uname + '.');
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Incorrect password.")));
			return;
		}
		
		// Create session for user
		request.getSession().setAttribute("user", user); // DO NOT SEND JSON BEFORE SETTING SESSION ATTRIBUTE
		// This might cause the client to get a successful result BEFORE the user is stored in the session
		Support.logInfo("Successfully logged in " + uname + '.');
		writer.write(Support.MAPPER.writeValueAsString(new RequestResult(true)));
	}

	/**
	 * Invalidates the current session if one exists.
	 * 
	 * @param request - The request sent to the servlet.
	 * @param response - The response sent to the servlet.
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession sesh = request.getSession(false);
		if (sesh != null) {
			Support.logInfo("Invalidating session.");
			sesh.invalidate();
			return;
		}
		Support.logInfo("No session to invalidate.");
	}

	/**
	 * Sends username of the session's user and all their reimbursements to the client.
	 * 
	 * @param request - The request sent to the servlet.
	 * @param response - The response sent to the servlet.
	 * @throws IOException 
	 */
	public static void retrieveUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("POST")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return;
		}
		
		// Get writer for response
		PrintWriter writer = response.getWriter();
		
		// Get user from session
		User user = (User)request.getSession().getAttribute("user");
		
		// User does not exist
		if (user == null) {
			Support.logInfo("Unable to retrieve reimbursements, no user for session.");
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Not logged in!")));
			return;
		}
		
		// Get username and reimbursements for current user, or all reimbursements if user is a manager
		List<Object> data = new ArrayList<Object>();
		data.add(user.getUsername());
		List<Reimbursement> reimb = null;
		try {
			if (user.getRole() == UserRole.FINANCEMAN) {
				reimb = ReimbService.getInstance().getAllReimbursements();
			} else {
				reimb = ReimbService.getInstance().getReimbursements(user.getId());
			}
		} catch (SQLException e) {
			Support.logError("SQL exception! " + e.getMessage());
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Error querying database.")));
			return;
		}
		
		// No reimbursements for user
		if (reimb == null) {
			Support.logInfo("No reimbursements for " + user.getUsername() + ", sending only username.");
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult(true, "No reimbusements.", data.toArray())));
			return;
		}
		
		// Sort list by date (descending) and send JSON
		Collections.sort(reimb, Collections.reverseOrder(Reimbursement.sortBySubmitted));
		data.addAll(reimb);
		Support.logInfo("Sending username and reimbursements for " + user.getUsername() + '.');
		writer.write(Support.MAPPER.writeValueAsString(new RequestResult(data.toArray())));
	}

	/**
	 * Attempts to add a new reimbursement to the database and send it back to the client.
	 * 
	 * @param request - The request sent to the servlet.
	 * @param response - The response sent to the servlet.
	 * @throws IOException
	 */
	public static void submitReimb(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("POST")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return;
		}
		
		// Get writer for response
		PrintWriter writer = response.getWriter();
		
		// Get user from session
		User user = (User)request.getSession().getAttribute("user");
		
		// User does not exist
		if (user == null) {
			Support.logInfo("Unable to submit reimbursement, no user for session.");
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Not logged in!")));
			return;
		}
		
		// Add reimbursement to DB
		Money amount = new Money(request.getParameter("amount"));
		String desc = request.getParameter("desc");
		ReimbType type = ReimbType.fromString(request.getParameter("type"));
		Reimbursement reimb = null;
		try {
			reimb = ReimbService.getInstance().addReimbursement(amount, (desc == "") ? null : desc, user.getId(), type);
		} catch (SQLException e) {
			Support.logError("SQL exception! " + e.getMessage());
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Error querying database.")));
			return;
		}
		
		// Send reimbursement to client
		if (reimb == null) {
			Support.logError("Could not add reimbursement for " + user.getUsername() + '.');
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Unable to add reimbursement.")));
			return;
		}
		Support.logInfo("Reimbursement added for " + user.getUsername() + ", sending back to client.");
		writer.write(Support.MAPPER.writeValueAsString(new RequestResult(new Reimbursement[] {reimb})));
	}

	/**
	 * Attempts to update the status of a reimbursement.
	 * 
	 * @param request - The request sent to the servlet.
	 * @param response - The response sent to the servlet.
	 * @throws IOException
	 */
	public static void setReimbStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Wrong HTTP method
		if (!request.getMethod().contentEquals("POST")) {
			Support.logInfo("Invalid method " + request.getMethod() + '.');
			return;
		}
		
		// Get writer for response
		PrintWriter writer = response.getWriter();
		
		// Get user from session
		User user = (User)request.getSession().getAttribute("user");
		
		// User does not exist
		if (user == null) {
			Support.logInfo("Unable to update reimbursement status, no user for session.");
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Not logged in!")));
			return;
		}
		
		// User is not a finance manager
		if (user.getRole() != UserRole.FINANCEMAN) {
			Support.logInfo("Unable to update reimbursement status, " + user.getUsername() + " is not a finance manager.");
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("User is not a finance manager.")));
			return;
		}
		
		// Update status
		int reimb = Integer.parseInt(request.getParameter("reimb"));
		ReimbStatus status = ReimbStatus.fromString(request.getParameter("status"));
		try {
			ReimbService.getInstance().setReimbursementStatus(reimb, user.getId(), status);
		} catch (SQLException e) {
			Support.logError("SQL exception! " + e.getMessage());
			writer.write(Support.MAPPER.writeValueAsString(new RequestResult("Error querying database.")));
			return;
		}
		Support.logInfo("Updated reimbursement " + reimb + "'s status to " + status.toString() + " for " + user.getUsername() + '.');
		writer.write(Support.MAPPER.writeValueAsString(new RequestResult(true)));
	}

}
