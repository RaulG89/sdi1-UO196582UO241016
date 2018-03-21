package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniovi.entities.Publication;
import com.uniovi.entities.User;
import com.uniovi.services.PublicationsService;
import com.uniovi.services.UsersService;

@Controller
public class PublicationsController {

	
	@Autowired
	private PublicationsService publicationService;
	
	@Autowired
	private UsersService usersService;
	
	
	@RequestMapping("/publication/createform")
	public String createPublication(Model model, Principal principal) {
		return null;
		
	}
	
	@RequestMapping("/publication/list")
	public String listPublications(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<Publication> publications = new PageImpl<Publication>(new LinkedList<Publication>());
		publications = publicationService.getPublicationsByUser(loggedInUser, pageable);
		model.addAttribute("publicationsList", publications.getContent());
		model.addAttribute("page", publications);
		return "publication/list";
	}
	
	@RequestMapping("/publication/details/{id}")
	public String getDetails(Model model, @PathVariable Long id){
		Publication publication = publicationService.getPublicationById(id);
		model.addAttribute("publication", publication);
		return "publication/details";
	}
	
	
}
