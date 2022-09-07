package com.springboot.pojo;

import lombok.Data;

@Data
public class AuthenticationResponsePOJO {

	private String jwtToken;
	private String username;
	private String[] roles;
	private byte[] profile;	

	public AuthenticationResponsePOJO(String jwtToken, String[] roles) {
		this.jwtToken = jwtToken;
		this.roles = roles;
	}
}
