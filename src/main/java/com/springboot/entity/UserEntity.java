package com.springboot.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(name = "displayname")
	private String displayName;

	@NotNull
	@Column(name = "username")
	private String userName;

	@NotNull
	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "mobilenumber")
	private String mobileNumber;
	
	@Lob
	@Column(name = "profile")
	byte[] profile;	

	@OneToMany(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<AccessMappingEntity> roleMapping = new HashSet<>();

	public UserEntity(String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdateDate,
			String displayName, String userName, String password, String email, String mobileNumber) {
		super(createdBy, createdDate, lastUpdatedBy, lastUpdateDate);
		this.displayName = displayName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.mobileNumber = mobileNumber;
	}
}
