package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import com.uniovi.entities.Publication;
import com.uniovi.entities.User;

public interface PublicationRepository
		extends CrudRepository<Publication, Long> {

	List<Publication> findAll();

	Page<Publication> findPublicationByOwner(User user, Pageable pageable);

}
