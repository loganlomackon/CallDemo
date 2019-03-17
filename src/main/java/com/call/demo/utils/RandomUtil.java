package com.call.demo.utils;

public class RandomUtil {

	public static Integer getRandomIntBetween(int a, int b) {
		if (a == b) {
			return a;
		}
		int num = (int) (Math.random() * (b - a + 1)) + a;
		return num;
	}
	
}
