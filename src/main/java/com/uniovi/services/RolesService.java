package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Role;
import com.uniovi.repositories.RoleRepository;

@Service
public class RolesService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public Role getRoleByType(String type) {
		return roleRepository.findByType(type);
	}
	
	public void addRole(Role role) {
		roleRepository.save(role);
	}

}
