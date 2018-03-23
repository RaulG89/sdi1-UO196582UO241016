package com.uniovi.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	
	private String name;
	
	private String lastName;
	
	@NotNull
	@Column(unique = true)
	private String email;
	
	@NotNull
	private String password;
	
	@Transient
	private String passwordConfirm;
	
	@OneToMany(mappedBy = "friend", cascade=CascadeType.ALL)
	private Set<Friendship> userFriends;

	@OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
	private Set<Friendship> userIsFriend;

	@OneToMany(mappedBy= "requestingUser", cascade=CascadeType.ALL)
	private Set<FriendRequest> friendRequestsSent;
	
	@OneToMany(mappedBy= "requestedUser", cascade=CascadeType.ALL)
	private Set<FriendRequest> friendRequestsReceived;
	
	@OneToMany(mappedBy= "owner", cascade=CascadeType.ALL)
	private Set<Publication> publications;
	
	@ManyToOne
	private Role role;
	
	public User(String name, String lastName, String email, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public User() {}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

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

	public Set<Friendship> getUserFriends() {
		return userFriends;
	}	
	
	public void setUserFriends(Set<Friendship> userFriends) {
		this.userFriends = userFriends;
	}

	public Set<Friendship> getUserIsFriend() {
		return userIsFriend;
	}

	public void setUserIsFriend(Set<Friendship> userIsFriend) {
		this.userIsFriend = userIsFriend;
	}

	public Set<FriendRequest> getFriendRequestsSent() {
		return friendRequestsSent;
	}

	public void setFriendRequestsSent(Set<FriendRequest> friendRequestsSent) {
		this.friendRequestsSent = friendRequestsSent;
	}

	public Set<FriendRequest> getFriendRequestsReceived() {
		return friendRequestsReceived;
	}

	public void setFriendRequestsReceived(Set<FriendRequest> friendRequestsReceived) {
		this.friendRequestsReceived = friendRequestsReceived;
	}

	public Set<Publication> getPublications() {
		return publications;
	}

	public void setPublications(Set<Publication> publications) {
		this.publications = publications;
	}

	

	
}
