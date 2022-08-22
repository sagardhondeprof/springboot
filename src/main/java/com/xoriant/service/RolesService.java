package com.xoriant.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xoriant.entity.EmployeeEntity;
import com.xoriant.entity.RolesEntity;
import com.xoriant.pojo.EmployeePOJO;
import com.xoriant.pojo.RolesPOJO;
import com.xoriant.repository.RolesRepository;

@Service
public class RolesService {

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private ModelMapper modelMapper;

	public RolesPOJO addroles(RolesPOJO roles) {
		Optional<RolesEntity> roleEntity = rolesRepository.findByRoleName(roles.getRoleName());
		if (roleEntity.isPresent()) {
			RolesEntity roleentity = roleEntity.get();
			RolesPOJO response = mapToPojo(roleentity);
			return response;
		} else {
			String loggeduser = userName();
			RolesEntity addrole = new RolesEntity(loggeduser, LocalDateTime.now(), loggeduser, LocalDateTime.now(),
					roles.getRoleName(),roles.getDescription());
			RolesEntity roleentity = rolesRepository.save(addrole);
			RolesPOJO response = mapToPojo(roleentity);
			return response;
		}
	}

	private RolesPOJO mapToPojo(RolesEntity user) {
		return modelMapper.map(user, RolesPOJO.class);
	}

	public String userName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}

	public List<RolesEntity> getRoles() {
		 List<RolesEntity> rolesList = rolesRepository.findAll();
		return rolesList;
	}

	public void deleteRole(int id) {
		rolesRepository.deleteById(id);
	}

	public RolesPOJO updateRole(int id,RolesPOJO role) {
		RolesEntity roleEntity = rolesRepository.findById(id).get();
		String loggedInUser = userName();
		if (roleEntity != null) {
			roleEntity.setRoleName(role.getRoleName());
			roleEntity.setDescription(role.getDescription());
			
		}
		RolesEntity roleEntity2 = rolesRepository.save(roleEntity);
		RolesPOJO responseEntity = mapToPojo(roleEntity2);
		return responseEntity;
	}
}
