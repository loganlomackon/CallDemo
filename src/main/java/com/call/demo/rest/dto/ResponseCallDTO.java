package com.call.demo.rest.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResponseCallDTO implements Serializable {

	private String employee_name;
	
	private String job_level;
	
	private String content;
	
	public ResponseCallDTO() {
		
	}
	
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	
	public String getJob_level() {
		return job_level;
	}
	public void setJob_level(String job_level) {
		this.job_level = job_level;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content= content;
	}
	
}
