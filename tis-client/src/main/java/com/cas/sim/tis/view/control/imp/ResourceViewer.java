package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.CollectionAction;
import com.cas.sim.tis.view.action.ResourceAction;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.vlc.VLCPlayer;
import com.cas.sim.tis.vo.ResourceInfo;
import com.cas.util.DateUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.Refine;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * 资源查看面板
 * @功能 ResourceViewer.java
 * @作者 Caowj
 * @创建日期 2018年1月23日
 * @修改人 Caowj
 */
public class ResourceViewer extends VBox implements IContent {
	@FXML
	private StackPane viewer;
	@FXML
	private Label creator;
	@FXML
	private Label createDate;
	@FXML
	private Label browsedTimes;
	@FXML
	private Label collectedTimes;
	@FXML
	private Button collectedBtn;
	/**
	 * 目标资源
	 */
	private Resource resource;
	/**
	 * 资源收藏状态
	 */
	private boolean collected;

	/**
	 * 资源查看面板
	 * @param resource 目标资源
	 */
	public ResourceViewer(Resource resource) {
		this.resource = resource;
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/resource/Viewer.fxml");
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
		createViewer();
		loadResourceInfo();
	}

	/**
	 * 创建资源查看器
	 */
	private void createViewer() {
		int type = resource.getType();
		ResourceType resourceType = ResourceType.getResourceType(type);
		switch (resourceType) {
		case IMAGE:
			createXdocViewer();
			break;
		case SWF:
			createSWFViewer();
			break;
		case VIDEO:
			createVLCViewer();
			break;
		case TXT:
			createXdocViewer();
			break;
		case WORD:
			createXdocViewer();
			break;
		case PPT:
			createXdocViewer();
			break;
		case EXCEL:
			createXdocViewer();
			break;
		case PDF:
			createPDFViewer();
			break;
		default:
			break;
		}
	}

	/**
	 * 创建文档文件查看器
	 */
	private void createXdocViewer() {
	}

	/**
	 * 创建视频查看器
	 */
	private void createVLCViewer() {
		VLCPlayer player = SpringUtil.getBean(VLCPlayer.class);
		viewer.getChildren().add(player);
		HTTPUtils utils = SpringUtil.getBean(HTTPUtils.class);
		player.loadVideo(utils.getHttpUrl(resource.getPath()));
	}

	/**
	 * 创建PDF查看器
	 */
	private void createPDFViewer() {
		Refine.init();
		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
		// FIXME
		browser.loadURL("http://192.168.1.19:8082/Test/Fanuc0i参数说明书.pdf");
	    BrowserView view = new BrowserView(browser);
		viewer.getChildren().add(view);
	}

	/**
	 * 创建FLASH查看器
	 */
	private void createSWFViewer() {
		Refine.init();
		SWFBrowser browser = new SWFBrowser(BrowserType.LIGHTWEIGHT);
		// FIXME
		browser.loadHTML("http://192.168.1.19:8082/Test/teachResources.swf");
		BrowserView view = new BrowserView(browser);
		viewer.getChildren().add(view);
	}

	/**
	 * 加载资源信息
	 */
	private void loadResourceInfo() {
		ResourceInfo info = SpringUtil.getBean(ResourceAction.class).findResourceInfoByID(resource.getId());
		if (info != null) {
			this.creator.setText(MsgUtil.getMessage("resource.creator", info.getCreator()));
			this.createDate.setText(MsgUtil.getMessage("resource.create.date", DateUtil.date2Str(info.getCreateDate(), DateUtil.DATE_SHT_PAT_10_)));
			this.browsedTimes.setText(MsgUtil.getMessage("resource.browse.times", String.valueOf(info.getBrowsedTimes())));
			this.collectedTimes.setText(MsgUtil.getMessage("resource.collection.times", String.valueOf(info.getCollectedTimes())));
		}
		collected = SpringUtil.getBean(CollectionAction.class).checkCollected(resource.getId());
		if (collected) {
			this.collectedBtn.setGraphic(new ImageView("/static/images/resource/collected.png"));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.done"));
		} else {
			this.collectedBtn.setGraphic(new ImageView("/static/images/resource/uncollect.png"));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.todo"));
		}
	}

	@FXML
	private void collect() {
		Integer rid = resource.getId();
		if (collected) {
			this.collectedBtn.setGraphic(new ImageView("/static/images/resource/uncollect.png"));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.todo"));
			SpringUtil.getBean(CollectionAction.class).uncollect(rid);
			collected = false;
		} else {
			this.collectedBtn.setGraphic(new ImageView("/static/images/resource/collected.png"));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.done"));
			SpringUtil.getBean(CollectionAction.class).collected(rid);
			collected = true;
		}
	}

	@Override
	public Region getContent() {
		initialize();
		return this;
	}

	@Override
	public void removed() {
		for (Node child : viewer.getChildren()) {
			if (child instanceof IDistory) {
				((IDistory) child).distroy();
			}
		}
	}
}
