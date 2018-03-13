package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 放大、缩小、关闭按钮条
 * @功能 LoginDecoration.java
 * @作者 caowj
 * @创建日期 2017年12月29日
 * @修改人 caowj
 */
public class LoginDecoration extends HBox {

	private Region settingView;

	public LoginDecoration() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/LoginDecoration.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 最小化
	 */
	@FXML
	private void min() {
		Stage stage = (Stage) getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	private void more() {
		// TODO 高级
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), getScene().getRoot());
		rotateTransition.setAxis(new Point3D(0, 1, 0));
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(90);
		rotateTransition.setOnFinished(e -> {
//			Application.showView(NetworkView.class);

			Parent root = getScene().getRoot();

			getScene().setRoot(settingView);

			RotateTransition r = new RotateTransition(Duration.millis(200), root);
			r.setNode(root);
			r.setAxis(new Point3D(0, 1, 0));
			r.setFromAngle(90);
			r.setToAngle(0);
			r.play();
		});
		rotateTransition.play();
	}

	@FXML
	private void close() {
		Platform.exit();
		System.exit(0);
	}

	public void setSettingView(Region settingView) {
		this.settingView = settingView;
	}
}
