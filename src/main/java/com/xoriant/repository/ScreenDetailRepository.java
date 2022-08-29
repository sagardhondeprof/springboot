package com.xoriant.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.entity.ScreenDetailEntity;

@Repository
@Transactional
public interface ScreenDetailRepository extends JpaRepository<ScreenDetailEntity, Long>{

	ScreenDetailEntity findByAccessName(String accessName);
	
}
