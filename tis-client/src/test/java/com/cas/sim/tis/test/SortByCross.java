package com.cas.sim.tis.test;

/**
 * 交叉排序
 * @author JhonKkk
 * @date 2018-4-10 15:02:47
 */
public class SortByCross {
	public final static void main(String[] args) {
		Integer it[] = new Integer[10 * 10000];
		for (int i = 0; i < it.length; i++) {
			it[i] = (int) (Math.random() * it.length);
		}
		long timeStart = 0;
		long timeChat = 0;

		timeStart = System.currentTimeMillis();
		int max = sort(it, 93052);
		timeChat = System.currentTimeMillis() - timeStart;
		int index = 93052;
		System.out.println("总" + it.length + "个数,第" + index + "个最大值" + max + ";用时:" + (timeChat) / 1000.0f + "s" + "(" + timeChat + "ms)");

		timeStart = System.currentTimeMillis();
		Integer[] sorted = SortByBubble.sort(it);
		timeChat = System.currentTimeMillis() - timeStart;
		System.out.println("总" + it.length + "个数,第" + index + "个最大值" + sorted[index] + ";用时:" + (timeChat) / 1000.0f + "s" + "(" + timeChat + "ms)");
	}

	public final static <T extends Number> Integer sort(T[] t, int k) {
		if (t == null || t.length <= 0) {
			return 0;
		}
		int length = t.length;
		int min = Math.min(length, k);
		Integer it[] = new Integer[min];
		for (int i = 0; i < min; i++) {
			it[i] = t[i].intValue();
		}
		Integer temp;
		// 读取前k个数进行排序
		for (int i = 0; i < min; i++) {
			for (int n = 0; n < min - 1; n++) {
				if (it[n] < it[n + 1]) {
					temp = it[n];
					it[n] = it[n + 1];
					it[n + 1] = temp;
				}
			}
		}
		for (int i = min; i < length - 1; i++) {
			if (t[i].intValue() <= it[min - 1]) {
				continue;
			}
			for (int n = 0; n < min; n++) {
				if (t[i].intValue() > it[n]) {
					for (int g = min - 1; g > n; g--) {
						it[g] = it[g - 1];
					}
					it[n] = t[i].intValue();
					break;
				}
			}
		}
		return it[min - 1];
	}
}
