package com.cas.sim.tis.view.control.imp.preparation;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.util.FileUtil;
import com.cas.util.StringUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ResourceUploadDialog extends DialogPane<List<Integer>> {
	private Logger LOG = LoggerFactory.getLogger(ResourceUploadDialog.class);

	private TextField filePath;
	private Label size;
	private TextField keywords;
	private Label uploadTip;

	private List<File> uploadFiles = new ArrayList<>();

	private ResourceType type;

	private FileChooser chooser;

	public ResourceUploadDialog() {
		VBox box = new VBox(10);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setPadding(new Insets(20));
		box.setMaxWidth(390);

		filePath = new TextField();
		HBox.setHgrow(filePath, Priority.ALWAYS);
		filePath.setPrefHeight(40);
		filePath.setDisable(true);

		Button browse = new Button(MsgUtil.getMessage("button.browse"));
		browse.getStyleClass().add("blue-btn");
		browse.setPrefHeight(38);
		browse.setPrefWidth(80);
		browse.setOnAction(e -> {
			browse();
		});
		HBox fileBox = new HBox();
		fileBox.getChildren().addAll(filePath, browse);

		size = new Label();

		Label show = new Label();
		show.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		show.setPrefWidth(390);

		Label label = new Label(MsgUtil.getMessage("resource.keyword"));
		label.setPrefHeight(41);
		keywords = new TextField();
		keywords.setPromptText(MsgUtil.getMessage("resource.keyword.split"));
		keywords.setPrefHeight(40);
		keywords.setOnKeyReleased(event -> {
			String text = keywords.getText();
			List<String> words = StringUtil.split(text);
			show.setText(StringUtil.combine(words, ' '));
		});
		HBox.setHgrow(keywords, Priority.ALWAYS);
		HBox keyBox = new HBox(10);
		keyBox.getChildren().addAll(label, keywords);

		uploadTip = new Label();
		uploadTip.getStyleClass().add("red");

		Button upload = new Button(MsgUtil.getMessage("button.upload"));
		upload.getStyleClass().add("blue-btn");
		upload.setMinSize(390, 40);
		upload.setPrefSize(390, 40);
		upload.setOnAction(e -> {
			upload(e);
		});

		box.getChildren().addAll(fileBox, size, show, keyBox, uploadTip, upload);
		getChildren().add(box);
		setAlignment(Pos.TOP_CENTER);
	}

	public ResourceUploadDialog(ResourceType type) {
		this();
		this.type = type;
	}

	private void browse() {
		// 打开文件管理器
		chooser = new FileChooser();
		if (type == null) {
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.all"), "*.doc", "*.docx", "*.xls", "*.xlsx", "*.ppt", "*.pptx", "*.pdf", "*.png", "*.jpg", "*.swf", "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi", "*.txt"));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.word"), ResourceType.WORD.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), ResourceType.EXCEL.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.ppt"), ResourceType.PPT.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pdf"), ResourceType.PDF.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.pic"), ResourceType.IMAGE.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.swf"), ResourceType.SWF.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.video"), ResourceType.VIDEO.getSuffixs()));
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.txt"), ResourceType.TXT.getSuffixs()));
		} else {
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.all"), type.getSuffixs()));
		}
		List<File> targets = chooser.showOpenMultipleDialog(dialog.getOwner());
		if (StringUtils.isEmpty(targets)) {
			return;
		}
		this.uploadFiles = targets;
		this.filePath.setText((targets.stream().map(target -> target.getAbsolutePath())).collect(Collectors.joining(";")));
		this.size.setText((targets.stream().map(target -> FileUtil.getFileSize(target))).collect(Collectors.joining(";")));
		this.chooser.setInitialDirectory(targets.get(0).getParentFile());
	}

	private void upload(ActionEvent event) {
		// 禁用上传按钮
		((Button) event.getSource()).setDisable(true);
		uploadTip.setText(MsgUtil.getMessage("ftp.upload.waiting"));
		setCloseable(false);
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				List<String> renames = new ArrayList<>();
				List<Resource> resources = new ArrayList<>();
				int creator = Session.get(Session.KEY_LOGIN_ID);
				for (File uploadFile : uploadFiles) {
					String filePath = uploadFile.getAbsolutePath();
					String fileName = FileUtil.getFileName(filePath);
					String ext = FileUtil.getFileExt(filePath);
					// 重命名
					String rename = UUID.randomUUID() + "." + ext;
					renames.add(rename);
					// 封装资源记录
					Resource resource = new Resource();
					resource.setCreator(creator);
					resource.setKeyword(keywords.getText());
					resource.setPath(rename);
					resource.setName(fileName);
					resources.add(resource);
					try {
						if (type == null) {
							int type = ResourceType.parseType(ext);
							resource.setType(type);
						} else {
							resource.setType(type.getType());
						}
					} catch (Exception e) {
						LOG.warn("解析文件后缀名出现错误", e);
						throw e;
					}
				}
				// 上传文件到FTP
				// FIXME 批量上传
				for (int i = 0; i < uploadFiles.size(); i++) {

					try {
						FTPUtils.connect().cd(ResourceConsts.FTP_RES_PATH).uploadFile(uploadFiles.get(i), renames.get(i)).disconnect();
					} catch (Exception e) {
						uploadTip.setText(MsgUtil.getMessage("ftp.upload.failure"));
						// 启用上传按钮
						((Button) event.getSource()).setDisable(false);
						return null;
					}
				}
				// 记录到数据库
				List<Integer> ids = SpringUtil.getBean(ResourceAction.class).addResources(resources);
				Platform.runLater(() -> {
					if (ids == null) {
						uploadTip.setText(MsgUtil.getMessage("ftp.upload.converter.failure"));
						// 启用上传按钮
						((Button) event.getSource()).setDisable(false);
						setCloseable(true);
					} else {
						AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("ftp.upload.success"));
						setResult(ids);
					}
				});
				return null;
			}
		};
		new Thread(task).start();
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
