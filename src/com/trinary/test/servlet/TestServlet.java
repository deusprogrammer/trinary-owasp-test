package com.trinary.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trinary.test.entity.User;
import com.trinary.test.service.TestService;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = -1778574173539761350L;
	
	@Inject
	protected TestService testService;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("PATH: " + req.getPathInfo());
		User user = null;
		
		/*
		try {
			testService = new ProxyFactory().createProxy(TestService.class, TestServiceImpl.class);
		} catch (IllegalArgumentException e) {
			throw new ServletException(e);
		} catch (InstantiationException e) {
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e);
		}
		*/
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		if (req.getPathInfo().equals("/create")) {
			testService.create();
			out.append("<html><h1>Created user.</h1></html>");
		} else {
			user = testService.get(req.getPathInfo().substring(1));
			out.append(testService.someComplexFunction(user));
		}
		
		return;
	}
}