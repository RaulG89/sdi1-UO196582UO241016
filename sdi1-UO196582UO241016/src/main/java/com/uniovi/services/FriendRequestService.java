package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.FriendRequest;
import com.uniovi.entities.User;
import com.uniovi.repositories.FriendRequestRepository;

@Service
public class FriendRequestService {

	@Autowired
	FriendRequestRepository friendRequestRepository;

	public void addFriendRequest(User requestingUser, User requestedUser) {
		friendRequestRepository
				.save(new FriendRequest(requestingUser, requestedUser));
	}

	public void deleteFriendRequest(User requestingUser, User requestedUser) {
		FriendRequest friendRequest = friendRequestRepository
				.findByRequestingUserRequestedUser(requestingUser,
						requestedUser);
		friendRequestRepository.delete(friendRequest.getId());
	}

	public Page<FriendRequest> getIncomingFriendRequestsByUser(
			Pageable pageable, User loggedInUser) {
		return friendRequestRepository.findByRequestedUser(pageable,
				loggedInUser);
	}

}
