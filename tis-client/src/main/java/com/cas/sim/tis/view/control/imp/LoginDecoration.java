package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.view.NetworkView;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * 放大、缩小、关闭按钮条
 * @功能 TopBtns.java
 * @作者 caowj
 * @创建日期 2017年12月29日
 * @修改人 caowj
 */
public class LoginDecoration extends HBox {

	public LoginDecoration() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/LoginDecoration.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void min() {
		// TODO 最小化
		Application.getStage().setIconified(true);
	}

	@FXML
	private void more() {
		// TODO 高级
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), Application.getScene().getRoot());
		rotateTransition.setAxis(new Point3D(0, 1, 0));
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(90);
		rotateTransition.setOnFinished(e->{
			Application.showView(NetworkView.class);
			RotateTransition r = new RotateTransition(Duration.millis(200), Application.getScene().getRoot());
			r.setNode(Application.getScene().getRoot());
			r.setAxis(new Point3D(0, 1, 0));
			r.setFromAngle(90);
			r.setToAngle(0);
			r.play();
		});
		rotateTransition.play();
	}

	@FXML
	private void close() {
		// FIXME
		System.exit(0);
	}
}
