package com.xoriant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.pojo.EmployeePOJO;
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
	@GetMapping("getroles")
	public ResponseEntity<?> getRoles() {
		return new ResponseEntity<>(rolesService.getRoles(), HttpStatus.ACCEPTED);
	}
	@DeleteMapping("deleterole/{id}")
	public ResponseEntity<?> deleteRole(@PathVariable("id") int id) {
		rolesService.deleteRole(id);
		return new ResponseEntity<>("Role Deleted Successfully", HttpStatus.ACCEPTED);
	}
	@PutMapping("updaterole/{id}")
	public ResponseEntity<?> updateRole(@PathVariable("id") int id,@RequestBody RolesPOJO role) {
		
		return new ResponseEntity<>(rolesService.updateRole(id,role), HttpStatus.ACCEPTED);
	}
}
