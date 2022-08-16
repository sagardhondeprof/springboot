package com.xoriant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.entity.RolesEntity;

@Repository
@Transactional
public interface RolesRepository extends JpaRepository<RolesEntity, Integer>, JpaSpecificationExecutor<RolesEntity> {

	Optional<RolesEntity> findByRoleName(String roleName);
}
