package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Publication;
import com.uniovi.entities.User;
import com.uniovi.repositories.PublicationRepository;

@Service
public class PublicationsService {
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	public List<Publication> getAllPublications(){
		return publicationRepository.findAll();
	}
	
	public List<Publication> getPublicationsByUser(User user){
		return publicationRepository.findPublicationByOwner(user);		
	}
	

}
