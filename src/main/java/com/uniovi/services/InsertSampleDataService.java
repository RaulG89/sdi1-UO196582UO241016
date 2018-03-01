package com.uniovi.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@PostConstruct
	public void init() {
		User user1 = new User("Raúl","Gómez","rulas@gmail.com","123456");
		usersService.addUser(user1);
		User user2 = new User("Marcos","Ruiz","yeyas@gmail.com","123456");
		usersService.addUser(user2);
		User user3 = new User("Nacho","Escribano","nachas@gmail.com","123456");
		usersService.addUser(user3);
		User user4 = new User("Pablo","Rubio","pablas@gmail.com","123456");
		usersService.addUser(user4);
		User user5 = new User("Martin","Mozalbete","pacharanman@gmail.com","123456");
		usersService.addUser(user5);
		User user6 = new User("Moisés","Chacón","moisas@gmail.com","123456");
		usersService.addUser(user6);
		User user7 = new User("Borja","Muiña","termometro@gmail.com","123456");
		usersService.addUser(user7);
		User user8 = new User("Aurora","López","pocito@gmail.com","123456");
		usersService.addUser(user8);
		User user9 = new User("Anahí","Fulgueiras","nanani@gmail.com","123456");
		usersService.addUser(user9);
		User user10 = new User("Ana","Ponfe","anaponfe@gmail.com","123456");
		usersService.addUser(user10);
		
	}
}
