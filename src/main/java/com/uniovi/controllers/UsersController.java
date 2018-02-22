package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;

@Controller
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value="/login")
	public String login(Model model) {
		return "login";
	}
	
	@RequestMapping(value="/home")
	public String home(Model model) {
		return "home";
	}

}
