package com.trinary.test.dao;

import javax.ejb.Local;

import com.trinary.test.entity.User;

@Local
public interface UserDAO {
	public User get(long id);
	public User getByUsername(String username);
	public void save(User user);
}