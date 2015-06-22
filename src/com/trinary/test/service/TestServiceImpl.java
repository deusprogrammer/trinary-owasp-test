package com.trinary.test.service;

import javax.ejb.EJB;

import com.trinary.test.dao.UserDAO;
import com.trinary.test.entity.User;

public class TestServiceImpl implements TestService {
	@EJB UserDAO userDAO;

	@Override
	public User get(String username) {
		User user = userDAO.getByUsername(username);
		
		return user;
	}

	@Override
	public void create() {
		User user = new User();
		user.setUsername("deusprogrammer");
		user.setPassword("pa$$word");
		userDAO.save(user);
	}

	@Override
	public String someComplexFunction(User user) {
		System.out.println("I RAN");
		return "<html><h1>Hello " + user.getUsername() + "</h1></html>";
	}
}