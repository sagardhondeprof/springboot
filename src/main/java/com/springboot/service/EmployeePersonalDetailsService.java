package com.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.EmployeePersonalDetails;
import com.springboot.repository.EmployeePersonalDetailsRepository;

@Service
public class EmployeePersonalDetailsService {
	
	@Autowired
	EmployeePersonalDetailsRepository employeePersonalDetailsRepository;

	public EmployeePersonalDetails addBBasicDetails(EmployeePersonalDetails employee) {
		EmployeePersonalDetails emp = employeePersonalDetailsRepository.save(employee);
		return emp;
	}

	public EmployeePersonalDetails getBasicDetails(long id) {
		EmployeePersonalDetails emp = employeePersonalDetailsRepository.findByEmpId(id);
		return emp;
	}

}
