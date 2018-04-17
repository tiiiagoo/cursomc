package com.tgs.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tgs.cursomc.dto.EmailDTO;
import com.tgs.cursomc.security.JWTUtil;
import com.tgs.cursomc.security.UserSpringSecurity;
import com.tgs.cursomc.services.AuthService;
import com.tgs.cursomc.services.UserService;

import javassist.tools.rmi.ObjectNotFoundException;



@RestController
@RequestMapping(value="/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		UserSpringSecurity user = UserService.getAuthenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer "+token);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(path="/forgot")
	public ResponseEntity<Void> forgotPassword(@Valid @RequestBody EmailDTO emailDto) throws ObjectNotFoundException{
		authService.sendNewPassword(emailDto.getEmail());
		return ResponseEntity.noContent().build();
	}
}
