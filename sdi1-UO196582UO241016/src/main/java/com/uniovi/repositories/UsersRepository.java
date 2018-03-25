package com.uniovi.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.uniovi.entities.User;

public interface UsersRepository extends CrudRepository<User, Long>{
	
	User findByEmail(String email);
	
	Page<User> findAll(Pageable pageable);

	@Query("SELECT u "
			+ "FROM User u "
			+ "WHERE (LOWER(u.email) LIKE LOWER(?1) "
			+ "OR LOWER(u.name) LIKE LOWER(?1))")
	Page<User> searchByNameAndEmail(String searchText, Pageable pageable);
	
	/**Method that returns the list of users that aren't friends and don't have
	 * friend requests to user in session.
	 * @param user
	 * @return
	 */
	@Query("SELECT u "
			+ "FROM User u "
			+ "WHERE u NOT IN ( "
			+ "SELECT f.friend "
			+ "FROM Friendship f "
			+ "WHERE f.user = ?1) "
			+ "AND u NOT IN ( "
			+ "SELECT f.user "
			+ "FROM Friendship f "
			+ "WHERE f.friend = ?1) "
			+ "AND u NOT IN ( "
			+ "SELECT r.requestedUser "
			+ "FROM FriendRequest r "
			+ "WHERE r.requestingUser = ?1) "
			+ "AND u NOT IN ( "
			+ "SELECT r.requestingUser "
			+ "FROM FriendRequest r "
			+ "WHERE r.requestedUser = ?1) "
			+ "AND u <> ?1")
	List<User> searchNotFriendsNorRequestedUsers(User user);
	
	@Query("SELECT u FROM User u WHERE u <> ?1")
	Page<User> findAllUsersExceptLoggedInUser(User user, Pageable pageable);

}
