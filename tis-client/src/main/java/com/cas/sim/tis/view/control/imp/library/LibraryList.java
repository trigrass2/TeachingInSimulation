package com.cas.sim.tis.view.control.imp.library;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.consts.LibraryType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.LibraryAction;
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
		name.setMaxWidth(250);
		name.getStyleClass().add("gray-label");
		table.getColumns().addAll(primary, name);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", id -> {
			// FIXME
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(view);
		if (menuType.isEditable()) {
			// 删除按钮
			Column<String> del = new Column<String>();
			del.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), Priority.ALWAYS, "blue-btn", id -> {
				// FIXME
			}));
			del.setAlignment(Pos.CENTER_RIGHT);
			table.getColumns().add(del);
		}
	}

	private void reload() {
		int pageIndex = pagination.getPageIndex() + 1;
		int pageSize = 10;

		PageInfo<Library> pageInfo = SpringUtil.getBean(LibraryAction.class).findLibraryByType(pageIndex, pageSize, menuType.getLibraryType().getType());
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

	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		reload();
		return new Region[] { this };
	}

}
