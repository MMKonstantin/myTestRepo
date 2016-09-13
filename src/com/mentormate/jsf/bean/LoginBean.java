package com.mentormate.jsf.bean;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.mentormate.jsf.entities.LoginInfo;
import com.mentormate.jsf.entities.User;
import com.mentormate.jsf.services.LoginInfoService;
import com.mentormate.jsf.services.UserService;
import com.mentormate.jsf.session.UserManager;

@Named
@ViewScoped
public class LoginBean implements Serializable {

	@Inject
	private UserManager userManager;

	@Inject
	private UserService userService;

	@Inject
	private LoginInfoService loginInfoService;

	private static final long serialVersionUID = 1L;
	private String userEmail;
	private String password;

	@PostConstruct
	public void init() { // Same as constructor
		System.out.println("Init");
	}

	public String login() {
		
		String returnStatus = "";
		User currentUser = userService.getByEmail(userEmail);
		LoginInfo currentRecord = new LoginInfo();
		currentRecord.setUser(currentUser);
		currentRecord.setLoginDate(new Date()); // set current date

		if (currentUser != null && password.equals(currentUser.getPassword())) {
			userManager.setUser(currentUser);
			currentRecord.setLoginSuccess(true);
			returnStatus = "success";
		} else {
			currentRecord.setLoginSuccess(false);
			returnStatus = "failure";
		}
		loginInfoService.save(currentRecord);
		return returnStatus;

	} // end login()

	// Getters and Setters
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void reset() {
		userEmail = null;
		password = null;
	}
} // end class