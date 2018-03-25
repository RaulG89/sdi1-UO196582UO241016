package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

	@Query("select f from Friendship f where f.user=?1 or f.friend=?1")
	Page<Friendship> findByUser(Pageable pageable, User loggedInUser);

	
	/**Query that obtains friendships from database if a user 1 is friend of 
	 * user2.
	 * @param user1 
	 * @param user2
	 * @return The Friendship or null.
	 */
	@Query("select f from Friendship f where (f.user=?1 AND f.friend=?2) "
			+ "OR (f.user=?2 AND f.friend=?1)")
	Friendship areFriends(User user1, User user2);

}
