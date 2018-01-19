package com.cas.sim.tis.view.control.imp.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.IconCell;
import com.cas.sim.tis.view.control.imp.table.Table;

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
@PropertySource(value = { "file:cfg.properties" })
public abstract class ResourceList extends HBox implements IContent {
	@Value("${server.base.address}")
	private String address;

	@Value("${server.rmi.registry}")
	private Integer rmiPort;

	@FXML
	protected Table table;
	@FXML
	protected Pagination pagination;
	@FXML
	protected ToggleGroup order;
	@FXML
	protected PieChart chart;
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

	protected List<Integer> resourceTypes = new ArrayList<>();

	protected ResourceService service;

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
		initialize();
	}

	/**
	 * 界面初始化
	 */
	public void initialize() {
		try {
			String serviceUrl = "rmi://" + address + ":" + rmiPort + "/" + "resourceService";
			service = (ResourceService) Naming.lookup(serviceUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
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
	protected abstract void loadResources(); 

	/**
	 * 加载饼图数据
	 */
	protected abstract void loadPieChart(); 

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
