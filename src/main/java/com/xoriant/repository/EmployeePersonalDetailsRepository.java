package com.xoriant.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.entity.EmployeePersonalDetails;

@Repository
@Transactional
public interface EmployeePersonalDetailsRepository extends JpaRepository<EmployeePersonalDetails, Long>{

	EmployeePersonalDetails findByEmpId(long id);
}
