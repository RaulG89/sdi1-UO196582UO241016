package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.entities.Publication;
import com.uniovi.entities.User;
import com.uniovi.services.PublicationsService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.AddPublicationFormValidator;

@Controller
public class PublicationsController {

	
	@Autowired
	private PublicationsService publicationService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private AddPublicationFormValidator addPublicationFormValidator;
	
	@RequestMapping(value = "/publication/add")
	public String createPublication(Model model) {
		model.addAttribute("publication", new Publication());
		return "publication/add";
	}
	
	@RequestMapping(value = "/publication/add", method = RequestMethod.POST)
	public String savePublication(@ModelAttribute @Validated Publication publication, Model model, BindingResult result, Principal principal) {
		addPublicationFormValidator.validate(publication, result);
		if (result.hasErrors()) {
			return "/publication/add";
		}
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		publication.setOwner(loggedInUser);
		publication.setCreationDate(publicationService.getDate());
		publicationService.addPublication(publication);
		return "redirect:/publication/list";
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
