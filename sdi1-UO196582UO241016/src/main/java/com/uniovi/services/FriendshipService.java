package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;
import com.uniovi.repositories.FriendshipRepository;

@Service
public class FriendshipService {
	
	@Autowired
	FriendshipRepository friendshipRepository;

	public void addFriendship(User requestingUser, User requestedUser) {
		Friendship friendship = new Friendship(requestingUser, requestedUser);
		friendshipRepository.save(friendship);
	}

	public Page<Friendship> findByUser(Pageable pageable, User loggedInUser) {
		return friendshipRepository.findByUser(pageable, loggedInUser);
	}
	
	public Friendship areFriends(User loggedInUser, User user) {
		return friendshipRepository.areFriends(loggedInUser, user);
	}

	

}
