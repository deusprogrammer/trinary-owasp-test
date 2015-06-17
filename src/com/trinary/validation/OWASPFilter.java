package com.trinary.validation;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class OWASPFilter implements Filter {
	protected FilterConfig filterConfig;
	protected Class<ParameterObject> parameterMapType;
	@EJB OWASPValidator validator;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
		String parameterMapTypeString = filterConfig.getInitParameter("owasp.parameterObjectType");
		
		if (parameterMapTypeString == null) {
			throw new ServletException("Parameter \"owasp.parameterObjectType\" is a required parameter for OWASPFilter");
		}
		
		try {
			parameterMapType = (Class<ParameterObject>)Class.forName(parameterMapTypeString);
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
		
		if (!ParameterObject.class.isAssignableFrom(parameterMapType)) {
			throw new ServletException("Type " + parameterMapType.getCanonicalName() + " is not assignable from " + ParameterObject.class.getCanonicalName());
		}
	}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		try {
			// Create a new parameter object
			Constructor<ParameterObject> constructor = (Constructor<ParameterObject>)parameterMapType.getConstructor(Map.class);
			ParameterObject object = constructor.newInstance(req.getParameterMap());
			
			// Validate the parameter object
			validator.validate(object);
		} catch (NoSuchMethodException e) {
			throw new ServletException(e);
		} catch (SecurityException e) {
			throw new ServletException(e);
		} catch (InstantiationException e) {
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e);
		} catch (IllegalArgumentException e) {
			throw new ServletException(e);
		} catch (InvocationTargetException e) {
			throw new ServletException(e);
		} catch (OWASPValidationException e) {
			throw new ServletException(e);
		}
	}
}