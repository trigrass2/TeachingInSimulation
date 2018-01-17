package com.cas.sim.tis.view.control.imp.resource;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.table.BtnsCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.control.imp.table.TextFieldCell;
import com.cas.sim.tis.view.controller.LoginController;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * 资源列表界面
 * @功能 ResourceList.java
 * @作者 Caowj
 * @创建日期 2018年1月17日
 * @修改人 Caowj
 */
public class ResourceList extends HBox implements IContent {

	public ResourceList() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/resource/List.fxml");
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
	 * 加载资源
	 */
	private void loadResources() {
		if (RoleConst.ADMIN == LoginController.USER_ROLE) {

		} else if (RoleConst.TEACHER == LoginController.USER_ROLE) {

		} else if (RoleConst.STUDENT == LoginController.USER_ROLE) {

		}
	}

	private void initTable() {
		Table table = new Table();
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER);
		name.setEditable(true);
		name.setCellFactory(TextFieldCell.forTableColumn());
		name.setKey("name");
		name.setText("资源名称");//FIXME
		name.setMaxWidth(110);
		name.getStyleClass().add("gray-label");
		// 上传日期
		Column<String> updateDate = new Column<>();
		updateDate.setAlignment(Pos.CENTER);
		updateDate.setEditable(true);
		updateDate.setCellFactory(TextFieldCell.forTableColumn());
		updateDate.setKey("update_date");
		updateDate.setText("上传日期");
		updateDate.setMaxWidth(160);
		updateDate.getStyleClass().add("gray-label");
		// 编辑按钮
		Column<String> btns = new Column<String>();
		btns.setCellFactory(BtnsCell.forTableColumn());
		btns.setAlignment(Pos.CENTER_RIGHT);
		btns.setPrefWidth(145);
		table.getColumns().addAll(id, name, updateDate, btns);
	}

	@Override
	public Region getContent() {
		return this;
	}

}
