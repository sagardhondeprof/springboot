package com.xoriant.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xoriant.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginSession {
	
	@Autowired
	JwtUtil jwtUtil;
	
	private Map<String, Boolean> tokenMap = new HashMap<>();

	private Map<String, String> usernameToken = new HashMap<>();
	
	
	public Map<String, Boolean> getTokenMap(){
		return tokenMap;
	}
	
	public Map<String, String> getUsernameMap(){
		return usernameToken;
	}

	@Scheduled(cron = "${blacklist.tokens.cleanup.schedule}")
	public void expiredTokenCleanUp() {
		tokenMap.entrySet().removeIf(entry -> jwtUtil.isTokenExpired(entry.getKey()));
		log.info("tokenMap cleanup scheduler run !");
	}
}
