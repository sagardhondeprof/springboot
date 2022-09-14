package com.springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.entity.UserEntity;

@SpringBootTest
class RepositoryTest {

	@Autowired
	private UserRepository userRepo;
	
	@Test
	void findByUserName() {
		UserEntity user = new UserEntity();
		user.setId(100);
		user.setUserName("tester");
		user.setMobileNumber("123456");
		userRepo.save(user);
		
		UserEntity user1  = userRepo.findByUserName("tester");
		
		assertEquals(user1.getUserName(), user.getUserName());
		
		//assertThat(user1.getUserName()).isEqualTo(user.getUserName());
	}

}
