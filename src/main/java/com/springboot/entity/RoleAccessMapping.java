package com.springboot.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ROLE_ACCESS_MAPPING")
public class RoleAccessMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_access_id")
	private long id;
	
	@Column 
	private boolean accessRead;
	
	@Column 
	private boolean accessWrite;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id")
	private RolesEntity roleentity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "access_id")
	private ScreenDetailEntity screenDetailEntity;
	
	
//	@Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof RoleAccessMapping )) return false;
//        return id != null && id.equals(((RoleAccessMapping) o).getId());
//    }
// 
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }

}
