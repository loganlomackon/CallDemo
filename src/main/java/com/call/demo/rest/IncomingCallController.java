package com.call.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.call.demo.rest.dto.IncomingCallDTO;
import com.call.demo.rest.dto.ResponseCallDTO;
import com.call.demo.services.IncomingCallService;

@RestController
@RequestMapping("/api/call")
public class IncomingCallController {
	
	@Autowired
	private IncomingCallService incomingCallService;
	
	
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> acceptCall(@RequestBody IncomingCallDTO incomingCallDTO) {
		ResponseCallDTO dto = incomingCallService.acceptCall(incomingCallDTO.getCaller(), incomingCallDTO.getContent());
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
	
}
