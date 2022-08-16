package com.xoriant.pojo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AuthenticatePOJO {

	@NotNull
	private String userName;
	@NotNull
	private String userPassword;
}
