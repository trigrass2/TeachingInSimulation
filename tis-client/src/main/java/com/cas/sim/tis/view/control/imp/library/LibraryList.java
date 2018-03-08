package com.cas.sim.tis.view.control.imp.library;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.LibraryAction;
import com.cas.sim.tis.consts.LibraryType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.question.PreviewQuestionPaper;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
import com.github.pagehelper.PageInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class LibraryList extends HBox implements IContent {
	private static final Logger LOG = LoggerFactory.getLogger(LibraryList.class);

	public enum LibraryMenuType {
		ADMIN_MOCK(LibraryType.MOCK, true), //
		ADMIN_OLD(LibraryType.OLD, true), //

		TEACHER_MOCK(LibraryType.MOCK, false), //
		TEACHER_OLD(LibraryType.OLD, false), //
		TEACHER_MINE(LibraryType.TEACHERS, true), //

		STUDENT_MOCK(LibraryType.MOCK, false), //
		STUDENT_OLD(LibraryType.OLD, false), //
		STUDENT_MINE(LibraryType.TEACHERS, false);

		private LibraryType libraryType;
		private boolean editable;

		private LibraryMenuType(LibraryType libraryType, boolean editable) {
			this.libraryType = libraryType;
			this.editable = editable;
		}

		public LibraryType getLibraryType() {
			return libraryType;
		}

		public boolean isEditable() {
			return editable;
		}

	}

	private LibraryMenuType menuType;

	@FXML
	private Title title;
	@FXML
	private HBox option;
	@FXML
	private Table table;
	@FXML
	private PaginationBar pagination;

	public LibraryList(LibraryMenuType menuType) {
		this.menuType = menuType;

		loadFXML();

		initialize();
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/library/List.fxml");
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
		this.title.setTitle(menuType.getLibraryType().getKey());
		this.option.setVisible(menuType.isEditable());
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
		// 题库名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("library.name"));
		name.setPrefWidth(250);
		table.getColumns().addAll(primary, name);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", id -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new PreviewQuestionPaper((Integer) id, menuType.isEditable()), PageLevel.Level2);
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(view);
		if (menuType.isEditable()) {
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
				AlertUtil.showConfirm(MsgUtil.getMessage("table.delete"), response -> {
					if (response == ButtonType.YES) {
						SpringUtil.getBean(LibraryAction.class).deleteLibrary((int) id);
						pagination.reload();
					}
				});
			}));
			del.setAlignment(Pos.CENTER_RIGHT);
			del.setMaxWidth(58);
			del.setMinWidth(58);
			table.getColumns().add(del);
		}
	}

	private void reload(Integer pageIndex) {
		int pageSize = 10;

		PageInfo<Library> pageInfo = SpringUtil.getBean(LibraryAction.class).findLibraryByType(pageIndex + 1, pageSize, menuType.getLibraryType().getType());
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
		Dialog<Library> dialog = new Dialog<>();
		dialog.setDialogPane(new LibraryAddDialog(menuType.getLibraryType()));
		dialog.setTitle(MsgUtil.getMessage("library.name"));
		dialog.setPrefSize(635, 320);
		dialog.showAndWait().ifPresent(library -> {
			if (library == null) {
				return;
			}
			try {
				SpringUtil.getBean(LibraryAction.class).addLibrary(library);
				AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("data.add.success"));
				pagination.reload();
			} catch (Exception e) {
				e.printStackTrace();
				AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
			}
		});
	}

	private void modify(int id) {
		Library library = SpringUtil.getBean(LibraryAction.class).findLibraryByID(id);

		Dialog<Library> dialog = new Dialog<>();
		dialog.setDialogPane(new LibraryModifyDialog(library));
		dialog.setTitle(MsgUtil.getMessage("library.name"));
		dialog.setPrefSize(635, 320);
		dialog.showAndWait().ifPresent(lib -> {
			if (lib == null) {
				return;
			}
			try {
				SpringUtil.getBean(LibraryAction.class).modifyLibrary(lib);
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
