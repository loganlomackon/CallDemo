package com.call.demo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_EMPLOYEES", uniqueConstraints={
		@UniqueConstraint(columnNames={"ID"})})
public class Employee extends AbstractObject implements Serializable {

	@Enumerated
	@Column(name="JOB_LEVEL")
	private JobLevel jobLevel;
	
	@Column(name="NAME")
	private String name;
	
	public Employee() {
		
	}
	
	public JobLevel getJobLevel() {
		return jobLevel;
	}
	public void setJobLevel(JobLevel jobLevel) {
		this.jobLevel = jobLevel;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
