package com.mentormate.jsf.bean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.mentormate.jsf.entities.LoginInfo;
import com.mentormate.jsf.services.LoginInfoService;

@Named
@ViewScoped
public class LoginInfoBean implements Serializable {

	@Inject
	private LoginInfoService loginInfoService;

	private static final long serialVersionUID = 1L;

	private List<LoginInfo> logins;

	@PostConstruct
	public void init() { // Same as constructor
		System.out.println("Init");
		logins = loginInfoService.getAllUserLogs();
	}

	// Getters and Setters
	public List<LoginInfo> getLogins() {
		return logins;
	}

	public void setLogins(List<LoginInfo> logins) {
		this.logins = logins;
	}
} // end class