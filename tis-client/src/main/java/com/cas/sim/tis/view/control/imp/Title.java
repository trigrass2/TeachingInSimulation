package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Title extends HBox {
	private static final Logger LOG = LoggerFactory.getLogger(Title.class);

	@FXML
	private Label title;

	public Title() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Title.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
			LOG.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
	}

	public Title(String title) {
		this();
		setTitle(title);
	}

	public String getTitle() {
		return title.getText();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}
}
