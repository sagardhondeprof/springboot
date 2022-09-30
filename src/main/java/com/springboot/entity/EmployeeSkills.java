package com.springboot.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMPLOYEE_SKILLS")
public class EmployeeSkills {

	@Id
	private Long Id;
	
	@Column(name= "qualification")
	private String qualification; //qualification
	
	@Column(name= "languages")
	private String languages;
	
	@Column(name = "framworks")
	private String frameworks;
	
	@Column(name = "tools")
	private String tools;
	
	@Column(name = "projects")
	private String projects;
	
	@OneToOne(fetch = FetchType.EAGER)
	@MapsId
	@JsonIgnore
	private EmployeeEntity employee;
}
