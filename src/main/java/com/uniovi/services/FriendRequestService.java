package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.FriendRequest;
import com.uniovi.repositories.FriendRequestRepository;

@Service
public class FriendRequestService {
	
	@Autowired
	FriendRequestRepository friendRequestRepository;
	
	public void addFriendRequest(FriendRequest friendRequest) {
		
		friendRequestRepository.save(friendRequest);
	}

}
