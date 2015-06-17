package com.trinary.test.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trinary.test.service.TestService;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = -1778574173539761350L;
	
	@EJB
	protected TestService testService;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("PATH: " + req.getPathInfo());
		
		if (req.getPathInfo().equals("/create")) {
			testService.create();
		} else {
			testService.get(req.getPathInfo().substring(1));
		}
		
		resp.setContentType("text/html");
		resp.getWriter().append("<html><h1>TESTY TEST!</h1></html>");
	}
}