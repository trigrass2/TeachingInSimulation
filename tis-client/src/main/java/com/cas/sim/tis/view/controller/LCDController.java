package com.cas.sim.tis.view.controller;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.circuit.Multimeter;
import com.cas.sim.tis.circuit.meter.Digit;
import com.cas.sim.tis.circuit.meter.Function;
import com.cas.sim.tis.circuit.meter.ModeType;
import com.cas.sim.tis.circuit.meter.Range;
import com.cas.sim.tis.circuit.meter.Rotary;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Setter;

public class LCDController implements Initializable {
	@FXML
	private Pane lcdView;
	@FXML
	private Pane content;

	@FXML
	private Label hold;
	@FXML
	private Label auto;

	@FXML
	private Label nano;
	@FXML
	private Label micro;
	@FXML
	private Label milli;

	@FXML
	private Label capacity;
	@FXML
	private Label am;
	@FXML
	private Label volt;

	@FXML
	private Label dc;
	@FXML
	private Label ac;

	@FXML
	private Label million;
	@FXML
	private Label kilo;
	@FXML
	private Label ohm;
	@FXML
	private ImageView diode;
	@FXML
	private ImageView on_off;
	@FXML
	private Label hz;

	@Setter
	private Multimeter multimeter;

	private Digit[] digits;
	private Rectangle[] dots;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// create effect for on LEDs
//		Glow onEffect = new Glow(1.7f);
//		onEffect.setInput(new InnerShadow());
//		// create effect for on dot LEDs
//		Glow onDotEffect = new Glow(1.7f);
//		onDotEffect.setInput(new InnerShadow(5, Color.BLACK));
		// create effect for off LEDs
		InnerShadow offEffect = new InnerShadow();
		digits = new Digit[4];
		for (int i = 0; i < 4; i++) {
			Digit digit = new Digit(Color.web("#003333"), Color.rgb(0, 0, 0, 0), null, offEffect);
			digit.setLayoutX(35 + i * 70);
			digit.setLayoutY(42.0f);
			digits[i] = digit;
			content.getChildren().add(digit);
		}

		dots = new Rectangle[3];
		for (int i = 0; i < 3; i++) {
			Rectangle dot = new Rectangle(10, 18, Color.web("#003333"));
			dot.setLayoutX(87 + i * 70);
			dot.setLayoutY(135.0f);
			dots[i] = dot;
			content.getChildren().add(dot);
		}
	}

	public BufferedImage snapshot() {
		lcdView.layout();
		WritableImage image = lcdView.snapshot(null, null);
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
		return bufferedImage;
	}

	public void update() {
		Rotary rotary = multimeter.getRotary();

		hide(hold, auto, nano, micro, milli, kilo, million, ohm, am, volt, dc, ac, capacity, diode, on_off, hz);
		hide(dots);
		hide(digits);

		switch (rotary) {
		case OFF:
			break;
		case AV:
			show(volt, ac, auto);
			break;
		case DV:
			show(volt, dc, auto);
			break;
		case DmV:
			show(volt, milli, dc);
			break;
		case Ohms:
			show(auto, ohm);
			break;
		case C:
			show(auto, capacity, nano);
			break;
		case A:
			show(auto, dc, am);
			break;
		case mA:
			show(auto, dc, am, milli);
			break;
		case muA:
			show(auto, dc, micro, am);
			break;
		case Temperature:
			break;
		}

		range();
	}

	void show(Node... nodes) {
		for (Node node : nodes) {
			node.setVisible(true);
		}
	}

	void hide(Node... nodes) {
		for (Node node : nodes) {
			node.setVisible(false);
		}
	}

	public void hold(boolean result) {
		hold.setVisible(result);
	}

	public void range() {
		Range range = multimeter.getRange();
		if (range == null) {
			return;
		}

		auto.setVisible(multimeter.isAutoRange());

		int resolution = range.getResolution(); // 小数点后的有效位数

//		FIXME
		double value = multimeter.getValue();

//		数字
		hide(digits);
		for (int i = digits.length - 1; i >= digits.length - 1 - resolution; i--) {
			digits[i].showNumber(0);
			show(digits[i]);
		}
//		小数点
		hide(dots);
		if (resolution > 0) {
			show(dots[dots.length - resolution]);
		}

//		数量级
		double magnitude = range.getMagnitude();
		Double num = new Double(magnitude);

		hide(nano, micro, milli, kilo, million);
		if (num.equals(1E3)) {
			show(kilo);
		} else if (num.equals(1E6)) {
			show(million);
		} else if (num.equals(1E-3)) {
			show(milli);
		} else if (num.equals(1E-6)) {
			show(micro);
		} else if (num.equals(1E-9)) {
			show(nano);
		}
	}

	public void mode() {
		hide(hold, auto, nano, micro, milli, kilo, million, ohm, am, volt, dc, ac, capacity, diode, on_off, hz);
		hide(dots);
		hide(digits);
		
		range();

		hide(diode, on_off);

		Function mode = multimeter.getFunction();
		ModeType type = mode.getType();
		switch (type) {
		case AC:
			hide(dc);
			show(ac);
			break;
		case DC:
			hide(ac);
			show(dc);
			break;
		case Diode:
			show(diode, volt);
			break;
		case Ohm:
			show(auto, ohm);
			break;
		case Capacitance:
			break;
		case ON_Off:
			show(on_off, ohm);
			break;
		}
	}
}
