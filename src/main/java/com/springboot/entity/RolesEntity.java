package com.springboot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
public class RolesEntity extends AuditEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int id;

	@Column(name = "rolename")
	private String roleName;
	
	@Column(name = "description")
	private String description;
	
	@JsonIgnore
	@OneToMany(mappedBy = "roleentity", cascade = { CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<RoleAccessMapping> accessControl;

	public RolesEntity(String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdateDate,
			String roleName,String description) {
		super(createdBy, createdDate, lastUpdatedBy, lastUpdateDate);
		this.roleName = roleName;
		this.description=description;
	}
	
	public RolesEntity(String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdateDate,
			String roleName,String description, List<RoleAccessMapping> list) {
		super(createdBy, createdDate, lastUpdatedBy, lastUpdateDate);
		this.roleName = roleName;
		this.description=description;
		this.accessControl = list;
	}
	
	
}
