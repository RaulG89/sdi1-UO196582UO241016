package com.uniovi.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Friendship {
	
	@Id
	@GeneratedValue
	long id;
	
	@ManyToOne
	private User user;

	@ManyToOne
	private User friend;
	
	public Friendship() {
		// TODO Auto-generated constructor stub
	}

	public Friendship(User user, User friend) {
		super();
		this.user = user;
		this.friend = friend;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}



	
	

}
