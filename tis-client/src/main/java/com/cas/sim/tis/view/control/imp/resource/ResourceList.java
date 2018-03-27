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
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ResourceViewer;
import com.cas.sim.tis.view.control.imp.SearchBox;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.SVGIconCell;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
	private static final Logger LOG = LoggerFactory.getLogger(ResourceList.class);

	public enum ResourceMenuType {
		ADMIN_SYS("resource.title.system", "resource.upload.date", true), //
		TEACHER_SYS("resource.title.system", "resource.upload.date", false), //
		TEACHER_MINE("resource.title.mine", "resource.upload.date", true), //
		STUDENT_SYS("resource.title.system", "resource.upload.date", false), //
		STUDENT_TECH("resource.title.teacher", "resource.upload.date", false), //
		BROWSE("resource.title.history", "resource.browsed.date", false), //
		COLLECTION("resource.title.collect", "resource.collected.date", false);

		private String title;
		private String dateLabel;
		private boolean editable;

		private ResourceMenuType(String key, String dateKey, boolean editable) {
			this.title = MsgUtil.getMessage(key);
			this.dateLabel = MsgUtil.getMessage(dateKey);
			this.editable = editable;
		}

		public boolean isEditable() {
			return editable;
		}

		public String getTitle() {
			return title;
		}

		public String getDateLabel() {
			return dateLabel;
		}
	}

	// 我的资源列表
	@FXML
	private Title title;
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
	private VBox uploadPane;
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

	private int creator;

	/**
	 * 资源列表是否可操作
	 */
	private ResourceMenuType type;

	private ResourceAction action;
	
	private FileChooser chooser;

	public ResourceList(ResourceMenuType type, int creator) {
		action = SpringUtil.getBean(ResourceAction.class);

		this.type = type;
		this.creator = creator;

		loadFXML();
		initialize();
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/resource/List.fxml");
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
		if (!type.isEditable()) {
			uploadPane.setVisible(false);
		}
		title.setTitle(type.getTitle());
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
		search.setOnSearch(text -> {
			pagination.reload();
		});
		pagination.setContent(pageIndex -> {
			reload(pageIndex);
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
		Function<Integer, SVGGlyph> converter = new Function<Integer, SVGGlyph>() {

			@Override
			public SVGGlyph apply(Integer type) {
				if (type == null) {
					return null;
				}
				ResourceType resourceType = ResourceType.getResourceType(type);
				return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22);
			}
		};
		icon.setCellFactory(SVGIconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("resource.name"));
		name.setMaxWidth(250);
		// 日期
		Column<Date> date = new Column<>();
		date.setAlignment(Pos.CENTER);
		date.setKey("createDate");
		date.setText(type.getDateLabel());
		date.setMaxWidth(160);
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
		table.getColumns().addAll(id, icon, name, date);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", rid -> {
			SpringUtil.getBean(ResourceAction.class).browsed((Integer) rid);
			ResourceAction action = SpringUtil.getBean(ResourceAction.class);
			Resource resource = action.findResourceByID((Integer) rid);
			// 跳转到查看页面
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceViewer(resource), PageLevel.Level2);
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().add(view);
		if (type.isEditable()) {
			// 删除按钮
			Column<String> delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
					if (response == ButtonType.YES) {
						SpringUtil.getBean(ResourceAction.class).detele((Integer) rid);
						pagination.reload();
					}
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			table.getColumns().add(delete);
		}
	}

	/**
	 * 加载资源
	 */
	private void loadResources(Integer pageIndex) {
		int curr = pageIndex + 1;
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
		page = action.findResources(type, curr, pageSize, types, keyword, orderByClause, creator);
		if (page == null) {
			pagination.setPageCount(0);
			table.setItems(null);
			table.build();
		} else {
			pagination.setPageCount((int) page.getPages());
			JSONArray array = new JSONArray();
			array.addAll(page.getList());
			table.setItems(array);
			table.build();
		}
	}

	/**
	 * 加载饼图数据
	 */
	private void loadPieChart() {
		String keyword = search.getText();
		int picNum = picCheck.isSelected() ? action.countResourceByType(type, ResourceType.IMAGE.getType(), keyword, creator) : 0;
		int swfNum = swfCheck.isSelected() ? action.countResourceByType(type, ResourceType.SWF.getType(), keyword, creator) : 0;
		int videoNum = videoCheck.isSelected() ? action.countResourceByType(type, ResourceType.VIDEO.getType(), keyword, creator) : 0;
		int txtNum = txtCheck.isSelected() ? action.countResourceByType(type, ResourceType.TXT.getType(), keyword, creator) : 0;
		int wordNum = wordCheck.isSelected() ? action.countResourceByType(type, ResourceType.WORD.getType(), keyword, creator) : 0;
		int pptNum = pptCheck.isSelected() ? action.countResourceByType(type, ResourceType.PPT.getType(), keyword, creator) : 0;
		int excelNum = excelCheck.isSelected() ? action.countResourceByType(type, ResourceType.EXCEL.getType(), keyword, creator) : 0;
		int pdfNum = pdfCheck.isSelected() ? action.countResourceByType(type, ResourceType.PDF.getType(), keyword, creator) : 0;

		ObservableList<Data> datas = FXCollections.observableArrayList(new PieChart.Data(MsgUtil.getMessage("resource.pdf"), pdfNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.ppt"), pptNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.txt"), txtNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.excel"), excelNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.pic"), picNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.video"), videoNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.word"), wordNum), //
				new PieChart.Data(MsgUtil.getMessage("resource.swf"), swfNum)); //
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
	private void typeFilter() {
		pagination.reload();
	}

	@FXML
	private void orderBy() {
		pagination.reload();
	}

	@FXML
	private void browse() {
		// 打开文件管理器
		if (chooser == null) {
			chooser = new FileChooser();
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.all"), "*.doc", "*.docx", "*.xls", "*.xlsx", "*.ppt", "*.pptx", "*.pdf", "*.png", "*.jpg", "*.swf", "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi", "*.txt"));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.word"), ResourceType.WORD.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), ResourceType.EXCEL.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.ppt"), ResourceType.PPT.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pdf"), ResourceType.PDF.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pic"), ResourceType.IMAGE.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.swf"), ResourceType.SWF.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.video"), ResourceType.VIDEO.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.txt"), ResourceType.TXT.getSuffixs()));
		}
		File target = chooser.showOpenDialog(GUIState.getStage());
		if (target == null) {
			return;
		}
		this.filePath.setText(target.getAbsolutePath());
		this.size.setText(getFileSize(target));
		this.uploadFile = target;
		this.chooser.setInitialDirectory(target.getParentFile());
	}

	@FXML
	private void upload(ActionEvent event) {
		// 禁用上传按钮
		((Button) event.getSource()).setDisable(true);
		uploadTip.setText(MsgUtil.getMessage("ftp.upload.waiting"));
		String filePath = uploadFile.getAbsolutePath();
		String fileName = FileUtil.getFileName(filePath);
		String ext = FileUtil.getFileExt(filePath);
		// 重命名
		String rename = UUID.randomUUID() + "." + ext;
		// 上传文件到FTP
		boolean uploaded = SpringUtil.getBean(FTPUtils.class).uploadFile(ResourceConsts.FTP_RES_PATH, uploadFile, rename);
		if (!uploaded) {
			AlertUtil.showAlert(AlertType.ERROR, MsgUtil.getMessage("ftp.upload.failure"));
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
		Integer id = action.addResource(resource);
		if (id != null) {
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("ftp.upload.success"));
		} else {
			AlertUtil.showAlert(AlertType.ERROR, MsgUtil.getMessage("ftp.upload.converter.failure"));
		}
		// 启用上传按钮
		((Button) event.getSource()).setDisable(false);
		clear();
		pagination.reload();
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

	private void reload(Integer pageIndex) {
		loadResources(pageIndex);
		loadPieChart();
	}

	@Override
	public Region[] getContent() {
		clear();
		pagination.reload();
		return new Region[] { this };
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
	public void distroy() {

	}
}
