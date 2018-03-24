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

import com.uniovi.entities.FriendRequest;
import com.uniovi.entities.User;
import com.uniovi.services.FriendRequestService;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.LoggerService;
import com.uniovi.services.UsersService;

@Controller
public class FriendRequestController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private FriendRequestService friendRequestService;

	@Autowired
	private FriendshipService friendshipService;

	private LoggerService logger = new LoggerService(this);

	@RequestMapping("/user/sendFriendRequest/{id}")
	public String sendFriendRequest(Model model, @PathVariable Long id,
			Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		User requestedUser = usersService.getUser(id);
		friendRequestService.addFriendRequest(loggedInUser, requestedUser);
		logger.infoLog("The user with email: " + loggedInUser.getEmail()
				+ " has send friend request to user with email: "
				+ requestedUser.getEmail() + ".");
		return "redirect:/user/list";
	}

	@RequestMapping("/user/acceptRequest/{id}")
	public String acceptRequest(@PathVariable Long id, Model model,
			Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		User requestingUser = usersService.getUser(id);
		friendshipService.addFriendship(requestingUser, loggedInUser);
		friendRequestService.deleteFriendRequest(requestingUser, loggedInUser);
		logger.infoLog("The user with email: " + loggedInUser.getEmail()
				+ " has accepted the friend request of the user with email: "
				+ requestingUser.getEmail() + ".");
		return "redirect:/user/friendRequests";
	}

	@RequestMapping("/user/friendRequests")
	public String getFriendRequests(Model model, Pageable pageable,
			Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<FriendRequest> friendRequests = new PageImpl<FriendRequest>(
				new LinkedList<FriendRequest>());
		friendRequests = friendRequestService
				.getIncomingFriendRequestsByUser(pageable, loggedInUser);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("friendRequests", friendRequests.getContent());
		model.addAttribute("page", friendRequests);
		logger.infoLog("The user with email: " + loggedInUser.getEmail()
				+ " has listed their friend requests.");
		return "user/friendrequests";
	}
}
