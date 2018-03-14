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
import javafx.scene.layout.VBox;

public class HomeMenu extends VBox implements Initializable {
	private static final Logger LOG = LoggerFactory.getLogger(HomeMenu.class);
	@FXML
	private ImageView icon;
	
	@FXML
	private Label name;
	
	public HomeMenu() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/HomeMenu.fxml");
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
		
	}
	
	public void setMenuIcon(String icon) {
		this.icon.setImage(new Image(icon));
	}
	
	public String getMenuIcon() {
		System.out.println("HomeMenu.getMenuIcon()");
		throw new RuntimeException("请不要调用这个方法，这个方法存在意义仅限于在加载界面时候不报错");
	}
	
	public void setMenuName(String title) {
		this.name.setText(title);
	}
	
	public String getMenuName() {
		return name.getText();
	}
}
