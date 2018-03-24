package com.uniovi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByType(String type);

}
