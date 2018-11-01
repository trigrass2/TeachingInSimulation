package com.cas.sim.tis.view.control.imp.library;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.consts.PublishType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.question.StudentQuestionPaper;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.util.DateUtil;
import com.github.pagehelper.PageInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;

/**
 * 教师发布的考核记录
 * @功能 PublishList.java
 * @作者 Caowj
 * @创建日期 2018年2月9日
 * @修改人 Caowj
 */
public class PublishListForStudent extends HBox implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(PublishListForStudent.class);
	
//	@FXML
//	private Title title;
	@FXML
	private HBox option;
	@FXML
	private Table table;
	@FXML
	private PaginationBar pagination;

	private PublishType type;

	public PublishListForStudent(PublishType type) {
		this.type = type;
		loadFXML();
		initialize();
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
//		this.title.setTitle(type.getTitle());
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
		name.setPrefWidth(150);
		// 得分
		Column<String> score = new Column<>();
		score.setAlignment(Pos.CENTER);
		score.setKey("score");
		score.setText(MsgUtil.getMessage("exam.score"));
		score.setPrefWidth(50);
		// 用时
		Column<String> time = new Column<>();
		time.setAlignment(Pos.CENTER);
		time.setKey("time");
		time.setText(MsgUtil.getMessage("exam.time"));
		time.setPrefWidth(80);
		// 日期
		Column<Date> date = new Column<>();
		date.setAlignment(Pos.CENTER);
		date.setKey("date");
		date.setText(MsgUtil.getMessage("exam.date"));
		date.setPrefWidth(120);
		date.setCellFactory(Cell.forTableColumn(new StringConverter<Date>() {

			@Override
			public String toString(Date date) {
				return DateUtil.date2Str(date, DateUtil.DATE_TIME_PAT_SHOW_);
			}

			@Override
			public Date fromString(String string) {
				return null;
			}
		}));
		table.getColumns().addAll(primary, name, score, time, date);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", id -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new StudentQuestionPaper((int) id, type), PageLevel.Level2);
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(view);
	}

	private void reload(Integer pageIndex) {
		int pageSize = 10;

		PageInfo<LibraryPublishForStudent> pageInfo = SpringUtil.getBean(LibraryPublishAction.class).findPublishForStudent(pageIndex + 1, pageSize, type.getType(), Session.get(Session.KEY_LOGIN_ID));
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

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		pagination.reload();
		return new Region[] { this };
	}

	@Override
	public void onContentAttached(PageController pageController) {
		pageController.setTitleName(type.getTitle());
	}

}
