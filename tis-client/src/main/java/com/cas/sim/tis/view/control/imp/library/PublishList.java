package com.cas.sim.tis.view.control.imp.library;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.view.control.IContent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * 教师发布的考核记录
 * @功能 PublishList.java
 * @作者 Caowj
 * @创建日期 2018年2月9日
 * @修改人 Caowj
 */
public class PublishList extends VBox implements IContent {
	
	public PublishList() {
		loadFXML();
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/library/PublishList.fxml");
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
	
	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		return new Region[] { this };
	}

}
