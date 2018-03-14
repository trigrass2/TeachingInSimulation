package com.cas.sim.tis.view.control.imp.jme;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class WireRadius extends StackPane {

	private Circle inner;
	private Circle outer;

	public WireRadius() {
		this(0, Color.TRANSPARENT, 0, Color.TRANSPARENT);
	}

	public WireRadius(double radiusIn, Color in, double radiusOut, Color out) {
		this.getChildren().addAll(outer = new Circle(radiusOut, out), inner = new Circle(radiusIn, in));
		this.setPrefSize(24, 24);
	}

	public Paint getOuterFill() {
		return outer.getFill();
	}

	public double getOuterRadius() {
		return outer.getRadius();
	}

	public void setOuterFill(Paint paint) {
		this.outer.setFill(paint);
	}

	public void setOuterRadius(double radius) {
		this.outer.setRadius(radius);
	}

	public Paint getInnerFill() {
		return inner.getFill();
	}

	public double getInnerRadius() {
		return inner.getRadius();
	}

	public void setInnerFill(Paint paint) {
		this.inner.setFill(paint);
	}

	public void setInnerRadius(double radius) {
		this.inner.setRadius(radius);
	}

}
