package com.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.springboot.entity.EmployeeEntity;
import com.springboot.pojo.EmployeePOJO;
import com.springboot.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
	
	private EmployeeEntity employee;
	private EmployeePOJO pjo;

	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeService employeeService;
	
	@BeforeEach
	void setUp() {
		//this.employeeService = new EmployeeService(this.employeeRepository);
	    employee = new EmployeeEntity();
	    employee.setId(1111L);
	    employee.setFirstName("saurbh");
	    employee.setLastName("gade");
	    employee.setEmail("saurabh@gmail.com");
	    
		//employeeRepository.save(emp);
	}
	
	
	
	@Test
	void getemployees() {
		
		employeeService.getAll();
		verify(employeeRepository).findAll();
	}
	
	@Test
	void searchById() {
		when(employeeRepository.findById(1111L)).thenReturn(Optional.of(employee));
		EmployeeEntity employeeEntity = employeeService.searchEmployeeById(employee.getId());
		assertThat(employeeEntity).isNotNull();
		
	}
	
	@Test
	void delete() {
		long id = 1111L;
		willDoNothing().given(employeeRepository).deleteById(id);
		employeeService.deleteById(id);
		verify(employeeRepository, times(1)).deleteById(id);

	}
}
