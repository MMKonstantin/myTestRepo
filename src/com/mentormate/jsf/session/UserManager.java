package com.mentormate.jsf.session;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import com.mentormate.jsf.entities.User;

@Named
@SessionScoped
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private User user; // object of type User Entity

	// Getters and Setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Methods
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index.xhtml?faces-redirect=true";
	}
} // end class