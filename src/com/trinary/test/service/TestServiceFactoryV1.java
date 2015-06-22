package com.trinary.test.service;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.trinary.security.owasp.factory.ProxyFactory;

@Stateless
public class TestServiceFactoryV1 implements TestServiceFactory {
	@Inject ProxyFactory proxyFactory;
	Class<?>[] interfaces = {TestService.class};
	
	/* (non-Javadoc)
	 * @see com.trinary.test.service.TestServiceFactory#createESignService()
	 */
	@Override
	@Produces
	@Default
	public TestService createESignService() throws IllegalArgumentException, InstantiationException, IllegalAccessException {
		return proxyFactory.createProxy(TestService.class, TestServiceImpl.class);
	}
}