package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Radius {
	RADIUS1(2, 6, "0.75mm"), RADIUS2(3, 7, "1.00mm"), RADIUS3(4, 8, "1.50mm"), RADIUS4(6, 10, "2.50mm"), RADIUS5(8, 12, "4.00mm");

	private int innerRadius;
	private int outterRadius;
	private String radius;

	public float getRadiusWidth() {
		return convert(outterRadius);
	}

	public static Radius getRadiusByKey(String key) {
		for (Radius r : values()) {
			if (r.name().equals(key)) {
				return r;
			}
		}
		return null;
	}

	public static float convert(int width) {
//		考虑模型的显示比例。
		return width * 2f / 20000;
	}
}
