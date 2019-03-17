package com.call.demo.domain;

import java.util.ArrayList;
import java.util.List;

public enum JobLevel {
	FRESHMEN("Fresher", 0, false),
	TL("Team Lead", 1, false),
	PM("PM", 2, true);
	
	private final String description;
	private final Integer callPassLevel;
	private final Boolean mustAnswer;
	
	/*
	 * Explanation of fields. You can add as many jobs as you like, and define how the call should be passed.
	 * 1.description: Description of job title, it's required when creating a new employee.
	 * 2.callPassLevel: Define the order to pass incoming call when 
	 * no lower level employee is available or an employee cannot solve problem.
	 * 3.mustAnswer: Define if employee with this job title must answer the call. He or she will not be able to pass call to others.
	 */
	JobLevel(String description, Integer callPassLevel, Boolean mustAnswer) {
		this.description = description;
		this.callPassLevel = callPassLevel;
		this.mustAnswer = mustAnswer;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Integer getCallPassLevel() {
		return callPassLevel;
	}
	
	public Boolean isMustAnswer() {
		return mustAnswer;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
	public static List<JobLevel> getValuesOrderByCallPassLevelAsc() {
		JobLevel[] array = JobLevel.values();
		List<JobLevel> allLevels = new ArrayList<JobLevel>();
		for (int i = 0; i < array.length; i++) {
			allLevels.add(array[i]);
		}
		allLevels.sort((j1,j2) -> j1.getCallPassLevel().compareTo(j2.getCallPassLevel())); 
		return allLevels;
	}
	public static List<JobLevel> getValuesHigherThan(JobLevel level) {
		List<JobLevel> allLevels = getValuesOrderByCallPassLevelAsc();
		List<JobLevel> result = new ArrayList<JobLevel>();
		for (JobLevel lev : allLevels) {
			if (lev.getCallPassLevel() > level.getCallPassLevel()) {
				result.add(lev);
			}
		}
		return result;
	}
	
	public static JobLevel parse(String input) {
		for (JobLevel level : JobLevel.values()) {
			if (level.toString().equals(input)) {
				return level;
			}
		}
		return null;
	}
}
