package com.xoriant.entity;

import java.time.LocalDate;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EMPLOYEE_PERSONAL_DETAILS")
public class EmployeePersonalDetails{
	
	@Id
	@Column(name = "emp_id")
	Long empId;
	
	@OneToOne(mappedBy = "employeePersonalDetails", fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "emp_id")
	EmployeeEntity employee;
	
	@Column(name = "mobile_no")
	String mobile;
	
	
	@Column(name= "gender")
	String gender;
	
	@Column(name="date_of_birth")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate dob;
	
	@Column(name = "country")
	String country;
	
	@Column(name = "hobbies")
	String hobby;
	
	@Lob
	@Column(name = "profile_image")
	byte[] profile;	
	
}