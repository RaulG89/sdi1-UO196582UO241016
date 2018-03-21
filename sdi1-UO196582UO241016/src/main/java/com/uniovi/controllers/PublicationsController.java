package com.uniovi.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uniovi.services.PublicationsService;

@Controller
public class PublicationsController {

	
	@Autowired
	private PublicationsService publicationService;
	
	
	@RequestMapping("/publication/createform")
	public String createPublication(Model model, Principal principal) {
		return null;
		
	}
	
	
	
}
