package com.trinary.validation;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class OWASPValidationFilter implements Filter {
	protected FilterConfig filterConfig;
	protected Class<ParameterObject> parameterMapType;
	@EJB OWASPValidator validator;
	@EJB ParameterObjectFactory poFactory;

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
			ParameterObject object = poFactory.create(parameterMapType, req.getParameterMap());
			validator.validate(object);
		} catch (OWASPValidationException e) {
			throw new ServletException(e);
		}
		
		chain.doFilter(req, res);
	}
}