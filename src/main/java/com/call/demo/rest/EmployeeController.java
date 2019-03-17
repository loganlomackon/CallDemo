package com.call.demo.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.call.demo.domain.Employee;
import com.call.demo.domain.JobLevel;
import com.call.demo.rest.dto.EmployeeDTO;
import com.call.demo.services.EmployeeService;
import com.call.demo.services.RedisCacheService;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private RedisCacheService redisCacheService;
	
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		employee.setJobLevel(JobLevel.parse(employeeDTO.getJob_level()));
		employee.setName(employeeDTO.getName());
		employeeService.saveAndUpdateCache(employee);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@RequestMapping(value="/list", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> listEmployee() {
		List<EmployeeDTO> dtos = new ArrayList<EmployeeDTO>();
		List<Employee> employees = employeeService.getAll();
		Map<String, String> busyMap = redisCacheService.getEmployeeBusyStatus();
		for (Employee employee : employees) {
			EmployeeDTO dto = EmployeeDTO.createDTO(employee);
			dto.setIs_busy(busyMap.get(employee.getId().toString()));
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
}
