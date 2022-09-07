package com.springboot.configuration;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.springboot.entity.AccessMappingEntity;
import com.springboot.entity.UserEntity;
import com.springboot.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator{
	

	@Autowired
	UserService userService;
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		UserDetails u = (UserDetails) authentication.getPrincipal();
		log.info("hasPermission " + permission + " user name:- " + u.getUsername());
		UserEntity userEntity = userService.findByUserName(u.getUsername());
		
		if(userEntity != null) {
			List<String> roles = userEntity.getRoleMapping().stream().map(role -> role.getRole().getRoleName())
					.collect(Collectors.toList());
		}
		
		for(AccessMappingEntity am : userEntity.getRoleMapping()) {
			if(am.getRole().getAccessControl().stream().anyMatch(
					predicate -> predicate.getScreenDetailEntity().getAccessName().equalsIgnoreCase(permission.toString()))) {
				return true;
			}
		}
		
		
		return false;
		
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return true;
	}

}
