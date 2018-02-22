package com.uniovi.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(FriendKey.class)
public class Friend {
	
	@Id
	@ManyToOne
	private User user1;
	@Id
	@ManyToOne
	private User user2;
	
	public Friend() {
		// TODO Auto-generated constructor stub
	}

	public Friend(User user1, User user2) {
		super();
		this.user1 = user1;
		this.user2 = user2;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}
	
	

}
