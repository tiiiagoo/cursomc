package com.tgs.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.tgs.cursomc.security.UserSpringSecurity;

public class UserService {
	
	public static UserSpringSecurity getAuthenticated() {
		try {
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch (Exception e) {
			return null;
		}
	}

}
