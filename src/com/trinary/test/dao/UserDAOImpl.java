package com.trinary.test.dao;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.trinary.test.entity.User;

@Stateful
public class UserDAOImpl implements UserDAO {
	@PersistenceContext(name="default")
	protected EntityManager entityManager;

	@Override
	public User get(long id) {
		return entityManager.getReference(User.class, id);
	}

	@Override
	public User getByUsername(String username) {
		Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = '" + username + "'");
		@SuppressWarnings("unchecked")
		List<User> results = query.getResultList();
		
		if (results.size() == 0) {
			return null;
		}
		
		return results.get(0);
	}

	@Override
	public void save(User user) {
		entityManager.persist(user);
	}
}