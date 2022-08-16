package com.xoriant.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
public class RolesEntity extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int id;

	@Column(name = "rolename")
	private String roleName;

	public RolesEntity(String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdateDate,
			String roleName) {
		super(createdBy, createdDate, lastUpdatedBy, lastUpdateDate);
		this.roleName = roleName;
	}
}
