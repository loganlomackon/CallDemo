package com.call.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.call.demo.domain.Employee;
import com.call.demo.repositories.EmployeeRepository;

@Service
public class EmployeeService { 
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Transactional
	public Employee getById(Long id) {
		return employeeRepository.getOne(id);
	}
	
	@Transactional
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}
	
	@Transactional
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee saveAndUpdateCache(Employee employee) {
		employee = employeeRepository.save(employee);
		/*
		 * Update cache while adding new employee
		 */
		redisCacheService.addNewEmployeeIntoCache(employee);
		return employee;
	}
}
