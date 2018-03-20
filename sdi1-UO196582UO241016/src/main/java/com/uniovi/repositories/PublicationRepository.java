package com.uniovi.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.uniovi.entities.Publication;
import com.uniovi.entities.User;

public interface PublicationRepository extends CrudRepository<Publication, Long> {

	List<Publication> findAll();
	
	List<Publication> findPublicationByOwner(User user);
	
}
