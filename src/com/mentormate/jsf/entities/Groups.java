package com.mentormate.jsf.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(catalog = "JSF_DB", schema = "dbo", name = "Groups")
public class Groups extends BaseEntity implements IEntity{

	// Fields
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Group name is required field")
	@Column(name = "name")
	private String groupName;
	
	@NotEmpty(message = "Group description is required field")
	@Column(name = "descriptions")
	private String groupDescription;
	
	@ManyToMany(mappedBy="groups", cascade = CascadeType.ALL)
	private List<User> users;
	
	public Groups() {
		// default constructor
	}

	// Getters and Setters
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}	
} // end class