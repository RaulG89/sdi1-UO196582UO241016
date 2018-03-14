package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;


public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

	// TODO Hacer la Query. Pendiente de acabar de hacer funcionar el modelo.
	Page<Friendship> findByUser1(Pageable pageable, User loggedInUser);

}
