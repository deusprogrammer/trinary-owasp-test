package com.trinary.test.service;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.trinary.security.owasp.annotations.OWASPSecuredProxy;
import com.trinary.security.owasp.factory.ProxyFactory;

@Stateless
public class TestServiceFactoryV1 implements TestServiceFactory {
	@Inject ProxyFactory proxyFactory;
	@Inject TestService testService;
	
	/* (non-Javadoc)
	 * @see com.trinary.test.service.TestServiceFactory#createESignService()
	 */
	@Override
	@Produces
	@OWASPSecuredProxy
	public TestService createESignService() throws IllegalArgumentException, InstantiationException, IllegalAccessException {
		return proxyFactory.createProxy(TestService.class, testService);
	}
}