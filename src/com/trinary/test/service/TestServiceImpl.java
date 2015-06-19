package com.trinary.test.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.trinary.test.dao.UserDAO;
import com.trinary.test.entity.User;

@Stateless(name="testService")
public class TestServiceImpl implements TestService {
	@EJB UserDAO userDAO;

	@Override
	public void get(String username) {
		User user = userDAO.getByUsername(username);
		
		if (user != null) {
			System.out.println("USER: " + user);
		}
	}

	@Override
	public void create() {
		User user = new User();
		user.setUsername("deusprogrammer");
		user.setPassword("pa$$word");
		userDAO.save(user);
	}
}