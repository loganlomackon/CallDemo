package com.call.demo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.call.demo.domain.Employee;
import com.call.demo.domain.JobLevel;
import com.call.demo.rest.dto.ResponseCallDTO;
import com.call.demo.utils.RandomUtil;


@Service
public class IncomingCallService {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private RedisCacheService redisCacheService;
	
	public ResponseCallDTO acceptCall(String caller, String content) {
		System.out.println("Accept call: caller:"+caller+", content:"+content);
		
		List<JobLevel> jobLevels = JobLevel.getValuesOrderByCallPassLevelAsc();
		while(jobLevels.size() > 0) {
			//1.Select one employee to answer the call. If no one is available, return "no response".
			Long accepterId = selectEmployeeAcceptCall(jobLevels);
			if (accepterId == null) {
				ResponseCallDTO dto = new ResponseCallDTO();
				dto.setContent("no response");
				return dto;
			}
			
			//Simulate the process that employee answer the call
			Employee accepter = employeeService.getById(accepterId);
			String response = getResponseFromEmployee(accepterId, accepter.getJobLevel(), caller, content);
			//Set employee status back to "not busy"
			redisCacheService.setEmployeeBusy(accepterId.toString(), "false");
			if (response != null) {
				//Response != null => The employee can solve caller's problem
				ResponseCallDTO dto = new ResponseCallDTO();
				dto.setEmployee_name(accepter.getName());
				dto.setJob_level(accepter.getJobLevel().toString());
				dto.setContent(response);
				return dto;
			}
			else {
				//Pass the call to higher job levels
				JobLevel currentJobLevel = accepter.getJobLevel();
				jobLevels = JobLevel.getValuesHigherThan(currentJobLevel);
			}
		}

		return null;
	}
	
	public String getResponseFromEmployee(Long employeeId, JobLevel jobLevel, String caller, String content) {
		System.out.println("employeeId:"+employeeId+" processing");
		//TODO Get response from employee
		timeCost();
		
		if (!jobLevel.isMustAnswer()) {
			//Simulate if current employee could solve the call.
			Boolean canSolve = RandomUtil.getRandomIntBetween(0, 1) == 0;
			if (!canSolve) {
				System.out.println("Cannot solve");
				return null;
			}
		}
		
		String response = "Response to:"+content;
		System.out.println(response);
		return response;
	}
	
	//Just simulate the time employee spent to answer the call
	private void timeCost() {
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Select one employee and set to "busy".
	 * This step should be synchronized, for different thread should not choose the same person simultaneously,
	 */
	synchronized public Long selectEmployeeAcceptCall(List<JobLevel> jobLevels) {
		Map<String, Map<String, String>> statusMap = redisCacheService.getEmployeeStatus();
		if (statusMap == null) {
			return null;
		}
		
		Map<String, String> employeeIsBusy = statusMap.get(RedisCacheService.EMPLOYEE_IS_BUSY);
		Map<String, String> employeeJobLevel = statusMap.get(RedisCacheService.EMPLOYEE_JOB_LEVEL);
		Map<JobLevel, List<String>> levelIdList = new HashMap<JobLevel, List<String>>();
		for (String id : employeeJobLevel.keySet()) {
			if ("false".equals(employeeIsBusy.get(id))) {
				if (jobLevels.get(0).toString().equals(employeeJobLevel.get(id))) {
					redisCacheService.setEmployeeBusy(id, "true");
					return Long.valueOf(id);
				}
				else {
					JobLevel level = JobLevel.parse(employeeJobLevel.get(id));
					if (levelIdList.get(level) == null) {
						levelIdList.put(level, new ArrayList<String>());
					}
					levelIdList.get(level).add(id);
				}
			}
		}
		
		jobLevels.remove(0);
		for (JobLevel level : jobLevels) {
			List<String> ids = levelIdList.get(level);
			if (ids != null && ids.size() > 0) {
				String target = ids.get(0);
				redisCacheService.setEmployeeBusy(target, "true");
				return Long.valueOf(target);
			}
		}
		
		return null;
	}

	
}
