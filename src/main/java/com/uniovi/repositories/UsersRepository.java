package com.uniovi.repositories;

<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
import java.util.List;

import org.springframework.data.jpa.repository.Query;
>>>>>>> Search_Users
import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.User;

public interface UsersRepository extends CrudRepository<User, Long>{
	
	User findByEmail(String email);
	
<<<<<<< HEAD
	Page<User> findAll(Pageable pageable);
=======
	@Query("SELECT u "
			+ "FROM User u "
			+ "WHERE (LOWER(u.email) LIKE LOWER(?1) "
			+ "OR LOWER(u.name) LIKE LOWER(?1))")
	List<User> searchByNameAndEmail(String searchText);
>>>>>>> Search_Users

}
