package com.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.entity.UserEntity;
import com.springboot.pojo.UserPOJO;
import com.springboot.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private UserEntity user;
	
	
	@Mock
	UserRepository repository;
	
	@InjectMocks
	UserService service;
	
	@BeforeEach
	void setUp() {
		user = new UserEntity();
		user.setId(1111);
		user.setUserName("testing");
		user.setPassword("password");
	}
	
	@Test
	void getall() {
		List<UserPOJO> pojo =  service.findAll();
		//verify(repository.findAll());
		List<UserEntity> pjo = repository.findAll();
		assertEquals(pojo.size(), pjo.size());
	}
	
	@Test
	void finByName() {
		when(repository.findByUserName("testing")).thenReturn(user);
		UserEntity user1 = service.findByUserName(user.getUserName());
		assertThat(user1).isNotNull();
	}

}
