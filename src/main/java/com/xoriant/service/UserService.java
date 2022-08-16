package com.xoriant.service;

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

import com.xoriant.entity.AccessMappingEntity;
import com.xoriant.entity.RolesEntity;
import com.xoriant.entity.UserEntity;
import com.xoriant.pojo.UserPOJO;
import com.xoriant.pojo.UserRolesPOJO;
import com.xoriant.pojo.UserUpdatePOJO;
import com.xoriant.repository.RolesRepository;
import com.xoriant.repository.UserRepository;

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

	public UserPOJO adduser(UserPOJO user) {
		String loggeduser = userName();
		UserEntity userdetails = new UserEntity(loggeduser, LocalDateTime.now(), loggeduser, LocalDateTime.now(),
				user.getDisplayName(), user.getUserName(), getEncodedPassword(user.getPassword()), user.getEmail(),
				user.getMobileNumber());
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
		return responseEntity;
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

	public String userName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}

}
