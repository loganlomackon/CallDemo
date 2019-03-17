package com.call.demo.rest.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IncomingCallDTO implements Serializable {

	private String caller;
	
	private String content;
	
	public IncomingCallDTO() {
		
	}
	
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller= caller;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content= content;
	}
	
}
