package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

/**
 * 放大、缩小、关闭按钮条
 * @功能 TopBtns.java
 * @作者 caowj
 * @创建日期 2017年12月29日
 * @修改人 caowj
 */
public class Decoration extends HBox {

	public Decoration() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Decoration.fxml");
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
	}
	
	@FXML
	private void max() {
		// TODO 最大化
	}

	@FXML
	private void close() {
		// FIXME
		System.exit(0);
	}
}
