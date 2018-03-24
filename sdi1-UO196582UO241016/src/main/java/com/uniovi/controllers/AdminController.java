package com.uniovi.controllers;

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

import com.uniovi.entities.User;
import com.uniovi.services.LoggerService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.AdminLoginFormValidator;

@Controller
public class AdminController {

	@Autowired
	private UsersService usersService;

	private LoggerService logger = new LoggerService(this);

	@Autowired
	private SecurityService securityService;

	@Autowired
	private AdminLoginFormValidator adminLoginFormValidator;

	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public String checkedAdminLogin(
			@ModelAttribute("user") @Validated User user, BindingResult result,
			Model model) {
		adminLoginFormValidator.validate(user, result);
		if (result.hasErrors()) {
			logger.infoLog("Invalid Log-in.");
			return "adminlogin";
		}
		securityService.autoLogin(user.getEmail(), user.getPassword());
		logger.infoLog("The ADMIN user with email: " + user.getEmail()
				+ " has accessed the system.");
		return "redirect:/admin/users";
	}

	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public String adminLogin(Model model) {
		model.addAttribute("user", new User());
		return "adminlogin";
	}

	@RequestMapping("/admin/users")
	public String listUsers(Model model, Pageable pageable) {
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		users = usersService.getAllUsers(pageable);
		model.addAttribute("usersList", users.getContent());
		model.addAttribute("page", users);
		return "admin/users";
	}

	@RequestMapping("/admin/users/update")
	public String updateList(Model model, Pageable pageable) {
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		users = usersService.getAllUsers(pageable);
		model.addAttribute("usersList", users.getContent());
		return "admin/users :: tableUsers";
	}

	@RequestMapping("/admin/deleteuser/{id}")
	public String deleteUser(Model model, @PathVariable Long id) {
		usersService.deleteUser(id);
		logger.infoLog("Deleted user with id" + id);
		return "redirect:/admin/users";
	}

}
