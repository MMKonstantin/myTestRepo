package com.mentormate.jsf.services;

import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import com.mentormate.jsf.dao.UserDao;
import com.mentormate.jsf.entities.User;

@Default
@Named
@Stateless
public class UserService {

	@Inject
	protected UserDao userDao;

	// Methods
	public List<User> getAllUsers() {
		return userDao.getAll(User.class);
	}

	public User getByEmail(String email) {
		return userDao.getByEmail(email);
	}

	public void save(User newUser) {
		userDao.save(newUser);
	}
} // end UserServices class