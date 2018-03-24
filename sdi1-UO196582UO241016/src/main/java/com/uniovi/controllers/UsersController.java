package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uniovi.entities.User;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.LoggerService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UsersController {

	@Autowired
	private UsersService usersService;

	@Autowired
	FriendshipService friendshipService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;

	private LoggerService logger = new LoggerService(this);

	@RequestMapping(value = "/login")
	public String login(
			@RequestParam(value = "error", required = false) String error,
			Model model) {
		if (error != null)
			model.addAttribute("error", true);
		return "login";
	}

	@RequestMapping(value = "/home")
	public String home(Model model) {
		return "home";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String setUser(@ModelAttribute @Validated User user,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}
		usersService.addUser(user);
		logger.infoLog(
				"New user was created with Email: " + user.getEmail() + ".");
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		redirectAttributes.addFlashAttribute("success", true);
		logger.infoLog("The user with email: " + user.getEmail()
				+ " has accessed the system.");
		return "redirect:home";
	}

	@RequestMapping("/user/list")
	public String getListado(Model model, Pageable pageable,
			Principal principal,
			@RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		if (searchText != null && !searchText.isEmpty())
			users = usersService.searchUserByNameAndEmail(searchText, pageable);
		else
			users = usersService.getUsers(loggedInUser, pageable);
		List<User> usersNotFriends = usersService
				.searchNotFriendsNorRequestedUsers(loggedInUser);
		model.addAttribute("usersNotFriends", usersNotFriends);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("usersList", users.getContent());
		model.addAttribute("page", users);
		model.addAttribute("listAction", true);
		logger.infoLog("The user with email: " + loggedInUser.getEmail()
				+ " has listed the users of the system.");
		return "user/list";
	}

	@RequestMapping("/user/list/update")
	public String updateList(Model model, Pageable pageable,
			Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		users = usersService.getUsers(loggedInUser, pageable);
		List<User> usersNotFriends = usersService
				.searchNotFriendsNorRequestedUsers(loggedInUser);
		model.addAttribute("usersNotFriends", usersNotFriends);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("usersList", users.getContent());
		return "user/list :: tableUsers";
	}

}
