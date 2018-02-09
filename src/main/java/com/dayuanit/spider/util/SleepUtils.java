package com.dayuanit.spider.util;

import java.util.Random;

public class SleepUtils {
	
	private static final int[] mills = {800, 1000, 1500, 2000, 2500, 3000, 3500};
	
	public static int getMill() {
		Random random = new Random();
		return mills[random.nextInt(7)];
	}
	
	public static void main(String[] args) {
		for (int i=0; i<10000;i++) {
			System.out.println(SleepUtils.getMill());
		}
	}

}
