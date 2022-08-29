package com.xoriant.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACCESS_DETAILS")
public class ScreenDetailEntity {
	
	@Id
	@Column(name = "access_id")
	private long accessId;
	
	@Column(name = "access_name")
	private String accessName;
	
	@JsonIgnore
	@OneToMany(
	        mappedBy = "screenDetailEntity",
	        cascade = CascadeType.ALL
	    )
	private List<RoleAccessMapping> roleAccessMappings;

}
