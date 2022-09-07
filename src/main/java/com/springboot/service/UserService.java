package com.springboot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.entity.AccessMappingEntity;
import com.springboot.entity.RolesEntity;
import com.springboot.entity.UserEntity;
import com.springboot.pojo.UserPOJO;
import com.springboot.pojo.UserRolesPOJO;
import com.springboot.pojo.UserUpdatePOJO;
import com.springboot.repository.RolesRepository;
import com.springboot.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public boolean adduser(UserPOJO user) {
		
		String loggeduser = userName();
		UserEntity userdetails = new UserEntity(loggeduser, LocalDateTime.now(), loggeduser, LocalDateTime.now(),
				user.getDisplayName(), user.getUserName(), getEncodedPassword(user.getPassword()), user.getEmail(),
				user.getMobileNumber());
		UserEntity list =null;
		list= userRepository.findByUserName(userdetails.getUserName());
		if(list!=null)
		{
			System.out.println(list.getUserName());
			System.out.println("flll");
			return false;
		}
		else
		{
			if (null != user.getRoles()) {
				for (String role : user.getRoles()) {
					Optional<RolesEntity> roleEntity = rolesRepository.findByRoleName(role);
					if (roleEntity.isPresent()) {
						userdetails.getRoleMapping().add(new AccessMappingEntity(userdetails, roleEntity.get()));
					}
				}
			}
			UserEntity response = userRepository.save(userdetails);
			UserPOJO responseEntity = mapToPojo(response);
			responseEntity.setRoles(getRoles(response.getRoleMapping()));
			responseEntity.setPassword("");
			return true;
		}
	}

	public String[] getRoles(Set<AccessMappingEntity> role) {
		return role.stream().map(mapper -> mapper.getRole().getRoleName()).toArray(String[]::new);
	}

	private UserPOJO mapToPojo(UserEntity user) {
		return modelMapper.map(user, UserPOJO.class);
	}

	public Optional<UserPOJO> updateuser(UserUpdatePOJO user) {
		Optional<UserEntity> oldUser = userRepository.findById(user.getId());
		String loggedInUser = userName();
		if (oldUser.isPresent()) {
			oldUser.get().setLastUpdatedBy(loggedInUser);
			oldUser.get().setLastUpdateDate(LocalDateTime.now());
			oldUser.get().setDisplayName(user.getDisplayName());
			oldUser.get().setEmail(user.getEmail());
			oldUser.get().setMobileNumber(user.getMobileNumber());
			UserEntity response = userRepository.save(oldUser.get());
			UserPOJO responseEntity = mapToPojo(response);
			responseEntity.setRoles(getRoles(response.getRoleMapping()));
			responseEntity.setPassword("");
			return Optional.of(responseEntity);
		} else {
			return Optional.empty();
		}
	}

	public Optional<UserPOJO> updateUserRoles(@Valid UserRolesPOJO user) {
		UserPOJO request = modelMapper.map(user, UserPOJO.class);
		Optional<UserEntity> oldUser = userRepository.findById((int) user.getId());
		if (oldUser.isPresent()) {
			updateRoles(oldUser.get(), request);
			if (null != request.getRoles()) {
				for (String role : request.getRoles()) {
					Optional<RolesEntity> roleEntity = rolesRepository.findByRoleName(role);
					if (roleEntity.isPresent()) {
						AccessMappingEntity mapping = new AccessMappingEntity(oldUser.get(), roleEntity.get());
						if (!oldUser.get().getRoleMapping().contains(mapping)) {
							oldUser.get().getRoleMapping().add(mapping);
						}
					}
				}
			}
			UserEntity response = userRepository.save(oldUser.get());
			UserPOJO responseEntity = mapToPojo(response);
			responseEntity.setRoles(getRoles(response.getRoleMapping()));
			responseEntity.setPassword("");
			return Optional.of(responseEntity);
		} else {
			return Optional.empty();
		}
	}

	private void updateRoles(UserEntity user, UserPOJO request) {
		if (user.getRoleMapping().size() > 0) {
			user.getRoleMapping()
					.removeAll(user.getRoleMapping().stream().filter(
							stream -> !Arrays.asList(request.getRoles()).contains(stream.getRole().getRoleName()))
							.collect(Collectors.toSet()));
		}
	}

	public List<UserPOJO> findAll() {
		List<UserPOJO> users = new ArrayList<>();
		userRepository.findAll().stream().forEach(result -> {
			UserPOJO responseEntity = mapToPojo(result);
			responseEntity.setRoles(getRoles(result.getRoleMapping()));
			responseEntity.setPassword("");
			users.add(responseEntity);
		});
		return users;
	}
	
	public UserEntity findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public String userName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}
	//profile updatee
	public boolean updateProfile(byte [] image,String username)
	{
		
		UserEntity user = userRepository.findByUserName(username);
		if(user!=null)
		{
			user.setProfile(image);
			userRepository.save(user);
			return true;
		}
		else return false;
		
		
	}
}
