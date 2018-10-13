package com.cas.sim.tis.consts;

import com.jme3.math.ColorRGBA;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WireColor {
	YELLOW(Color.YELLOW, "elec.case.wires.yellow"), GREEN(Color.GREEN, "elec.case.wires.green"), RED(Color.RED, "elec.case.wires.red"), BLUE(Color.BLUE, "elec.case.wires.blue"), BLACK(Color.BLACK, "elec.case.wires.black");

	private Color color;
	private String textKey;

	public ColorRGBA getColorRGBA() {
		return convert(color);
	}
	
	public static WireColor getWireColorByKey(String key) {
		for (WireColor c : values()) {
			if (c.name().equals(key)) {
				return c;
			}
		}
		return null;
	}
	
	public static ColorRGBA convert(Color color) {
		ColorRGBA colorRGBA = new ColorRGBA();
		colorRGBA.set((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
		return colorRGBA;
	}
}
