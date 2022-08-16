package com.xoriant.pojo;

import lombok.Data;

@Data
public class AuthenticationResponsePOJO {

	private String jwtToken;
	private String[] roles;

	public AuthenticationResponsePOJO(String jwtToken, String[] roles) {
		this.jwtToken = jwtToken;
		this.roles = roles;
	}
}
