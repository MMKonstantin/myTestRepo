package com.mentormate.jsf.bean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.mentormate.jsf.entities.User;
import com.mentormate.jsf.services.UserService;

@Named
@ViewScoped
public class UserInfoBean implements Serializable {

	@Inject
	private UserService userService;

	private static final long serialVersionUID = 1L;

	private List<User> userInfo;

	@PostConstruct
	public void init() { // Same as constructor
		System.out.println("Init");
		userInfo = userService.getAllUsers();
	}

	// Getters and Setters
	public List<User> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List<User> userInfo) {
		this.userInfo = userInfo;
	}
} // end class