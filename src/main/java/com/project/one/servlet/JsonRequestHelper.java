package com.project.one.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.one.Support;
import com.project.one.controller.JsonController;

/**
 * Helper for requests received by {@code JsonMasterServlet}.
 * 
 * @author Nicholas Smith
 */
public class JsonRequestHelper {

	public static void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch (request.getRequestURI()) {
		
		case "/ExpenseReimbursementSystem/authenticate":
			JsonController.authenticateUser(request, response);
			break;
		case "/ExpenseReimbursementSystem/logout":
			JsonController.logout(request, response);
			break;
		case "/ExpenseReimbursementSystem/retrieve/user-info":
			JsonController.retrieveUserInfo(request, response);
			break;
		case "/ExpenseReimbursementSystem/submit/new-ticket":
			JsonController.submitReimb(request, response);
			break;
		case "/ExpenseReimbursementSystem/submit/update-ticket-status":
			JsonController.setReimbStatus(request, response);
			break;
		
		default:
			Support.logError("Post request URI " + request.getRequestURI() + " was not recognized.");
		
		}
	}

}
