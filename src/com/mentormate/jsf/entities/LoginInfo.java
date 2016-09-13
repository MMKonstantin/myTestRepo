package com.mentormate.jsf.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(catalog = "JSF_DB", schema = "dbo", name = "LoginInfo")

public class LoginInfo extends BaseEntity implements IEntity {

	// Fields
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_date")
	private Date loginDate;

	@Column(name = "login_success")
	private boolean loginSuccess;

	@JoinColumn(name = "userID", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public LoginInfo() {
		// default constructor
	}

	// Getters and Setters
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
} // end class