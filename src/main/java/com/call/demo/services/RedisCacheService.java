package com.call.demo.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.call.demo.domain.Employee;

@Service
public class RedisCacheService {
	@Autowired
	private Environment env;
	@Autowired
	private EmployeeService employeeService;
	
	private static JedisPool jedisPool;
	public static final String EMPLOYEE_IS_BUSY = "employee_is_busy";
	public static final String EMPLOYEE_JOB_LEVEL = "employee_job_level";
	
	@PostConstruct
	public void init() {
		String redisPath = env.getProperty("redis.path");
		/*
		 * Ensure thread-safe with Jedis pool
		 */
		final JedisPoolConfig poolConfig = buildPoolConfig();
		jedisPool = new JedisPool(poolConfig, redisPath);
		System.out.println("Create Redis:"+redisPath);
	}
	
	private JedisPoolConfig buildPoolConfig() {
	    final JedisPoolConfig poolConfig = new JedisPoolConfig();
	    poolConfig.setMaxTotal(128);
	    poolConfig.setMaxIdle(128);
	    poolConfig.setMinIdle(16);
	    poolConfig.setTestOnBorrow(true);
	    poolConfig.setTestOnReturn(true);
	    poolConfig.setTestWhileIdle(true);
	    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
	    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
	    poolConfig.setNumTestsPerEvictionRun(3);
	    poolConfig.setBlockWhenExhausted(true);
	    return poolConfig;
	}
	
	public Jedis getJedis() {
		return jedisPool.getResource();
	}
	
	/*
	 * Retrieve employee job level and busy status from Redis cache
	 * Implement initialization with the concept of singleton.
	 */
	public Map<String, Map<String, String>> getEmployeeStatus() {
		Jedis jedis = getJedis();
		Map<String, String> employeeJobLevel = jedis.hgetAll(EMPLOYEE_JOB_LEVEL);
		if (employeeJobLevel == null || employeeJobLevel.isEmpty()) {
			synchronized(RedisCacheService.class) {
				if (employeeJobLevel == null || employeeJobLevel.isEmpty()) {
					List<Employee> all = employeeService.getAll();
					if (all.size() == 0) {
						return null;
					}
					
					for (Employee emp : all) {
						jedis.hset(EMPLOYEE_IS_BUSY, emp.getId().toString(), "false");
						jedis.hset(EMPLOYEE_JOB_LEVEL, emp.getId().toString(), emp.getJobLevel().toString());
					}
					employeeJobLevel = jedis.hgetAll(EMPLOYEE_JOB_LEVEL);
				}
			}
		}
		
		Map<String, String> employeeIsBusy = jedis.hgetAll(EMPLOYEE_IS_BUSY);
		Map<String, Map<String, String>> statusMap = new HashMap<String, Map<String, String>>();
		statusMap.put(EMPLOYEE_IS_BUSY, employeeIsBusy);
		statusMap.put(EMPLOYEE_JOB_LEVEL, employeeJobLevel);
		return statusMap;
	}
	public Map<String, String> getEmployeeBusyStatus() {
		return getEmployeeStatus().get(EMPLOYEE_IS_BUSY);
	}
	
	public void setEmployeeBusy(String id, String isBusy) {
		Jedis jedis = getJedis();
		jedis.hset(EMPLOYEE_IS_BUSY, id, isBusy);
	}
	
	public void addNewEmployeeIntoCache(Employee employee) {
		Jedis jedis = getJedis();
		jedis.hset(EMPLOYEE_IS_BUSY, employee.getId().toString(), "false");
		jedis.hset(EMPLOYEE_JOB_LEVEL, employee.getId().toString(), employee.getJobLevel().toString());
	}
	
}
