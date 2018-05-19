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
import com.cas.sim.tis.action.ClassAction;
import com.cas.sim.tis.consts.TemplateConsts;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
import com.cas.sim.tis.vo.ClassInfo;
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

public class ClassList extends HBox implements IContent {
	private static final Logger LOG = LoggerFactory.getLogger(ClassList.class);

	@FXML
	private Title title;
	@FXML
	private Title manage;
	@FXML
	private Table table;
	@FXML
	private PaginationBar pagination;

	public ClassList() {
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
			LOG.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
	}

	/**
	 * 界面初始化
	 */
	private void initialize() {
		title.setTitle(MsgUtil.getMessage("class.title.list"));
		manage.setTitle(MsgUtil.getMessage("class.title.manage"));
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
		// 班级名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("class.name"));
		name.setPrefWidth(250);
		// 班级负责教师
		Column<String> teacher = new Column<>();
		teacher.setAlignment(Pos.CENTER);
		teacher.setKey("teacher.name");
		teacher.setText(MsgUtil.getMessage("class.teacher"));
		teacher.setPrefWidth(100);
		table.getColumns().addAll(primary, name, teacher);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", id -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new StudentList((int) id), PageLevel.Level2);
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(view);
		// 修改按钮
		Column<String> modify = new Column<String>();
		modify.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.modify"), Priority.ALWAYS, "blue-btn", id -> {
			modify((int) id);
		}));
		modify.setAlignment(Pos.CENTER_RIGHT);
		modify.setMaxWidth(58);
		modify.setMinWidth(58);
		table.getColumns().add(modify);
		// 删除按钮
		Column<String> del = new Column<String>();
		del.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), Priority.ALWAYS, "blue-btn", id -> {
			AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
				if (response == ButtonType.YES) {
					SpringUtil.getBean(ClassAction.class).deleteClassByLogic((int) id);
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

		PageInfo<Class> pageInfo = SpringUtil.getBean(ClassAction.class).findClasses(pageIndex + 1, pageSize);
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
		List<ClassInfo> infos = new ArrayList<>(); 
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
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("class.name"));
				AlertUtil.showAlert(AlertType.WARNING, reason);
				return;
			}
			String name = String.valueOf(nameObj).trim();
			if (name.length() > 100) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("class.name"), String.valueOf(100));
				AlertUtil.showAlert(AlertType.WARNING, reason);
				return;
			}
			ClassInfo info = new ClassInfo();
			info.setName(name);
			info.setTeacherCode(code);
			infos.add(info);
		}
		try {
			SpringUtil.getBean(ClassAction.class).addClasses(infos);
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.import.success"));
			pagination.reload();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	@FXML
	private void template() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setInitialFileName("班级信息模版.xls");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls"));
		File target = chooser.showSaveDialog(GUIState.getStage());
		if (target == null) {
			return;
		}
		FileUtil.copyFile(TemplateConsts.CLASS_TEMPLATE, target.getAbsolutePath(), true);
		AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.export.success"));
	}

	private void modify(int id) {
		Class clazz = SpringUtil.getBean(ClassAction.class).findClass(id);

		Dialog<Class> dialog = new Dialog<>();
		dialog.setDialogPane(new ClassModifyDialog(clazz));
		dialog.setTitle(MsgUtil.getMessage("class.dialog.modify"));
		dialog.setPrefSize(635, 320);
		dialog.showAndWait().ifPresent(obj -> {
			if (obj == null) {
				return;
			}
			try {
				SpringUtil.getBean(ClassAction.class).modifyClass(obj);
				AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.data.update.success"));
				pagination.reload();
			} catch (Exception e) {
				e.printStackTrace();
				AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
				LOG.error("修改Class对象失败，Class编号{}：{}", obj.getId(), e.getMessage());
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
