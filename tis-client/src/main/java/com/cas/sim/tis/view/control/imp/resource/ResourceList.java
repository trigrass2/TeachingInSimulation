package com.cas.sim.tis.view.control.imp.resource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.ResourceAction;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.table.BtnsCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.IconCell;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.github.pagehelper.PageInfo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleGroup;
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
	private static final Logger LOG = LoggerFactory.getLogger(ResourceAction.class);
	@FXML
	private Table table;
	@FXML
	private Pagination pagination;
	@FXML
	private ToggleGroup order;
	@FXML
	private PieChart chart;
	@FXML
	private CheckBox picCheck;
	@FXML
	private CheckBox swfCheck;
	@FXML
	private CheckBox videoCheck;
	@FXML
	private CheckBox txtCheck;
	@FXML
	private CheckBox wordCheck;
	@FXML
	private CheckBox pptCheck;
	@FXML
	private CheckBox excelCheck;
	@FXML
	private CheckBox pdfCheck;

	@FXML
	private Label pic;
	@FXML
	private Label swf;
	@FXML
	private Label video;
	@FXML
	private Label txt;
	@FXML
	private Label word;
	@FXML
	private Label ppt;
	@FXML
	private Label excel;
	@FXML
	private Label pdf;

	private List<Integer> resourceTypes = new ArrayList<>();
	private List<Integer> creators = new ArrayList<>();

	private boolean editable;

	private ResourceAction action;

	public ResourceList(boolean editable, int... creators) {
		action = SpringUtil.getBean(ResourceAction.class);

		if (creators == null) {
			LOG.warn("此处传入creator不可能为空！");
		} else {
			for (int creator : creators) {
				this.creators.add(creator);
			}
		}

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
		initialize();
	}

	/**
	 * 界面初始化
	 */
	public void initialize() {
		createTable();
		order.selectedToggleProperty().addListener((observe, oldVal, newVal) -> {
			if (newVal == null) {
				order.selectToggle(oldVal);
			}
		});
		chart.setLegendVisible(false);
	}

	protected void createTable() {
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
		name.setText("资源名称");// FIXME
		name.setMaxWidth(110);
		name.getStyleClass().add("gray-label");
		// 上传日期
		Column<String> updateDate = new Column<>();
		updateDate.setAlignment(Pos.CENTER);
		updateDate.setKey("update_date");
		updateDate.setText("上传日期");
		updateDate.setMaxWidth(160);
		updateDate.getStyleClass().add("gray-label");
		table.getColumns().addAll(id, icon, name, updateDate);
		if (editable) {
			// 编辑按钮
			Column<String> btns = new Column<String>();
			btns.setCellFactory(BtnsCell.forTableColumn());
			btns.setAlignment(Pos.CENTER_RIGHT);
			btns.setPrefWidth(145);
			table.getColumns().add(btns);
		}
	}

	private void reload() {
		clear();
		loadResources();
	}

	private void clear() {
		picCheck.setSelected(true);
		swfCheck.setSelected(true);
		videoCheck.setSelected(true);
		txtCheck.setSelected(true);
		wordCheck.setSelected(true);
		pptCheck.setSelected(true);
		excelCheck.setSelected(true);
		pdfCheck.setSelected(true);
		resourceTypes.add(0);
		resourceTypes.add(1);
		resourceTypes.add(2);
		resourceTypes.add(3);
		resourceTypes.add(4);
		resourceTypes.add(5);
		resourceTypes.add(6);
		resourceTypes.add(7);

		order.getToggles().get(0).setSelected(true);
	}

	/**
	 * 加载资源
	 */
	protected void loadResources() {
		int curr = pagination.getCurrentPageIndex();
		int pageSize = pagination.getPageCount();

		// FIXME
		String keyword = null;

		String orderByClause = order.getSelectedToggle().getUserData().toString();

		PageInfo<Resource> page = null;
		page = action.findResourcesByCreator(curr, pageSize, resourceTypes, keyword, orderByClause, creators);
		pagination.setMaxPageIndicatorCount((int) page.getTotal());
		table.setItems(new JSONArray(page.getList()));
		table.build();
	}

	/**
	 * 加载饼图数据
	 */
	protected void loadPieChart() {
		// FIXME
		String keyword = null;
		int picNum = action.countResourceByType(1, 0, resourceTypes, keyword);
		int swfNum = action.countResourceByType(1, 1, resourceTypes, keyword);
		int videoNum = action.countResourceByType(1, 2, resourceTypes, keyword);
		int txtNum = action.countResourceByType(1, 3, resourceTypes, keyword);
		int wordNum = action.countResourceByType(1, 4, resourceTypes, keyword);
		int pptNum = action.countResourceByType(1, 5, resourceTypes, keyword);
		int excelNum = action.countResourceByType(1, 6, resourceTypes, keyword);
		int pdfNum = action.countResourceByType(1, 7, resourceTypes, keyword);

		chart.setData(FXCollections.observableArrayList(new PieChart.Data(MsgUtil.getMessage("resource.pic"), picNum), new PieChart.Data(MsgUtil.getMessage("resource.swf"), swfNum), new PieChart.Data(MsgUtil.getMessage("resource.video"), videoNum), new PieChart.Data(MsgUtil.getMessage("resource.txt"), txtNum), new PieChart.Data(MsgUtil.getMessage("resource.word"), wordNum), new PieChart.Data(MsgUtil.getMessage("resource.ppt"), pptNum), new PieChart.Data(MsgUtil.getMessage("resource.excel"), excelNum), new PieChart.Data(MsgUtil.getMessage("resource.pdf"), pdfNum)));
	}

	@FXML
	private void typeFilter(ActionEvent event) {
		int type = -1;
		if (event.getSource().equals(picCheck)) {
			type = 0;
		} else if (event.getSource().equals(swfCheck)) {
			type = 1;
		} else if (event.getSource().equals(videoCheck)) {
			type = 2;
		} else if (event.getSource().equals(txtCheck)) {
			type = 3;
		} else if (event.getSource().equals(wordCheck)) {
			type = 4;
		} else if (event.getSource().equals(pptCheck)) {
			type = 5;
		} else if (event.getSource().equals(excelCheck)) {
			type = 6;
		} else if (event.getSource().equals(pdfCheck)) {
			type = 7;
		} else {
			return;
		}
		if (((CheckBox) event.getSource()).isSelected()) {
			resourceTypes.add(type);
		} else {
			resourceTypes.remove(type);
		}
		reload();
	}

	@FXML
	public void orderBy() {
		reload();
	}

	@Override
	public Region getContent() {
		reload();
		return this;
	}

}
