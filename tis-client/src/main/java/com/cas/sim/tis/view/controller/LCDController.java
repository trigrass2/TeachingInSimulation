package com.cas.sim.tis.view.controller;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.circuit.Multimeter;
import com.cas.sim.tis.circuit.meter.Digit;
import com.cas.sim.tis.circuit.meter.FuncType;
import com.cas.sim.tis.circuit.meter.Function;
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
		InnerShadow offEffect = new InnerShadow();
		digits = new Digit[5];
		for (int i = 0; i < 5; i++) {
			Digit digit = new Digit(Color.web("#003333"), Color.rgb(0, 0, 0, 0), null, offEffect);
			digit.setLayoutX(35 + i * 70);
			digit.setLayoutY(42.0f);
			digits[i] = digit;
			content.getChildren().add(digit);
		}

		digits[4].setLayoutX(35 + 2 * 70);

		dots = new Rectangle[3];
		for (int i = 0; i < 3; i++) {
			Rectangle dot = new Rectangle(10, 18, Color.web("#003333"));
			dot.setLayoutX(87 + i * 70);
			dot.setLayoutY(135.0f);
			dots[i] = dot;
			content.getChildren().add(dot);
		}
	}

	public void update() {
		hide(hold, auto, nano, micro, milli, kilo, million, ohm, am, volt, dc, ac, capacity, diode, on_off, hz);
		hide(dots);
		hide(digits);

		updateMode();
		updateHold();
		updateRange();
		updateValue();
	}

	private void updateValue() {
		if (multimeter.getRotary() == Rotary.OFF) {
			return;
		}
		Range range = multimeter.getRange();
		if (range == null) {
			return;
		}
//		获取格式化之后的数值。
		double value = multimeter.format();
		int resolution = range.getResolution(); // 小数点后的有效位数

		if (value == Double.MAX_VALUE) {
			digits[1].showNumber(0);
			digits[4].showCharacter(0);
			show(digits[1], digits[4]);
		} else {
			writeNormalNumber(resolution, range, value);
		}

//		小数点
		if (resolution > 0) {
			show(dots[dots.length - resolution]);
		}

//		数量级
		double magnitude = range.getMagnitude();
		Double num = new Double(magnitude);

//		hide(nano, micro, milli, kilo, million);
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

	private int writeNormalNumber(int resolution, Range range, double value) {
		int[] beforeDot = new int[4];// 万用表显示4个数字
		int[] afterDot = new int[3];// 小数点后最多有3位数

		Double d = new Double(value);
//		整数部分
		int one = d.intValue() % 10;
		int ten = (d.intValue() - one) % 100 / 10;
		int hundred = (d.intValue() - ten * 10 - one) % 1000 / 100;
		int thousand = (d.intValue() - hundred * 100 - ten * 10 - one) / 1000;

		beforeDot[0] = one;
		beforeDot[1] = ten;
		beforeDot[2] = hundred;
		beforeDot[3] = thousand;

//		小数部分
		value = d.doubleValue() - d.intValue();
		d = new Double(value * Math.pow(10, resolution));
		int decile = (int) (d.intValue() / Math.pow(10, resolution - 1));
		int percentile = (d.intValue() - decile) % 100 / 10;
		int quantile = (d.intValue() - percentile * 10 - decile * 100);

		afterDot[0] = decile;
		afterDot[1] = percentile;
		afterDot[2] = quantile;

		int finalShow[] = new int[4];
		for (int i = 0; i < finalShow.length - resolution; i++) {
			finalShow[i] = beforeDot[beforeDot.length - 1 - resolution - i];
		}
		for (int i = 0; i < resolution; i++) {
			finalShow[finalShow.length - resolution + i] = afterDot[i];
		}
//		System.out.println(resolution);
//		System.out.println(String.format("%s%s%s%s", finalShow[0], finalShow[1], finalShow[2], finalShow[3]));

//		从低位 往 高位 写数字
		for (int i = 0; i < 4; i++) {
			Digit digit = digits[i];
			if (finalShow[i] == 0 && i < 4 - resolution - 1) { // 小数点前 有 前导零 的， 要把0 去掉（不显示）
				continue;
			}
			digit.showNumber(finalShow[i]);
			show(digit);
		}
		return resolution;
	}

	public BufferedImage snapshot() {
		update();

		WritableImage image = lcdView.snapshot(null, null);
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

//		try {
//			ImageIO.write(bufferedImage, "png", new java.io.File("D://image.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return bufferedImage;
	}

	private void updateHold() {
		hold.setVisible(multimeter.isHold());
	}

	private void updateRange() {
		auto.setVisible(multimeter.isAutoRange());
	}

	private void updateMode() {
		Function mode = multimeter.getFunction();
		if (mode == null) {
			return;
		}
		FuncType type = mode.getType();
		switch (type) {
		case AC_V:
			hide(dc);
			show(ac, volt);
			break;
		case DC_V:
			hide(ac);
			show(dc, volt);
			break;
		case AC_A:
			hide(dc);
			show(ac, am);
			break;
		case DC_A:
			hide(ac);
			show(dc, am);
			break;
		case Diode:
			show(diode, volt);
			break;
		case Ohm:
			show(auto, ohm);
			break;
		case Capacitance:
			show(capacity);
			break;
		case On_Off:
			show(on_off, ohm);
			break;
		case Centigrade:
			break;
		case Fahrenheit:
			break;
		default:
			break;
		}
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
}
