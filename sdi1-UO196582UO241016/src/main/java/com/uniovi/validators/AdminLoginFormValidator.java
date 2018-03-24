package com.uniovi.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.entities.Role;
import com.uniovi.entities.User;
import com.uniovi.services.RolesService;
import com.uniovi.services.UsersService;

@Component
public class AdminLoginFormValidator implements Validator {

	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
				"Error.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
				"Error.empty");
		User aux = usersService.getUserByEmail(user.getEmail());
		Role role2 = rolesService.getRoleByType("ROLE_ADMIN");
		if (aux == null)
			errors.rejectValue("email", "Error.adminlogin.invalidcredentials");
		else {
			Role role1 = aux.getRole();
			if (role1 != role2)
				errors.rejectValue("email",
						"Error.adminlogin.email.privileges");
		}
	}

}
