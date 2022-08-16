package com.xoriant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.pojo.RolesPOJO;
import com.xoriant.service.RolesService;

@RestController
@RequestMapping("roles")
public class RolesController {

	@Autowired
	private RolesService rolesService;

	@PostMapping("addrole")
	public ResponseEntity<?> add(@RequestBody RolesPOJO roles) {
		return new ResponseEntity<>(rolesService.addroles(roles), HttpStatus.CREATED);
	}

}
