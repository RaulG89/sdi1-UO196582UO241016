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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.FriendRequest;
import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;
import com.uniovi.services.FriendRequestService;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UsersController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private FriendRequestService friendRequestService;

	@Autowired
	FriendshipService friendshipService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;

	@RequestMapping(value = "/login")
	public String login(Model model) {
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
	public String setUser(@ModelAttribute @Validated User user, BindingResult result, Model model) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}
		usersService.addUser(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		return "redirect:home";
	}

	@RequestMapping("/user/list")
	public String getListado(Model model, Pageable pageable, Principal principal,
			@RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		if (searchText != null && !searchText.isEmpty())
			users = usersService.searchUserByNameAndEmail(searchText, pageable);
		else
			users = usersService.getUsers(pageable);
		List<User> usersNotFriends = usersService.searchNotFriendsNorRequestedUsers(loggedInUser);
		model.addAttribute("usersNotFriends", usersNotFriends);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("usersList", users.getContent());
		model.addAttribute("page", users);
		return "user/list";
	}

	@RequestMapping("/user/friendRequests")
	public String getFriendRequests(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<FriendRequest> friendRequests = new PageImpl<FriendRequest>(new LinkedList<FriendRequest>());
		friendRequests = friendRequestService.getIncomingFriendRequestsByUser(pageable, loggedInUser);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("friendRequests", friendRequests.getContent());
		model.addAttribute("page", friendRequests);
		return "user/friendrequests";
	}

	@RequestMapping("/user/acceptRequest/{id}")
	public String acceptRequest(@PathVariable Long id, Model model, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		User requestingUser = usersService.getUser(id);
		friendshipService.addFriendship(requestingUser, loggedInUser);
		friendRequestService.deleteFriendRequest(requestingUser, loggedInUser);
		return "redirect:/user/friendRequests";
	}
	
	@RequestMapping("/user/friends")
	public String showFriends(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<Friendship> friends = new PageImpl<Friendship>(new LinkedList<Friendship>());
		// TODO La movida es que no se sabe cual
		friends = friendshipService.findByUser(pageable, loggedInUser);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("friends", friends.getContent());
		model.addAttribute("page", friends);
		return "user/friends";
	}

}
