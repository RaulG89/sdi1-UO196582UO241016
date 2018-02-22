package com.uniovi.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="TUSER")
public class User {
	
	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private String name;
	private String lastName;
	@NotNull
	@Column(unique = true)
	private String email;
	@NotNull
	private String password;
	@Transient
	private String passwordConfirm;
	
	@OneToMany
	private Set<Friend> friends;
	@OneToMany
	private Set<FriendRequest> friendRequests;
	
	public User(String name, String lastName, String email, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public User() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Set<Friend> getFriends() {
		return friends;
	}

	public void setFriends(Set<Friend> friends) {
		this.friends = friends;
	}

	public Set<FriendRequest> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(Set<FriendRequest> friendRequests) {
		this.friendRequests = friendRequests;
	}

}
