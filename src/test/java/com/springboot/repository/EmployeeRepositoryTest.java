package com.springboot.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.entity.EmployeeEntity;

@SpringBootTest
class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	@Test
	void findByName() {
		EmployeeEntity emp = new EmployeeEntity();
		emp.setId(1112L);
		emp.setFirstName("saurabh");
		emp.setLastName("gade");
		emp.setEmail("saurabh@gmail.com");
	    employeeRepository.save(emp);
	    List<EmployeeEntity> emp1 =  employeeRepository.findByFirstName("saurabh");
		assertEquals(emp1.get(0).getFirstName(), emp.getFirstName());
	}

	
	@Test
	void findByEmail() {
		EmployeeEntity emp = new EmployeeEntity();
		emp.setId(1113L);
		emp.setFirstName("saurabh");
		emp.setLastName("gade");
		emp.setEmail("saurabh1@gmail.com");
	    employeeRepository.save(emp);
	    
	    EmployeeEntity emp1 =  employeeRepository.findByEmail("saurabh1@gmail.com");
	    assertEquals(emp1.getEmail(), emp.getEmail());
	}
}
