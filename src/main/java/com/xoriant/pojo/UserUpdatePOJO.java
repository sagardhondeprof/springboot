package com.xoriant.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePOJO {

	private int id;
	private String displayName;
	private String email;
	private String mobileNumber;

}
