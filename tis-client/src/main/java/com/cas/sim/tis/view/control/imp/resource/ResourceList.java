package com.cas.sim.tis.view.control.imp.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.BrowseHistoryAction;
import com.cas.sim.tis.view.action.ResourceAction;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ResourceViewer;
import com.cas.sim.tis.view.control.imp.SearchBox;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.IconCell;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.util.DateUtil;
import com.cas.util.FileUtil;
import com.cas.util.StringUtil;
import com.github.pagehelper.PageInfo;

import de.felixroske.jfxsupport.GUIState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
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
	// 我的资源列表
	@FXML
	private Table table;
	@FXML
	private PaginationBar pagination;
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
	private SearchBox search;

	// 我的资源统计
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
	@FXML
	private Label tip;

	// 上传资源
	@FXML
	private TextField filePath;
	@FXML
	private TextField keywords;
	@FXML
	private Label size;
	@FXML
	private Label show;
	@FXML
	private Label uploadTip;

	private File uploadFile;

	private List<Integer> creators = new ArrayList<>();

	/**
	 * 资源列表是否可操作
	 */
	private boolean editable;

	private ResourceAction action;

	public ResourceList(boolean editable, int... creators) {
		action = SpringUtil.getBean(ResourceAction.class);

		this.editable = editable;
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
	private void initialize() {
		createTable();
		order.selectedToggleProperty().addListener((observe, oldVal, newVal) -> {
			if (newVal == null) {
				order.selectToggle(oldVal);
			}
		});
		chart.setLegendVisible(false);
		keywords.setOnKeyReleased(event -> {
			String text = keywords.getText();
			List<String> words = StringUtil.split(text);
			show.setText(StringUtil.combine(words, ' '));
		});
		search.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				pagination.setPageIndex(0);
				reload();
			}
		});
		pagination.pageIndexProperty().addListener((b, o, n) -> {
			reload();
		});
	}

	private void createTable() {
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
		StringConverter<Integer> converter = new StringConverter<Integer>() {
			@Override
			public String toString(Integer type) {
				if (type == null) {
					return null;
				}
				ResourceType resourceType = ResourceType.getResourceType(type);
				return resourceType.getIcon();
			}

			@Override
			public Integer fromString(String string) {
				return null;
			}
		};
		icon.setCellFactory(IconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText("资源名称");// FIXME
		name.setMaxWidth(250);
		name.getStyleClass().add("gray-label");
		// 上传日期
		Column<Date> createDate = new Column<>();
		createDate.setAlignment(Pos.CENTER);
		createDate.setKey("createDate");
		createDate.setText("上传日期");
		createDate.setMaxWidth(160);
		createDate.getStyleClass().add("gray-label");
		createDate.setCellFactory(Cell.forTableColumn(new StringConverter<Date>() {

			@Override
			public String toString(Date date) {
				return DateUtil.date2Str(date, DateUtil.DATE_TIME_PAT_SHOW_);
			}

			@Override
			public Date fromString(String string) {
				return null;
			}
		}));
		table.getColumns().addAll(id, icon, name, createDate);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), "blue-btn", rid -> {
			SpringUtil.getBean(ResourceAction.class).browsed((Integer) rid);
			SpringUtil.getBean(BrowseHistoryAction.class).addBrowseHistory((Integer) rid);
			ResourceAction action = SpringUtil.getBean(ResourceAction.class);
			Resource resource = action.findResourceByID((Integer) rid);
			// 跳转到查看页面
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceViewer(resource));
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(view);
		if (editable) {
			// 删除按钮
			Column<String> delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				SpringUtil.getBean(ResourceAction.class).detele((Integer) rid);
			}));
			delete.setAlignment(Pos.CENTER);
			delete.setPrefWidth(58);
			delete.setMaxWidth(58);
			table.getColumns().add(delete);
		}
	}

	/**
	 * 加载资源
	 */
	private void loadResources() {
		int curr = pagination.getPageIndex();
		int pageSize = 10;

		String keyword = search.getText();

		String orderByClause = order.getSelectedToggle().getUserData().toString();

		PageInfo<Resource> page = null;
//		取出用户所选资源的类型
		List<Integer> types = new ArrayList<>();
		if (picCheck.isSelected()) {
			types.add(ResourceType.IMAGE.getType());
		}
		if (swfCheck.isSelected()) {
			types.add(ResourceType.SWF.getType());
		}
		if (videoCheck.isSelected()) {
			types.add(ResourceType.VIDEO.getType());
		}
		if (txtCheck.isSelected()) {
			types.add(ResourceType.TXT.getType());
		}
		if (wordCheck.isSelected()) {
			types.add(ResourceType.WORD.getType());
		}
		if (pptCheck.isSelected()) {
			types.add(ResourceType.PPT.getType());
		}
		if (excelCheck.isSelected()) {
			types.add(ResourceType.EXCEL.getType());
		}
		if (pdfCheck.isSelected()) {
			types.add(ResourceType.PDF.getType());
		}
		page = action.findResourcesByCreator(curr, pageSize, types, keyword, orderByClause, creators);
		pagination.setPageCount((int) page.getPages());
		JSONArray array = new JSONArray();
		array.addAll(page.getList());
		table.setItems(array);
		table.build();
	}

	/**
	 * 加载饼图数据
	 */
	private void loadPieChart() {
		String keyword = search.getText();
		int picNum = picCheck.isSelected() ? action.countResourceByType(ResourceType.IMAGE.getType(), keyword, creators) : 0;
		int swfNum = swfCheck.isSelected() ? action.countResourceByType(ResourceType.SWF.getType(), keyword, creators) : 0;
		int videoNum = videoCheck.isSelected() ? action.countResourceByType(ResourceType.VIDEO.getType(), keyword, creators) : 0;
		int txtNum = txtCheck.isSelected() ? action.countResourceByType(ResourceType.TXT.getType(), keyword, creators) : 0;
		int wordNum = wordCheck.isSelected() ? action.countResourceByType(ResourceType.WORD.getType(), keyword, creators) : 0;
		int pptNum = pptCheck.isSelected() ? action.countResourceByType(ResourceType.PPT.getType(), keyword, creators) : 0;
		int excelNum = excelCheck.isSelected() ? action.countResourceByType(ResourceType.EXCEL.getType(), keyword, creators) : 0;
		int pdfNum = pdfCheck.isSelected() ? action.countResourceByType(ResourceType.PDF.getType(), keyword, creators) : 0;

		ObservableList<Data> datas = FXCollections.observableArrayList(new PieChart.Data(MsgUtil.getMessage("resource.pic"), picNum), new PieChart.Data(MsgUtil.getMessage("resource.swf"), swfNum), new PieChart.Data(MsgUtil.getMessage("resource.video"), videoNum), new PieChart.Data(MsgUtil.getMessage("resource.txt"), txtNum), new PieChart.Data(MsgUtil.getMessage("resource.word"), wordNum), new PieChart.Data(MsgUtil.getMessage("resource.ppt"), pptNum), new PieChart.Data(MsgUtil.getMessage("resource.excel"), excelNum), new PieChart.Data(MsgUtil.getMessage("resource.pdf"), pdfNum));
		chart.setData(datas);

		chart.setOnMouseMoved(e -> {
			tip.setTranslateX(e.getX());
			tip.setTranslateY(e.getY());
		});
		for (final PieChart.Data data : chart.getData()) {
			Node node = data.getNode();
			node.setOnMouseEntered(e -> {
				tip.setText(data.getName() + ":" + (int) data.getPieValue());
				tip.setVisible(true);
			});
			node.setOnMouseExited(e -> {
				tip.setVisible(false);

			});
		}

		pic.setText(MsgUtil.getMessage("resource.pic") + ":" + picNum);
		swf.setText(MsgUtil.getMessage("resource.swf") + ":" + swfNum);
		video.setText(MsgUtil.getMessage("resource.video") + ":" + videoNum);
		txt.setText(MsgUtil.getMessage("resource.txt") + ":" + txtNum);
		word.setText(MsgUtil.getMessage("resource.word") + ":" + wordNum);
		ppt.setText(MsgUtil.getMessage("resource.ppt") + ":" + pptNum);
		excel.setText(MsgUtil.getMessage("resource.excel") + ":" + excelNum);
		pdf.setText(MsgUtil.getMessage("resource.pdf") + ":" + pdfNum);
	}

	@FXML
	private void typeFilter(ActionEvent event) {
		pagination.setPageIndex(0);
	}

	@FXML
	private void browse() {
		// 打开文件管理器
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.all"), "*.doc", "*.docx", "*.xls", "*.xlsx", "*.ppt", "*.pptx", "*.pdf", "*.png", "*.jpg", "*.swf", "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi", "*.txt"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.word"), ResourceType.WORD.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), ResourceType.EXCEL.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.ppt"), ResourceType.PPT.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pdf"), ResourceType.PDF.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pic"), ResourceType.IMAGE.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.swf"), ResourceType.SWF.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.video"), ResourceType.VIDEO.getSuffixs()));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.txt"), ResourceType.TXT.getSuffixs()));
		File target = chooser.showOpenDialog(GUIState.getStage());
		if (target == null) {
			return;
		}
		this.filePath.setText(target.getAbsolutePath());
		this.size.setText(getFileSize(target));
		this.uploadFile = target;
	}

	@FXML
	private void upload(ActionEvent event) {
		// 禁用上传按钮
		((Button) event.getSource()).setDisable(true);
		String filePath = uploadFile.getAbsolutePath();
		String fileName = FileUtil.getFileName(filePath);
		String ext = FileUtil.getFileExt(filePath);
		// 重命名
		String rename = UUID.randomUUID() + "." + ext;
		// 上传文件到FTP
		uploadTip.setText(MsgUtil.getMessage("ftp.upload.waiting"));
		boolean uploaded = SpringUtil.getBean(FTPUtils.class).uploadFile(ResourceConsts.FTP_RES_PATH, uploadFile, rename);
		if (!uploaded) {
			Alert alert = new Alert(AlertType.ERROR, MsgUtil.getMessage("ftp.upload.failure"));
			alert.initOwner(GUIState.getStage());
			alert.show();
			// 启用上传按钮
			((Button) event.getSource()).setDisable(false);
			return;
		}
		// 封装资源记录
		int type = ResourceType.parseType(ext);
		Resource resource = new Resource();
		resource.setKeyword(keywords.getText());
		resource.setPath(rename);
		resource.setName(fileName);
		try {
			resource.setType(type);
		} catch (Exception e) {
			LOG.warn("解析文件后缀名出现错误", e);
			throw e;
		}
		// 记录到数据库
		boolean converter = action.addResource(resource);
		if (converter) {
			Alert alert = new Alert(AlertType.INFORMATION, MsgUtil.getMessage("ftp.upload.success"));
			alert.initOwner(GUIState.getStage());
			alert.show();
		} else {
			Alert alert = new Alert(AlertType.ERROR, MsgUtil.getMessage("ftp.upload.converter.failure"));
			alert.initOwner(GUIState.getStage());
			alert.show();
		}
		// 启用上传按钮
		((Button) event.getSource()).setDisable(false);
		clear();
		reload();
	}

	@FXML
	private void orderBy() {
		pagination.setPageIndex(0);
		reload();
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
		order.getToggles().get(0).setSelected(true);

		clearUpload();
	}

	private void clearUpload() {
		filePath.setText(null);
		keywords.setText(null);
		size.setText(null);
		show.setText(null);
		uploadTip.setText(null);
	}

	private void reload() {
		loadResources();
		loadPieChart();
	}

	@Override
	public Region getContent() {
		clear();
		reload();
		return this;
	}

	public static String getFileSize(File file) {
		String size = "";
		if (file.exists() && file.isFile()) {
			long fileS = file.length();
			DecimalFormat df = new DecimalFormat("#.00");
			if (fileS < 1024) {
				size = df.format((double) fileS) + "BT";
			} else if (fileS < 1048576) {
				size = df.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824) {
				size = df.format((double) fileS / 1048576) + "MB";
			} else {
				size = df.format((double) fileS / 1073741824) + "GB";
			}
		} else if (file.exists() && file.isDirectory()) {
			size = "";
		} else {
			size = "0BT";
		}
		return size;
	}

	@Override
	public void removed() {

	}
}
