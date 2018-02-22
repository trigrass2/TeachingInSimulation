package com.cas.sim.tis.view.control.imp.info;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.UserAction;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.github.pagehelper.PageInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class StudentList extends HBox implements IContent {
	private static final Logger LOG = LoggerFactory.getLogger(StudentList.class);

	@FXML
	private Title title;
	@FXML
	private Table table;
	@FXML
	private PaginationBar pagination;
	
	public StudentList() {
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
		title.setTitle(MsgUtil.getMessage("student.title.list"));
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
		// 学生学号
		Column<String> code = new Column<>();
		code.setAlignment(Pos.CENTER_LEFT);
		code.setKey("code");
		code.setText(MsgUtil.getMessage("student.code"));
		code.setPrefWidth(250);
		// 学生名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("student.name"));
		name.setPrefWidth(250);
		table.getColumns().addAll(primary, code, name);
		Column<String> modify = new Column<String>();
		modify.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.modify"), Priority.ALWAYS, "blue-btn", id -> {
			modify((int) id);
		}));
		modify.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(modify);
	}

	private void reload(Integer pageIndex) {
		int pageSize = 10;

		PageInfo<User> pageInfo = SpringUtil.getBean(UserAction.class).findUsersByRole(pageIndex + 1, pageSize, RoleConst.STUDENT);
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
	private void add() {
//		Dialog<Library> dialog = new Dialog<>();
////		dialog.setDialogPane(new LibraryAddDialog(menuType.getLibraryType()));
//		dialog.setPrefSize(635, 320);
//		dialog.showAndWait().ifPresent(library -> {
//			if (library == null) {
//				return;
//			}
//			try {
//				SpringUtil.getBean(LibraryAction.class).addLibrary(library);
//				Alert alert = new Alert(AlertType.INFORMATION, MsgUtil.getMessage("data.add.success"));
//				alert.initOwner(GUIState.getStage());
//				alert.show();
//				pagination.reload();
//			} catch (Exception e) {
//				e.printStackTrace();
//				Alert alert = new Alert(AlertType.ERROR, e.getMessage());
//				alert.initOwner(GUIState.getStage());
//				alert.show();
//			}
//		});
	}

	private void modify(int id) {
//		Library library = SpringUtil.getBean(LibraryAction.class).findLibraryByID(id);
//
//		Dialog<Library> dialog = new Dialog<>();
//		dialog.setDialogPane(new LibraryModifyDialog(library));
//		dialog.setTitle(MsgUtil.getMessage("library.name"));
//		dialog.setPrefSize(635, 320);
//		dialog.showAndWait().ifPresent(lib -> {
//			if (lib == null) {
//				return;
//			}
//			try {
//				SpringUtil.getBean(LibraryAction.class).modifyLibrary(lib);
//				Alert alert = new Alert(AlertType.INFORMATION, MsgUtil.getMessage("data.update.success"));
//				alert.initOwner(GUIState.getStage());
//				alert.show();
//				pagination.reload();
//			} catch (Exception e) {
//				e.printStackTrace();
//				Alert alert = new Alert(AlertType.ERROR, e.getMessage());
//				alert.initOwner(GUIState.getStage());
//				alert.show();
//			}
//		});
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
