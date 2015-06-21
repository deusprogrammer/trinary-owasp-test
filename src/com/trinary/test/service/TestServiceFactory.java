package com.trinary.test.service;

import java.lang.reflect.Proxy;

import com.trinary.security.owasp.proxy.OWASPMethodValidatorProxy;

public class TestServiceFactory {
	Class<?>[] interfaces = {TestService.class};
	
	public TestService createESignService() throws IllegalArgumentException, InstantiationException, IllegalAccessException {
		return (TestService)Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
				interfaces,
				new OWASPMethodValidatorProxy<TestService>(TestServiceImpl.class));
	}
}