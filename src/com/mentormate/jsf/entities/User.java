package com.mentormate.jsf.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(catalog = "JSF_DB", schema = "dbo", name = "Users")
public class User extends BaseEntity implements IEntity {

	// Fields
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "User email is required")
	@Pattern(regexp = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", message = "Email address is invalid")
	@Column(name = "email")
	private String email;

	@NotEmpty(message = "Password field is required")
	@Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
	@Column(name = "passwords")
	private String password;

	@NotEmpty(message = "First Name is required")
	@Column(name = "first_name")
	private String firstName;

	@NotEmpty(message = "Last Name is required")
	@Column(name = "last_name")
	private String lastName;

	@NotEmpty(message = "Gender field is required")
	@Column(name = "gender")
	private String gender;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Past
	@Column(name = "birthdate")
	private Date birthdate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
	private List<LoginInfo> logins = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "UserRoles",
				joinColumns = @JoinColumn(name = "userID", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "groupID", referencedColumnName = "id"))
	private List<Groups> groups = new ArrayList<>();

	public User() {
		// default constructor
	}

	// Getters and Setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public List<LoginInfo> getLogins() {
		return logins;
	}

	public void setLogins(List<LoginInfo> logins) {
		this.logins = logins;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}
} // end entity class