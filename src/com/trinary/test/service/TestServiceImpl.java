package com.trinary.test.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.trinary.test.dao.UserDAO;
import com.trinary.test.entity.User;

@Stateless
public class TestServiceImpl implements TestService {
	@Inject UserDAO userDAO;

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
		if (user == null) {
			return "<html><h1>I don't know you...</h1></html>";
		}
		
		return "<html><h1>Hello " + user.getUsername() + "</h1></html>";
	}
}