package com.uniovi.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TROLE")
public class Role {

	@Id
	@GeneratedValue
	private Long roleId;
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String type;
	
	@OneToMany(mappedBy="role")
	private Set<User> users;
	
	Role(){}
	
	public Role(String type){
		this.type = type;
	}
}
