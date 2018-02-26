package com.cas.sim.tis.view.control.imp.info;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.consts.TemplateConsts;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.UserAction;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.util.FileUtil;
import com.cas.util.Util;
import com.github.pagehelper.PageInfo;

import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

public class TeacherList extends HBox implements IContent {
	private static final Logger LOG = LoggerFactory.getLogger(TeacherList.class);

	@FXML
	private Title title;
	@FXML
	private Title manage;
	@FXML
	private Table table;
	@FXML
	private PaginationBar pagination;

	public TeacherList() {
		loadFXML();

		initialize();
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/information/List.fxml");
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
	 * 界面初始化
	 */
	private void initialize() {
		title.setTitle(MsgUtil.getMessage("teacher.title.list"));
		manage.setTitle(MsgUtil.getMessage("teacher.title.manage"));
		this.pagination.setContent(pageIndex -> {
			reload(pageIndex);
		});
		createTable();
	}

	/**
	 * 创建表格
	 */
	private void createTable() {
		// 数据库唯一表示
		Column<Integer> primary = new Column<>();
		primary.setPrimary(true);
		primary.setVisible(false);
		primary.setKey("id");
		// 教师工号
		Column<String> code = new Column<>();
		code.setAlignment(Pos.CENTER);
		code.setKey("code");
		code.setText(MsgUtil.getMessage("teacher.code"));
		code.setPrefWidth(250);
		// 教师名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("teacher.name"));
		name.setPrefWidth(250);
		table.getColumns().addAll(primary, code, name);
		Column<String> modify = new Column<String>();
		modify.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.modify"), Priority.ALWAYS, "blue-btn", id -> {
			modify((int) id);
		}));
		modify.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(modify);
		// 删除按钮
		Column<String> del = new Column<String>();
		del.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), Priority.ALWAYS, "blue-btn", id -> {
			AlertUtil.showConfirm(MsgUtil.getMessage("table.delete"), response -> {
				if (response == ButtonType.YES) {
					SpringUtil.getBean(UserAction.class).deleteUser((int) id);
					pagination.reload();
				}
			});
		}));
		del.setAlignment(Pos.CENTER_RIGHT);
		del.setMaxWidth(58);
		del.setMinWidth(58);
		table.getColumns().add(del);
	}

	private void reload(Integer pageIndex) {
		int pageSize = 10;

		PageInfo<User> pageInfo = SpringUtil.getBean(UserAction.class).findTeachers(pageIndex + 1, pageSize);
		if (pageInfo == null) {
			pagination.setPageCount(0);
			table.setItems(null);
			table.build();
		} else {
			pagination.setPageCount((int) pageInfo.getPages());
			JSONArray array = new JSONArray();
			array.addAll(pageInfo.getList());
			table.setItems(array);
			table.build();
		}
	}

	@FXML
	private void importExcel() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls"));
		File source = chooser.showOpenDialog(GUIState.getStage());
		if (source == null) {
			return;
		}
		List<User> users = new ArrayList<User>();
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), "Sheet1", 2);
		for (int i = 2; i < result.length; i++) {
			Object codeObj = result[i][0];
			if (Util.isEmpty(codeObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("teacher.code"));
				AlertUtil.showAlert(AlertType.WARNING, reason);
				return;
			}
			String code = String.valueOf(codeObj).trim();
			if (code.length() > 20) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("teacher.code"), String.valueOf(20));
				AlertUtil.showAlert(AlertType.WARNING, reason);
				return;
			}
			Object nameObj = result[i][1];
			if (Util.isEmpty(nameObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("teacher.name"));
				AlertUtil.showAlert(AlertType.WARNING, reason);
				return;
			}
			String name = String.valueOf(nameObj).trim();
			if (name.length() > 20) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("teacher.name"), String.valueOf(20));
				AlertUtil.showAlert(AlertType.WARNING, reason);
				return;
			}
			User user = new User();
			user.setCode(code);
			user.setName(name);
			user.setRole(RoleConst.TEACHER);
			user.setCreator(Session.get(Session.KEY_LOGIN_ID));
			users.add(user);
		}
		try {
			SpringUtil.getBean(UserAction.class).addUsers(users);
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.import.success"));
			pagination.reload();
		} catch (Exception e) {
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void template() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setInitialFileName("教师信息模版.xls");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls"));
		File target = chooser.showSaveDialog(GUIState.getStage());
		if (target == null) {
			return;
		}
		FileUtil.copyFile(TemplateConsts.TEACHER_TEMPLATE, target.getAbsolutePath(), true);
		AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.export.success"));
	}

	private void modify(int id) {
		User teacher = SpringUtil.getBean(UserAction.class).findUserByID(id);

		Dialog<User> dialog = new Dialog<>();
		dialog.setDialogPane(new TeacherModifyDialog(teacher));
		dialog.setTitle(MsgUtil.getMessage("teacher.dialog.modify"));
		dialog.setPrefSize(635, 320);
		dialog.showAndWait().ifPresent(user -> {
			if (user == null) {
				return;
			}
			try {
				SpringUtil.getBean(UserAction.class).modifyUser(user);
				AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("data.update.success"));
				pagination.reload();
			} catch (Exception e) {
				e.printStackTrace();
				AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
			}
		});
	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		pagination.reload();
		return new Region[] { this };
	}

}
