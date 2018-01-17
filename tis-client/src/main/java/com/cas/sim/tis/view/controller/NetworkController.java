/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates. All rights reserved. Use is subject to license terms. This file is available and licensed under the following license: Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cas.sim.tis.view.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.view.LoginView;

import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * 设置服务器信息界面
 * @功能 NetworkController.java
 * @作者 Caowj
 * @创建日期 2018年1月17日
 * @修改人 Caowj
 */
@FXMLController
@PropertySource(value = { "file:cfg.properties" })
public class NetworkController implements Initializable {

	protected double xOffset;
	protected double yOffset;

	@FXML
	private VBox drag;
	@FXML
	private TextField ip;
	@FXML
	private TextField port;

	@Value("${server.base.address}")
	private String address;

	@Value("${server.base.port}")
	private Integer num;

	private static Properties properties = new Properties();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 绑定窗口拖拽事件
		drag.setOnMousePressed((MouseEvent event) -> {
			event.consume();
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		drag.setOnMouseDragged((MouseEvent event) -> {
			event.consume();
			Application.getStage().setX(event.getScreenX() - xOffset);

			// 根据自己的需求，做不同的判断
			if (event.getScreenY() - yOffset < 0) {
				Application.getStage().setY(0);
			} else {
				Application.getStage().setY(event.getScreenY() - yOffset);
			}
		});
		// 读取服务器信息配置
		try {
			properties.load(new FileInputStream("cfg.properties"));
			ip.setText(address);
			port.setText(String.valueOf(num));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void ok() {
		// 记录服务器信息
		properties.setProperty("server.base.address", ip.getText());
		properties.setProperty("server.base.address", port.getText());
		back();
	}

	/**
	 * 取消
	 */
	@FXML
	public void cancel() {
		back();
	}

	/**
	 * 返回登录界面
	 */
	private void back() {
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), Application.getScene().getRoot());
		rotateTransition.setAxis(new Point3D(0, 1, 0));
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(90);
		rotateTransition.setOnFinished(e -> {
			Application.showView(LoginView.class);
			RotateTransition r = new RotateTransition(Duration.millis(200), Application.getScene().getRoot());
			r.setAxis(new Point3D(0, 1, 0));
			r.setFromAngle(90);
			r.setToAngle(0);
			r.play();
			ip.setText(address);
			port.setText(String.valueOf(num));
		});
		rotateTransition.play();
	}
}
