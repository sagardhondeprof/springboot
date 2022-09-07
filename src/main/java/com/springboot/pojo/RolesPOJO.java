package com.springboot.pojo;

import java.util.HashMap;

import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RolesPOJO {

	private int id;
	@NotNull
	private String roleName;
	private String description;
	private HashMap<String, String> accessMapping;
	
}
