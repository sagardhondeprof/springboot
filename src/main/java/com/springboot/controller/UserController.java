package com.springboot.controller;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.pojo.UserPOJO;
import com.springboot.pojo.UserRolesPOJO;
import com.springboot.pojo.UserUpdatePOJO;
import com.springboot.service.UserService;

@RestController
@RequestMapping("registration")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("adduser")
	public ResponseEntity<?> add(@RequestBody @Valid UserPOJO user) {
		
		boolean isUserAdded = userService.adduser(user);
		
		if(isUserAdded)
		{
			System.out.println("taken");
			return new ResponseEntity<>("User added Successfully", HttpStatus.CREATED);
		}
		else {
			System.out.println("Username alredy taken");
			return new ResponseEntity<>("Username alredy taken", HttpStatus.CONFLICT);
		}
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
	@PostMapping(value = {"uploadprofile"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> uploadprofile(@RequestPart("username") String username,
			@RequestPart("profile") MultipartFile profile){
		System.out.println("gggggg");
		
		boolean isProfileUpdated= false;
		try {
			byte[] image = profile.getBytes();
			
			isProfileUpdated = userService.updateProfile(image, username);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		if(isProfileUpdated) return new ResponseEntity<>("profile updated",HttpStatus.OK);
		else return new ResponseEntity<>("error updateing profile picture",HttpStatus.NOT_MODIFIED);
		
	}
	

}
