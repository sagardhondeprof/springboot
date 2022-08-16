package com.xoriant.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xoriant.entity.EmployeeEntity;
import com.xoriant.pojo.EmployeePOJO;
import com.xoriant.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
	private ModelMapper modelMapper;

	public Object getUsers() {
		return employeeRepo.findAll();
	}

	public Page<EmployeePOJO> getpagelist(Pageable pageable) {
		Page<EmployeeEntity> entitypage= employeeRepo.findAll(pageable);
		Page<EmployeePOJO> pojopage = pageMapTOpojo(entitypage);
		
		return pojopage;

	}

	public void deleteById(long id) {
		employeeRepo.deleteById(id);

	}

	public EmployeePOJO adduser(EmployeePOJO employeepojo) {

		String loggeduser = userName();
		EmployeeEntity employee = new EmployeeEntity(loggeduser, LocalDateTime.now(), loggeduser, LocalDateTime.now(),
				employeepojo.getFirstName(), employeepojo.getLastName(), employeepojo.getEmail(),
				employeepojo.getDate());
		EmployeeEntity employeentity = employeeRepo.save(employee);
		EmployeePOJO responseEntity = mapToPojo(employeentity);
		return responseEntity;
	}

	public EmployeePOJO updateEmployee(long id, EmployeePOJO emppojo) {
		EmployeeEntity employee = employeeRepo.findById(id).get();
		String loggedInUser = userName();
		if (employee != null) {
			employee.setFirstName(emppojo.getFirstName());
			employee.setLastName(emppojo.getLastName());
			employee.setEmail(emppojo.getEmail());
			employee.setDate(emppojo.getDate());
			employee.setLastUpdateDate(LocalDateTime.now());
			employee.setLastUpdatedBy(loggedInUser);
		}
		EmployeeEntity employeentity = employeeRepo.save(employee);
		EmployeePOJO responseEntity = mapToPojo(employeentity);
		return responseEntity;
	}

	private EmployeePOJO mapToPojo(EmployeeEntity user) {
		return modelMapper.map(user, EmployeePOJO.class);
	}
	
	private Page<EmployeePOJO> pageMapTOpojo(Page<EmployeeEntity> entitypage){
		return entitypage.map(entityObject -> modelMapper.map(entityObject, EmployeePOJO.class));	
	}

	public List<EmployeeEntity> searchEmployee(String searchterm) {
		List<EmployeeEntity> searchlist = employeeRepo.findByFirstName(searchterm);
		return searchlist;

	}

	public String userName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}

	public List<EmployeePOJO> searchEmployeeLike(String query) {
		List<EmployeeEntity> list = employeeRepo.searchEmployee(query);
		List<EmployeePOJO> pojolist = listMapTOpojo(list);
		return pojolist;
	}
	
	private List<EmployeePOJO> listMapTOpojo(List<EmployeeEntity> entitypage){
		List<EmployeePOJO> pojo = new ArrayList<>();
		entitypage.forEach(emppojoobj -> pojo.add(modelMapper.map(emppojoobj, EmployeePOJO.class)));
		return pojo;
	}
	public EmployeeEntity searchEmployeeById(long id) {
		EmployeeEntity emp= employeeRepo.findById(id).get();
		return emp;
		
	}
	public List<EmployeeEntity> getAll() {
		List<EmployeeEntity> empList = employeeRepo.findAll();
		return empList;
		
	}
}
