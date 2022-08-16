package com.xoriant.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.pojo.UserPOJO;
import com.xoriant.pojo.UserRolesPOJO;
import com.xoriant.pojo.UserUpdatePOJO;
import com.xoriant.service.UserService;

@RestController
@RequestMapping("registration")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("adduser")
	public ResponseEntity<?> add(@RequestBody @Valid UserPOJO user) {
		return new ResponseEntity<>(userService.adduser(user), HttpStatus.CREATED);
	}

	@PutMapping("updateuser")
	public ResponseEntity<?> updateuser(@RequestBody @Valid UserUpdatePOJO user) {
		Optional<UserPOJO> userResponse = userService.updateuser(user);
		if (userResponse.isPresent()) {
			return new ResponseEntity<>(userResponse.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("updateuserroles")
	public ResponseEntity<?> updateUserRoles(@RequestBody @Valid UserRolesPOJO user) {
		Optional<UserPOJO> userResponse = userService.updateUserRoles(user);
		if (userResponse.isPresent()) {
			return new ResponseEntity<>(userResponse.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
