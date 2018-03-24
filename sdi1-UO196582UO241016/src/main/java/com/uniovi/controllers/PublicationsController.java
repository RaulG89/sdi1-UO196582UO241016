package com.uniovi.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uniovi.entities.Publication;
import com.uniovi.entities.User;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.LoggerService;
import com.uniovi.services.PublicationsService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.AddPublicationFormValidator;

@Controller
public class PublicationsController {

	@Autowired
	private PublicationsService publicationService;

	@Autowired
	private FriendshipService friendshipService;

	@Autowired
	private UsersService usersService;

	private LoggerService logger = new LoggerService(this);

	@Autowired
	private AddPublicationFormValidator addPublicationFormValidator;

	@RequestMapping(value = "/publication/add")
	public String createPublication(Model model) {
		model.addAttribute("publication", new Publication());
		return "publication/add";
	}

	@RequestMapping(value = "/publication/add", method = RequestMethod.POST)
	public String savePublication(@ModelAttribute Publication publication,
			BindingResult result, Principal principal,
			@RequestParam(value = "image", required = false) MultipartFile img)
			throws IOException {
		// addPublicationFormValidator.validate(publication, result);
		// if (result.hasErrors()) {
		// return "/publication/add";
		// }
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		publication.setOwner(loggedInUser);
		if (!img.isEmpty())
			publication.setImage(img.getBytes());
		publication.setCreationDate(publicationService.getDate());
		publicationService.addPublication(publication);
		logger.infoLog("User with email \"" + loggedInUser.getEmail()
				+ "\" has created a new publication with title \""
				+ publication.getTitle() + "\"");
		return "redirect:/publication/list";
	}

	@RequestMapping("/publication/list")
	public String listPublications(Model model, Pageable pageable,
			Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<Publication> publications = new PageImpl<Publication>(
				new LinkedList<Publication>());
		publications = publicationService.getPublicationsByUser(loggedInUser,
				pageable);
		model.addAttribute("publicationsList", publications.getContent());
		model.addAttribute("page", publications);
		logger.infoLog("User with email \"" + loggedInUser.getEmail()
				+ "\" has listed all his own publications");
		return "publication/list";
	}

	@RequestMapping("/publication/details/{id}")
	public String getDetails(Model model, @PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String email = auth.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Publication publication = publicationService.getPublicationById(id);
		if (loggedInUser.equals(publication.getOwner()) || friendshipService
				.areFriends(loggedInUser, publication.getOwner()) != null) {
			model.addAttribute("publication", publication);
			logger.infoLog("User with email \"" + loggedInUser.getEmail()
					+ "\" has shown details of publication with title \""
					+ publication.getTitle() + "\"");
			return "publication/details";
		} else {
			logger.infoLog("User with email \"" + loggedInUser.getEmail()
					+ "\" has tried to see the details of a publication he did not own.");
			return "home";
		}
	}

	@RequestMapping("/publication/{id}")
	public String getPublicationsByUser(Model model, @PathVariable Long id,
			Pageable pageable) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String email = auth.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		User user = usersService.getUser(id);
		Page<Publication> publications = new PageImpl<Publication>(
				new LinkedList<Publication>());
		publications = publicationService.getPublicationsByUser(user, pageable);
		if (friendshipService.areFriends(loggedInUser, user) != null) {
			model.addAttribute("publicationsList", publications.getContent());
			model.addAttribute("page", publications);
			logger.infoLog("User with email \"" + loggedInUser.getEmail()
					+ "\" has listed all his friends publications");
			return "publication/friendpublicationslist";
		} else {
			logger.infoLog("User with email \"" + loggedInUser.getEmail()
					+ "\" has tried to see the details of a publication that neither"
					+ " he nor a friend was owner of.");
			return "home";
		}
	}

}
