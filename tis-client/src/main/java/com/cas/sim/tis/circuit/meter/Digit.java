package com.cas.sim.tis.circuit.meter;

import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Shear;

/**
 * Simple 7 segment LED style digit. It supports the numbers 0 through 9.
 */
public class Digit extends Parent {

	private static final boolean[][] DIGIT_COMBINATIONS = new boolean[][] { //
			new boolean[] { true, false, true, true, true, true, true }, // 0
			new boolean[] { false, false, false, false, true, false, true }, // 1
			new boolean[] { true, true, true, false, true, true, false }, // 2
			new boolean[] { true, true, true, false, true, false, true }, // 3
			new boolean[] { false, true, false, true, true, false, true }, // 4
			new boolean[] { true, true, true, true, false, false, true }, // 5
			new boolean[] { true, true, true, true, false, true, true }, // 6
			new boolean[] { true, false, false, false, true, false, true }, // 7
			new boolean[] { true, true, true, true, true, true, true }, // 8
			new boolean[] { true, true, true, true, true, false, true }, // 9
			new boolean[] { false, false, true, true, false, true, false } // L
	};

	private static final boolean[][] CHARECTOR_COMBINATIONS = new boolean[][] { //
			new boolean[] { false, false, true, true, false, true, false } // 0 - L
	};

	private final Polygon[] polygons = new Polygon[] { //
			new Polygon(2, 0, 52, 0, 42, 10, 12, 10), //
			new Polygon(12, 49, 42, 49, 52, 54, 42, 59, 12f, 59f, 2f, 54f), //
			new Polygon(12, 98, 42, 98, 52, 108, 2, 108), //
			new Polygon(0, 2, 10, 12, 10, 47, 0, 52), //
			new Polygon(44, 12, 54, 2, 54, 52, 44, 47), //
			new Polygon(0, 56, 10, 61, 10, 96, 0, 106), //
			new Polygon(44, 61, 54, 56, 54, 106, 44, 96) }; //

	private final Color onColor;
	private final Color offColor;
	private final Effect onEffect;
	private final Effect offEffect;

	public Digit(Color onColor, Color offColor, Effect onEffect, Effect offEffect) {
		this.onColor = onColor;
		this.offColor = offColor;
		this.onEffect = onEffect;
		this.offEffect = offEffect;
		getChildren().addAll(polygons);
		getTransforms().add(new Shear(-0.05, 0));
		showNumber(0);
	}

	public final void showNumber(Integer num) {
		if (num < 0 || num > 9) {
			num = 0; // default to 0 for non-valid numbers
		}
		for (int i = 0; i < 7; i++) {
			polygons[i].setFill(DIGIT_COMBINATIONS[num][i] ? onColor : offColor);
			polygons[i].setEffect(DIGIT_COMBINATIONS[num][i] ? onEffect : offEffect);
		}
	}

	/**
	 * 0 - L<br/>
	 * @param num
	 */
	public final void showCharacter(Integer num) {
		for (int i = 0; i < 7; i++) {
			polygons[i].setFill(CHARECTOR_COMBINATIONS[num][i] ? onColor : offColor);
			polygons[i].setEffect(CHARECTOR_COMBINATIONS[num][i] ? onEffect : offEffect);
		}
	}

}
