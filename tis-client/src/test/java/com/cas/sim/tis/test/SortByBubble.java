package com.cas.sim.tis.test;

import java.util.Arrays;

/**
 * 冒泡排序
 * @author JhonKkk
 * @date 2018-4-10 15:03:13
 */
public class SortByBubble {
	public final static void main(String[] args) {
		Integer it[] = new Integer[50];
		for (int i = 0; i < 50; i++) {
			it[i] = (int) (Math.random() * 50);
		}
		System.out.println(Arrays.toString(it));
		sort(it);
		System.out.println(Arrays.toString(it));
		System.out.println("第10个最大值" + it[9]);
	}

	public final static <T extends Number> T[] sort(T[] t) {
		if (t == null || t.length <= 0) {
			return null;
		}
		int length = t.length;
		T temp;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length - 1; j++) {
				if (t[j].intValue() < t[j + 1].intValue()) {
					temp = t[j];
					t[j] = t[j + 1];
					t[j + 1] = temp;
				}
			}
		}
		return t;
	}
}
