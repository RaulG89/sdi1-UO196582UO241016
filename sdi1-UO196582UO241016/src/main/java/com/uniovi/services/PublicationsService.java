package com.uniovi.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Publication;
import com.uniovi.entities.User;
import com.uniovi.repositories.PublicationRepository;

@Service
public class PublicationsService {

	@Autowired
	private PublicationRepository publicationRepository;

	public List<Publication> getAllPublications() {
		return publicationRepository.findAll();
	}

	public Page<Publication> getPublicationsByUser(User user,
			Pageable pageable) {
		return publicationRepository.findPublicationByOwner(user, pageable);
	}

	public void addPublication(Publication publication) {
		publicationRepository.save(publication);
	}

	public Publication getPublicationById(Long id) {
		return publicationRepository.findOne(id);
	}

	public Date getDate() {
		return new Date(System.currentTimeMillis());
	}

}
