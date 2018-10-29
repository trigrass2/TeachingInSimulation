package com.cas.sim.tis.circuit.meter;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 量程
 * @author zzy
 */
@Slf4j
@Getter
public class Range {
	private double max; // 最大值
	private double min; // 最小值
	private int resolution; // 保留多少位有效数字
	private double magnitude; // 数量级： p:1E-12、n:1E-9、mu:1E-6、m:1E-3、 :1E0、K:1E3、M1E6、G1E9

	private int index = 0;

	public Range(double min, double max, int resolution, double magnitude) {
		this.min = min;
		this.max = max;
		this.resolution = resolution;
		this.magnitude = magnitude;
	}

	public double formatValue(double input) {
		if (input < 0) {
//			throw new IllegalArgumentException(String.format("input : %s", input));
		}
		double result = 0;
		if (input > max) {
			return Double.MAX_VALUE;
		}
		try {
			result = new BigDecimal(input / magnitude).setScale(resolution, BigDecimal.ROUND_DOWN).doubleValue();
		} catch (NumberFormatException e) {
			log.error("for input {}/{}", input, magnitude);
		}
		return result;
	}
}
