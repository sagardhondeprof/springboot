package com.springboot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.configuration.JWTBlacklist;
import com.springboot.pojo.AuthenticatePOJO;
import com.springboot.pojo.AuthenticationResponsePOJO;
import com.springboot.service.AuthenticateService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("authenticate")
@Slf4j
public class AuthenticationController {

	@Autowired
	private AuthenticateService authenticateService;

	@PostMapping
	public ResponseEntity<AuthenticationResponsePOJO> authenticateUser(@RequestBody AuthenticatePOJO request)
			throws Exception {
		log.info("Inside Authenticate User");
		return new ResponseEntity<>(authenticateService.authenticateUser(request), HttpStatus.OK);
	}
	public String userName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}
//	@PutMapping("/logout")
//	public ResponseEntity<String> logoutUser(@RequestParam String token)
//			throws Exception {
//		String username =userName();
//		JWTBlacklist.balcklistedTokensMap.put(username, token);
//		return new ResponseEntity<>("logged out", HttpStatus.OK);
//	}
	@GetMapping("logout")
	public ResponseEntity<Object> userLogout(HttpServletRequest request) {
		log.debug("inside logout");
		authenticateService.blacklistToken(request);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
