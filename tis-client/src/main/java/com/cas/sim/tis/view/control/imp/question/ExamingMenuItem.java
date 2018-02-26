package com.cas.sim.tis.view.control.imp.question;

import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.svg.SVGGlyph;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ExamingMenuItem extends HBox {

	private Library library;
	private RotateTransition rotateTransition;

	public ExamingMenuItem(Library library) {
		this.library = library;
		// 预置考试进行中提示
		SVGGlyph glyph = new SVGGlyph("iconfont.svg.clock", Color.WHITE, 22);
		if (rotateTransition != null) {
			rotateTransition.stop();
		}
		rotateTransition = new RotateTransition(Duration.millis(100), glyph);
		rotateTransition.setFromAngle(-20);
		rotateTransition.setToAngle(20);
		rotateTransition.setAutoReverse(true);

		rotateTransition.setOnFinished(e -> {
			if (rotateTransition.getToAngle() == 20) {
				rotateTransition.setFromAngle(20);
				rotateTransition.setToAngle(0);
				rotateTransition.setCycleCount(1);
				rotateTransition.setDelay(Duration.ZERO);
				rotateTransition.playFromStart();
			} else {
				rotateTransition.setFromAngle(-20);
				rotateTransition.setToAngle(20);
				rotateTransition.setCycleCount(3);
				rotateTransition.setDelay(Duration.seconds(2));
				rotateTransition.playFromStart();
			}
		});
		rotateTransition.playFromStart();

		String name = library.getName();

		Button examing = new Button();
		examing.setGraphic(glyph);
		examing.setText(name);
		examing.setTooltip(new Tooltip(name));
		examing.getStyleClass().add("examing-menu");
		VBox.setVgrow(examing, Priority.ALWAYS);
		setAlignment(Pos.CENTER);
	}
}
