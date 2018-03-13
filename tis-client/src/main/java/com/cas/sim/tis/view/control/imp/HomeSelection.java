package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class HomeSelection extends HBox implements Initializable {
	private static final Logger LOG = LoggerFactory.getLogger(HomeSelection.class);
	@FXML
	private ImageView icon;

	@FXML
	private Label name;

	@FXML
	private Text desc;

	public HomeSelection() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/HomeSelection.fxml");
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setSelectionIcon(String icon) {
		this.icon.setImage(new Image(icon));
	}

	public String getSelectionIcon() {
		throw new RuntimeException("请不要调用这个方法，这个方法存在意义仅限于在加载界面时候不报错");
	}

	public void setSelectionName(String name) {
		this.name.setText(name);
	}

	public String getSelectionName() {
		return name.getText();
	}

	public void setSelectionDesc(String desc) {
		this.desc.setText(desc);
	}

	public String getSelectionDesc() {
		return this.desc.getText();
	}
}
