package com.project.one.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForwardingMasterServlet
 */
public class ForwardingMasterServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5698991041842033112L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Making everything play nice with browser caching is a nightmare, so for the MVP
		// we'll just tell the browser not to cache any resources we forward to it
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.addHeader("Pragma", "no-cache");
		response.addIntHeader("Expires", 0);
		
		request
			.getRequestDispatcher(ForwardingRequestHelper.process(request, response))
			.forward(request, response);
	}

}
