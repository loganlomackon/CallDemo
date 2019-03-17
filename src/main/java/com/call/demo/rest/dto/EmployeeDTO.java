package com.call.demo.rest.dto;

import java.io.Serializable;

import com.call.demo.domain.Employee;

@SuppressWarnings("serial")
public class EmployeeDTO implements Serializable {

	private String job_level;
	
	private String name;
	
	private String is_busy;
	
	public EmployeeDTO() {
		
	}
	
	public static EmployeeDTO createDTO(Employee employee) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setJob_level(employee.getJobLevel().toString());
		dto.setName(employee.getName());
		return dto;
	}
	
	public String getJob_level() {
		return job_level;
	}
	public void setJob_level(String job_level) {
		this.job_level = job_level;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIs_busy() {
		return is_busy;
	}
	public void setIs_busy(String is_busy) {
		this.is_busy = is_busy;
	}
}
