package com.trinary.test.service;

import javax.ejb.Local;

import com.trinary.security.owasp.annotations.OWASPSecured;
import com.trinary.test.entity.User;

@Local
public interface TestService {
	public User get(String username);
	public void create();
	
	@OWASPSecured
	public String someComplexFunction(User user);
}
