package com.springboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.EmployeeEntity;
import com.springboot.entity.EmployeeSkills;
import com.springboot.repository.EmployeeRepository;
import com.springboot.repository.EmployeeSkillsRepository;

@Service
public class EmployeeSkillService {
	
	@Autowired
	private EmployeeSkillsRepository employeeSkillsRepo;
	
	@Autowired
	private EmployeeRepository employeeRepo;

	public EmployeeSkills getSkills(long id) {
		Optional<EmployeeSkills> emp = employeeSkillsRepo.findById(id);
		EmployeeSkills empskills = null;
		if(emp.isPresent()) {
			empskills = emp.get();
		}
		return empskills;
	}

	public EmployeeSkills addEmpSkills(EmployeeSkills skills) {
		long id = skills.getId();
		EmployeeEntity emp = employeeRepo.findById(id).get();
		EmployeeSkills empSkills = getSkills(id);
		if(empSkills == null) {
			empSkills = new EmployeeSkills();
			empSkills.setEmployee(emp);
		}
		if(skills.getLanguages() != null) {
			empSkills.setLanguages(skills.getLanguages());
		}
		if(skills.getFrameworks() != null) {
			empSkills.setFrameworks(skills.getFrameworks());
		}
		if(skills.getProjects() != null) {
			empSkills.setProjects(skills.getProjects());
		}
		if(skills.getTools() != null) {
			empSkills.setTools(skills.getTools());
		}
		if(skills.getQualification() != null) {
			empSkills.setQualification(skills.getQualification());
		}
		EmployeeSkills savedSkills = employeeSkillsRepo.save(empSkills);
		return savedSkills;
	}

	public void deleteSkills(long id) {
		Optional<EmployeeSkills> empskills = employeeSkillsRepo.findById(id);
		if(empskills.isPresent()) {
			employeeSkillsRepo.deleteById(id);
		}
	}

}
