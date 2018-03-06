package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.FriendRequest;
import com.uniovi.entities.User;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long>{
	
	Page<FriendRequest> findByRequestedUser(Pageable pageable, User requestedUser);
	
	Page<FriendRequest> findByRequestingUser(Pageable pageable, User requestingUser);

}
