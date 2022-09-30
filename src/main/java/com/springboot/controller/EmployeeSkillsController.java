package com.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.EmployeeSkills;
import com.springboot.service.EmployeeSkillService;

@RestController
@RequestMapping("skills")
public class EmployeeSkillsController {
	
	@Autowired
	private EmployeeSkillService employeeSkillService;
	
	@GetMapping("getskills/{id}")
	public ResponseEntity<?> getEmployeeSkills(@PathVariable("id") long id){
		System.out.println("in controller");
		EmployeeSkills emp = employeeSkillService.getSkills(id);
		if(emp != null) {
			return new ResponseEntity<>(emp, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Skills not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("addskill")
	public ResponseEntity<?> addEmployeeSkills(@RequestBody EmployeeSkills skills){
		System.out.println(skills);
		System.out.println(skills.getLanguages()+" lang");
		System.out.println(skills.getFrameworks()+ " frame");
		EmployeeSkills empSkills =  employeeSkillService.addEmpSkills(skills);
		return new ResponseEntity<>(empSkills, HttpStatus.ACCEPTED);
		
	}
	
	@DeleteMapping("deleteskills/{id}")
	public ResponseEntity<?> deleteSkills(@PathVariable("id") long id){
		employeeSkillService.deleteSkills(id);
		return new ResponseEntity<>("Skills deleted", HttpStatus.ACCEPTED);
	
	}

}
