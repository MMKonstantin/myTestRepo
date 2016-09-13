package com.mentormate.jsf.bean;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.mentormate.jsf.entities.User;
import com.mentormate.jsf.services.UserService;
import com.mentormate.jsf.session.UserManager;

@Named
@ViewScoped
public class RegistrationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String confirmPassword;
	private String gender;
	private Date birthDate;
	private Boolean allTerms = false;

	@Inject
	private UserService userService;

	@Inject
	private UserManager userManager;

	@PostConstruct
	public void init() { // Same as constructor
		System.out.println("Init");
	}

	public String login() {

		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword(confirmPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setGender(gender);
		newUser.setBirthdate(birthDate);
		userService.save(newUser);

		User currentUser = userService.getByEmail(email);
		userManager.setUser(currentUser);
		return "success";
	} // end login()

	public void reset() {
		email = null;
		firstName = null;
		lastName = null;
		password = null;
		confirmPassword = null;
		gender = null;
		birthDate = null;
		allTerms = false;
	} // end reset()

	// Getters and Setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAllTerms() {
		return allTerms;
	}

	public void setAllTerms(Boolean allTerms) {
		this.allTerms = allTerms;
	}
} // end class