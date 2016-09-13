package com.mentormate.jsf.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import com.mentormate.jsf.entities.User;

@Named
@Default
@Stateless
public class UserDao extends BaseDao<User> {

	public User getByEmail(String email) {
		List<User> users = findByCriteria(User.class, "email", email);
		if (users.size() > 0) {
			return users.get(0);
		}
		return null;
	} // end getByEmail()
} // end UserDao class