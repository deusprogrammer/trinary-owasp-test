package com.trinary.test.service;

import javax.ejb.Local;

@Local
public interface TestService {
	public void get(String username);
	public void create();
}
