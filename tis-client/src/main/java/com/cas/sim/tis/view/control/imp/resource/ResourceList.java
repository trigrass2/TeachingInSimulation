package com.cas.sim.tis.view.control.imp.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileSystemView;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
	private File uploadFile;

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
		keywords.setOnKeyReleased(event -> {
			String text = keywords.getText();
			List<String> words = StringUtil.split(text);
			show.setText(StringUtil.combine(words, ' '));
		});
	}

	private void createTable() {
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

	/**
	 * 加载资源
	 */
	private void loadResources() {
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
	private void loadPieChart() {
		// FIXME
		String keyword = null;
//		int picNum = action.countResourceByType(1, 0, resourceTypes, keyword);
//		int swfNum = action.countResourceByType(1, 1, resourceTypes, keyword);
//		int videoNum = action.countResourceByType(1, 2, resourceTypes, keyword);
//		int txtNum = action.countResourceByType(1, 3, resourceTypes, keyword);
//		int wordNum = action.countResourceByType(1, 4, resourceTypes, keyword);
//		int pptNum = action.countResourceByType(1, 5, resourceTypes, keyword);
//		int excelNum = action.countResourceByType(1, 6, resourceTypes, keyword);
//		int pdfNum = action.countResourceByType(1, 7, resourceTypes, keyword);
		int picNum = 17;
		int swfNum = 12;
		int videoNum = 20;
		int txtNum = 10;
		int wordNum = 13;
		int pptNum = 24;
		int excelNum = 21;
		int pdfNum = 15;

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
	private void browse() {
		// 打开文件管理器
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.all"), "*.doc", "*.docx", "*.xls", "*.xlsx", "*.ppt", "*.pptx", "*.pdf", "*.png", "*.jpg", "*.swf", "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi", "*.txt"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.word"), "*.doc", "*.docx"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls", "*.xlsx"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.ppt"), "*.ppt", "*.pptx"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pdf"), "*.pdf"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pic"), "*.png", "*.jpg"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.swf"), "*.swf"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.video"), "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi"));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.txt"), "*.txt"));
		File target = chooser.showOpenDialog(GUIState.getStage().getOwner());
		if (target == null) {
			return;
		}
		this.filePath.setText(target.getAbsolutePath());
		this.size.setText(getFileSize(target));
		this.uploadFile = target;
	}

	@FXML
	private void upload() {
		String filePath = uploadFile.getAbsolutePath();
		String fileName = FileUtil.getFileName(filePath);
		String ext = FileUtil.getFileExt(filePath);
		// 重命名
		String rename = System.currentTimeMillis() + "." + ext;
		// 上传文件到FTP
//		try {
			// FIXME
			boolean uploaded = true;
//			boolean uploaded = FTPUtils.getInstance().uploadFile("192.168.1.125", ResourceConsts.FTP_RES_PATH, rename, new FileInputStream(uploadFile));
			if (!uploaded) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(GUIState.getStage().getOwner());
				alert.setContentText(MsgUtil.getMessage("ftp.upload.failure"));
				alert.show();
				return;
			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		// 封装资源记录
		Resource resource = new Resource();
		resource.setKeyword(keywords.getText());
		resource.setPath(ResourceConsts.FTP_RES_PATH + rename);
		resource.setName(fileName);
		if (ext.equals("png") || ext.equals("jpg")) {
			resource.setType(0);
		} else if (ext.equals("swf")) {
			resource.setType(1);
		} else if (ext.equals("mp4") || ext.equals("wmv") || ext.equals("rmvb") || ext.equals("flv") || ext.equals("avi")) {
			resource.setType(2);
		} else if (ext.equals("txt")) {
			resource.setType(3);
		} else if (ext.equals("doc") || ext.equals("docx")) {
			resource.setType(4);
		} else if (ext.equals("ppt") || ext.equals("pptx")) {
			resource.setType(5);
		} else if (ext.equals("xls") || ext.equals("xlsx")) {
			resource.setType(6);
		} else if (ext.equals("pdf")) {
			resource.setType(7);
		}
		// TODO 记录到数据库
	}

	@FXML
	private void orderBy() {
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

	private void reload() {
		clear();
		// loadResources();
		loadPieChart();
	}

	@Override
	public Region getContent() {
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
}
