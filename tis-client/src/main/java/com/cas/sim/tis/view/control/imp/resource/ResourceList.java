package com.cas.sim.tis.view.control.imp.resource;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.table.BtnsCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.IconCell;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.LoginController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;

/**
 * 资源列表界面
 * @功能 ResourceList.java
 * @作者 Caowj
 * @创建日期 2018年1月17日
 * @修改人 Caowj
 */
public class ResourceList extends HBox implements IContent {

	@FXML
	private Table table;
	
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
		initTable();
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
		// 资源图标
		Column<Integer> icon = new Column<>();
		icon.setAlignment(Pos.CENTER_RIGHT);
		icon.setKey("type");
		icon.setText("");
		icon.setMaxWidth(25);
		icon.getStyleClass().add("gray-label");
		icon.setCellFactory(IconCell.forTableColumn(new StringConverter<Integer>() {
			@Override
			public String toString(Integer type) {
				switch (type) {
				case 0:
					return ResourceConsts.PIC_ICON;
				case 1:
					return ResourceConsts.SWF_ICON;
				case 2:
					return ResourceConsts.VIDEO_ICON;
				case 3:
					return ResourceConsts.TXT_ICON;
				case 4:
					return ResourceConsts.WORD_ICON;
				case 5:
					return ResourceConsts.PPT_ICON;
				case 6:
					return ResourceConsts.EXCEL_ICON;
				case 7:
					return ResourceConsts.PDF_ICON;
				default:
					return null;
				}
			}

			@Override
			public Integer fromString(String string) {
				return null;
			}
		}));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText("资源名称");//FIXME
		name.setMaxWidth(110);
		name.getStyleClass().add("gray-label");
		// 上传日期
		Column<String> updateDate = new Column<>();
		updateDate.setAlignment(Pos.CENTER);
		updateDate.setKey("update_date");
		updateDate.setText("上传日期");
		updateDate.setMaxWidth(160);
		updateDate.getStyleClass().add("gray-label");
		// 编辑按钮
		Column<String> btns = new Column<String>();
		btns.setCellFactory(BtnsCell.forTableColumn());
		btns.setAlignment(Pos.CENTER_RIGHT);
		btns.setPrefWidth(145);
		table.getColumns().addAll(id, icon, name, updateDate, btns);
	}

	@Override
	public Region getContent() {
		return this;
	}

}
