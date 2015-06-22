package com.trinary.test.service;

import javax.ejb.Local;

@Local
public interface TestServiceFactory {

	public abstract TestService createESignService()
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException;

}