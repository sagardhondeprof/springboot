package com.springboot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springboot.entity.EmployeeEntity;
import com.springboot.entity.RoleAccessMapping;
import com.springboot.entity.RolesEntity;
import com.springboot.entity.ScreenDetailEntity;
import com.springboot.pojo.EmployeePOJO;
import com.springboot.pojo.RolesPOJO;
import com.springboot.repository.RolesRepository;
import com.springboot.repository.ScreenDetailRepository;

@Service
public class RolesService {

	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired 
	private ScreenDetailRepository screenDetailRepository;

	@Autowired
	private ModelMapper modelMapper;

	public boolean addroles(RolesPOJO roles) {
		
		Optional<RolesEntity> roleEntity = rolesRepository.findByRoleName(roles.getRoleName());
		//RolesEntity roleEntity1 = rolesRepository.findByRoleName(roles.getRoleName()).get();
		if (roleEntity.isPresent()) {
			RolesEntity roleentity = roleEntity.get();
			RolesPOJO response = mapToPojo(roleentity);
			return false;
		} else {
			String loggeduser = userName();
			RolesEntity addrole = new RolesEntity(loggeduser, LocalDateTime.now(), loggeduser, LocalDateTime.now(),
					roles.getRoleName(),roles.getDescription());
			
			List<RoleAccessMapping> list1 = new ArrayList<>();
			HashMap<String, String> accessMapping = roles.getAccessMapping();
			
			for(Map.Entry<String, String> entry : accessMapping.entrySet()) {
				ScreenDetailEntity screen = screenDetailRepository.findByAccessName(entry.getKey());
				RoleAccessMapping obj = new RoleAccessMapping();
				obj.setRoleentity(addrole);
				obj.setScreenDetailEntity(screen);
				if(entry.getValue().contains("write")) {
					if(entry.getValue().contains("true")) {
						obj.setAccessRead(false);
						obj.setAccessWrite(false);
					}
				}
				if(entry.getValue().contains("read")) {
					if(entry.getValue().contains("true")) {
						obj.setAccessRead(false);
					}
				}
				list1.add(obj);
			}
			
//			ScreenDetailEntity sc = screenDetailRepository.findById(100L).get();
//			RoleAccessMapping a1 = new RoleAccessMapping();
//			a1.setRoleentity(addrole);
//			a1.setScreenDetailEntity(sc);
//			RoleAccessMapping a2 = new RoleAccessMapping();
//			a2.setRoleentity(addrole);
//			a2.setScreenDetailEntity(sc);
//			List<RoleAccessMapping> list = new ArrayList<>();
//			list.add(a1);
//			list.add(a2);
			
			//sc.setRoleAccessMappings(list);
			addrole.setAccessControl(list1);
			RolesEntity roleentity = rolesRepository.save(addrole);
			RolesPOJO response = mapToPojo(roleentity);
			return true;
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

	public List<ScreenDetailEntity> getAccessList() {
		List<ScreenDetailEntity> list = screenDetailRepository.findAll();
		return list;
	}
}
