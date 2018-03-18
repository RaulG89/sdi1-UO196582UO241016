package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uniovi.entities.FriendRequest;
import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;
import com.uniovi.services.FriendRequestService;
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
	private FriendRequestService friendRequestService;

	@Autowired
	FriendshipService friendshipService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;
	
	private LoggerService logger = new LoggerService(this);

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
	public String setUser(@ModelAttribute @Validated User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}
		usersService.addUser(user);
		logger.infoLog("New user was created with Email: " + user.getEmail() + ".");
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		redirectAttributes.addFlashAttribute("success", true);
		logger.infoLog("The user with email: " + user.getEmail() + " has accessed the system.");
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
		model.addAttribute("listAction", true);
		logger.infoLog("The user with email: " 
				+ loggedInUser.getEmail() 
				+ " has listed the users of the system.");
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
		logger.infoLog("The user with email: " 
				+ loggedInUser.getEmail() 
				+ " has listed their friend requests.");
		return "user/friendrequests";
	}

	@RequestMapping("/user/acceptRequest/{id}")
	public String acceptRequest(@PathVariable Long id, Model model, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		User requestingUser = usersService.getUser(id);
		friendshipService.addFriendship(requestingUser, loggedInUser);
		friendRequestService.deleteFriendRequest(requestingUser, loggedInUser);
		logger.infoLog("The user with email: " 
				+ loggedInUser.getEmail() 
				+ " has accepted the friend request of the user with email: " 
				+ requestingUser.getEmail() + ".");
		return "redirect:/user/friendRequests";
	}
	
	@RequestMapping("/user/friends")
	public String showFriends(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<Friendship> friends = new PageImpl<Friendship>(new LinkedList<Friendship>());
		friends = friendshipService.findByUser(pageable, loggedInUser);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("friends", friends.getContent());
		model.addAttribute("page", friends);
		logger.infoLog("The user with email: " 
				+ loggedInUser.getEmail() 
				+ " has listed their friend list.");
		return "user/friends";
	}
	
	@RequestMapping("/user/sendFriendRequest/{id}")
	public String sendFriendRequest(Model model, @PathVariable Long id, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		User requestedUser = usersService.getUser(id);
		friendRequestService.addFriendRequest(loggedInUser,requestedUser);
		logger.infoLog("The user with email: " 
				+ loggedInUser.getEmail() 
				+ " has send friend request to user with email: " 
				+ requestedUser.getEmail() + ".");
		return "redirect:/user/list";
	}
	
	@RequestMapping("/user/list/update")
	public String updateList(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User loggedInUser = usersService.getUserByEmail(email);
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		users = usersService.getUsers(pageable);
		List<User> usersNotFriends = usersService.searchNotFriendsNorRequestedUsers(loggedInUser);
		model.addAttribute("usersNotFriends", usersNotFriends);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("usersList", users.getContent());
		return "user/list :: tableUsers";
	}

}
