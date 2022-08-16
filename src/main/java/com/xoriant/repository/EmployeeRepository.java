package com.xoriant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xoriant.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>{
	EmployeeEntity findByEmail(String email);
	
	
	List<EmployeeEntity> findByFirstName(String fname);
	
	@Query("SELECT p FROM EmployeeEntity p WHERE " +
            "p.firstName LIKE CONCAT('%',:query, '%')" +
            "Or p.lastName LIKE CONCAT('%', :query, '%')")
	List<EmployeeEntity> searchEmployee(String query);

}
