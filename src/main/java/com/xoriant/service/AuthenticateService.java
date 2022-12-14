package com.xoriant.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xoriant.configuration.JWTBlacklist;
import com.xoriant.configuration.LoginSession;
import com.xoriant.entity.AccessMappingEntity;
import com.xoriant.entity.UserEntity;
import com.xoriant.pojo.AuthenticatePOJO;
import com.xoriant.pojo.AuthenticationResponsePOJO;
import com.xoriant.repository.UserRepository;
import com.xoriant.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticateService implements UserDetailsService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userDao;
	
	@Autowired
	private LoginSession loginSession;

	public AuthenticationResponsePOJO authenticateUser(AuthenticatePOJO request) throws Exception {
		String userName = request.getUserName();

		UserDetails userDetails = loadUserByUsername(userName);
		Authentication authentication = null;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userName, request.getUserPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException | DisabledException e) {
			log.error("Authentication failed " + e.toString());
			throw e;
		}
		String token = jwtUtil.generateToken(authentication);
		UserEntity authenticatedUser = userDao.findByUserName(userName);
		
		AuthenticationResponsePOJO response = new AuthenticationResponsePOJO(token,
				getRoles(authenticatedUser.getRoleMapping()));
		if(!loginSession.getUsernameMap().containsKey(userName)) {
		loginSession.getUsernameMap().put(userName, token);
		return response;
		}
		else {
			AuthenticationResponsePOJO response1 = new AuthenticationResponsePOJO("already loggged in",
					null);
			return response1;
		}
//		loginSession.getTokenMap().put(token, true);
//		String username = jwtUtil.getUsernameFromToken(token);
//		if(loginSession.getUsernameMap().containsKey(username)) {
//			JWTBlacklist.balcklistedTokensMap.put(loginSession.getUsernameMap().get(username), username);
//		}
//		loginSession.getUsernameMap().put(username, token);
		//return response;
	}

	public void authenticate(String userName, String userPassword) throws Exception {
		Authentication authentication = null;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException | DisabledException e) {
			throw e;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity user = userDao.findByUserName(username);
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
					new ArrayList<>());
		}
		/*
		 * for first time use if (username.equals("xorpay")) { return new
		 * org.springframework.security.core.userdetails.User("xorpay", "xorpay", new
		 * ArrayList<>()); }
		 */ else {
			log.info("User not found with username:{} " + username);
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	public String[] getRoles(Set<AccessMappingEntity> role) {
		return role.stream().map(mapper -> mapper.getRole().getRoleName()).toArray(String[]::new);
	}
	
	public void blacklistToken(HttpServletRequest request) {
		log.debug("start blacklistToken() method");
		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		if (null != requestTokenHeader) {
			jwtToken = requestTokenHeader.substring(11);
			try {
				if(!jwtUtil.isTokenExpired(jwtToken)) {
					username = jwtUtil.getUsernameFromToken(jwtToken);
			}
			}catch(ExpiredJwtException e) {
				//loginSession.getUsernameMap().entrySet().removeIf(entry -> jwtUtil.isTokenExpired(entry.getValue()));
				loginSession.getUsernameMap().values().remove(jwtToken);
			}
		}
		JWTBlacklist.balcklistedTokensMap.put(jwtToken, username);
		if(loginSession.getUsernameMap().containsKey(username)) {
			loginSession.getUsernameMap().remove(username);
		}
		log.debug("end blacklistToken() method");
	} 
	
}
