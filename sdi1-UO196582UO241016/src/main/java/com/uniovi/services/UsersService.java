package com.uniovi.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostConstruct
	public void init() {
	}

	public Page<User> getUsers(User user, Pageable pageable) {
		Page<User> users = usersRepository.findAllUsersExceptLoggedInUser(user,
				pageable);
		return users;
	}

	public User getUser(Long id) {
		return usersRepository.findOne(id);
	}

	public void addUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		if (user.getRole() == null)
			user.setRole(rolesService.getRoleByType("ROLE_REGISTERED"));

		usersRepository.save(user);
	}

	public void deleteUser(Long id) {
		usersRepository.delete(id);
	}

	public User getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}

	public Page<User> searchUserByNameAndEmail(String searchText,
			Pageable pageable) {
		return usersRepository.searchByNameAndEmail("%" + searchText + "%",
				pageable);
	}

	public List<User> searchNotFriendsNorRequestedUsers(User user) {
		return usersRepository.searchNotFriendsNorRequestedUsers(user);
	}

	public Page<User> getAllUsers(Pageable pageable) {
		return usersRepository.findAll(pageable);
	}

}
