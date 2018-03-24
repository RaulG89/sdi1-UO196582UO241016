package com.uniovi.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Publication;
import com.uniovi.entities.Role;
import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService roleService;

	@Autowired
	private FriendRequestService friendRequestService;

	@Autowired
	private FriendshipService friendshipService;

	@Autowired
	private PublicationsService publicationService;

	@PostConstruct
	public void init() {
		Role role1 = new Role("ROLE_REGISTERED");
		Role role2 = new Role("ROLE_ADMIN");
		roleService.addRole(role1);
		roleService.addRole(role2);
		User user1 = new User("Raúl", "Gómez", "rulas@gmail.com", "123456");
		user1.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user1);
		User user2 = new User("Marcos", "Ruiz", "yeyas@gmail.com", "123456");
		user2.setRole(roleService.getRoleByType("ROLE_ADMIN"));
		usersService.addUser(user2);
		User user3 = new User("Nacho", "Escribano", "nachas@gmail.com",
				"123456");
		user3.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user3);
		User user4 = new User("Pablo", "Rubio", "pablas@gmail.com", "123456");
		user4.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user4);
		User user5 = new User("Martin", "Mozalbete", "pacharanman@gmail.com",
				"123456");
		user5.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user5);
		User user6 = new User("Moisés", "Chacón", "moisas@gmail.com", "123456");
		user6.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user6);
		User user7 = new User("Borja", "Muiña", "termometro@gmail.com",
				"123456");
		user7.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user7);
		User user8 = new User("Aurora", "López", "pocito@gmail.com", "123456");
		user8.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user8);
		User user9 = new User("Anahí", "Fulgueiras", "nanani@gmail.com",
				"123456");
		user9.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user9);
		User user10 = new User("Ana", "Ponfe", "anaponfe@gmail.com", "123456");
		user10.setRole(roleService.getRoleByType("ROLE_REGISTERED"));
		usersService.addUser(user10);

		// FRIEND REQUESTS
		friendRequestService.addFriendRequest(user3, user1);
		friendRequestService.addFriendRequest(user1, user6);
		friendRequestService.addFriendRequest(user4, user1);

		publicationService
				.addPublication(new Publication(user10, "Prueba Publicación",
						"Esta es una publicación de prueba", null));

		// FRIENDSHIPS
		friendshipService.addFriendship(user1, user10);
		// friendshipService.addFriendship(user4, user1);

	}
}
