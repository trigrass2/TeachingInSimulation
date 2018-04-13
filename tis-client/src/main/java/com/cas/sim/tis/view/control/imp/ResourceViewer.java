package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.action.CollectionAction;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.vlc.VLCPlayer;
import com.cas.sim.tis.vo.ResourceInfo;
import com.cas.util.DateUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * 资源查看面板
 * @功能 ResourceViewer.java
 * @作者 Caowj
 * @创建日期 2018年1月23日
 * @修改人 Caowj
 */
public class ResourceViewer extends VBox implements IContent {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceViewer.class);

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
			createImageViewer();
			break;
		case SWF:
			createSWFViewer();
			break;
		case VIDEO:
			createVLCViewer();
			break;
		case TXT:
			createOfficeViewer();
			break;
		case WORD:
			createOfficeViewer();
			break;
		case PPT:
			createOfficeViewer();
			break;
		case EXCEL:
			createOfficeViewer();
			break;
		case PDF:
			createPDFViewer();
			break;
		default:
			break;
		}
	}

	/**
	 * 创建图片浏览
	 */
	private void createImageViewer() {
//		Image image = new Image("http://192.168.1.19:8082/Test/1516772514400.png");
		String url = HTTPUtils.getFullPath(ResourceConsts.FTP_RES_PATH + resource.getPath());
		if (url == null) {
			return;
		}

		Image image = new Image(url);
		ImageView imageView = new ImageView(image);

		HBox box = new HBox(imageView);
		box.setAlignment(Pos.CENTER);

		ScrollPane scrollPane = new ScrollPane(box);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setPannable(true);

		double width = image.getWidth();
		double height = image.getHeight();
		DoubleProperty zoomProperty = new SimpleDoubleProperty(1);
		zoomProperty.addListener(observable -> {
			if (height * zoomProperty.get() > viewer.getHeight()) {
				return;
			}
			imageView.setFitWidth(width * zoomProperty.get());
			imageView.setFitHeight(height * zoomProperty.get());
		});
		scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double delta = event.getDeltaY() / 100;
				double zoom = zoomProperty.get();
				double zoomDelta = zoom + delta;
				if (zoomDelta < 0.5) {
					zoomProperty.set(0.5);
				} else if (zoomDelta > 2) {
					zoomProperty.set(2);
				} else {
					zoomProperty.set(zoomDelta);
				}
			}
		});
		viewer.getChildren().add(scrollPane);
	}

	/**
	 * 创建文档文件查看器
	 */
	private void createOfficeViewer() {
		String officeName = resource.getPath();
		String pdfName = officeName.substring(0, officeName.lastIndexOf(".")) + ".pdf";
		String pdfPath = HTTPUtils.getFullPath(ResourceConsts.FTP_CONVERT_PATH + pdfName);
		if (pdfPath == null) {
			return;
		}
		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
		// 禁止下载文件
		browser.setDownloadHandler(new DownloadHandler() {
			@Override
			public boolean allowDownload(DownloadItem download) {
				return false;
			}
		});
		browser.loadURL(pdfPath);
		BrowserView view = new BrowserView(browser);
		viewer.getChildren().add(view);
	}

	/**
	 * 创建PDF查看器
	 */
	private void createPDFViewer() {
		String url = HTTPUtils.getFullPath(ResourceConsts.FTP_RES_PATH + resource.getPath());
		if (url == null) {
			return;
		}

		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
		// 禁止下载文件
		browser.setDownloadHandler(new DownloadHandler() {
			@Override
			public boolean allowDownload(DownloadItem download) {
				return false;
			}
		});
		browser.loadURL(url);
		BrowserView view = new BrowserView(browser);
		viewer.getChildren().add(view);
	}

	/**
	 * 创建视频查看器
	 */
	private void createVLCViewer() {
		String url = HTTPUtils.getFullPath(ResourceConsts.FTP_RES_PATH + resource.getPath());
		if (url == null) {
			return;
		}

		VLCPlayer player = new VLCPlayer();
		player.loadVideo(url);
		viewer.getChildren().add(player);
//		player.loadVideo("http://192.168.1.19:8082/Test/Mux140928003405.avi");
//		player.loadVideo("http://192.168.1.19:8082/resources/4dae3b67-1d55-4125-a577-4086585464c1.mp4");
	}

	/**
	 * 创建FLASH查看器
	 */
	private void createSWFViewer() {
		String url = HTTPUtils.getFullPath(ResourceConsts.FTP_RES_PATH + resource.getPath());
		if (url == null) {
			return;
		}

		SWFBrowser browser = new SWFBrowser(BrowserType.LIGHTWEIGHT);
		browser.loadHTML(url);
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
			this.collectedBtn.setGraphic(new SVGGlyph("iconfont.svg.collected", Color.web("#ff9e2c"), 25));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.done"));
		} else {
			this.collectedBtn.setGraphic(new SVGGlyph("iconfont.svg.uncollect", Color.web("#ff9e2c"), 25));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.todo"));
		}
	}

	@FXML
	private void collect() {
		Integer rid = resource.getId();
		if (collected) {
			this.collectedBtn.setGraphic(new SVGGlyph("iconfont.svg.uncollect", Color.web("#ff9e2c"), 25));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.todo"));
			SpringUtil.getBean(ResourceAction.class).uncollect(rid);
			collected = false;
		} else {
			this.collectedBtn.setGraphic(new SVGGlyph("iconfont.svg.collected", Color.web("#ff9e2c"), 25));
			this.collectedBtn.setText(MsgUtil.getMessage("resource.collection.done"));
			SpringUtil.getBean(ResourceAction.class).collected(rid);
			collected = true;
		}
	}

	@Override
	public Region[] getContent() {
		initialize();
		return new Region[] { this };
	}

	@Override
	public void distroy() {
		for (Node child : viewer.getChildren()) {
			if (child instanceof IDistory) {
				((IDistory) child).distroy();
			} else if (child instanceof BrowserView) {
				((BrowserView) child).getBrowser().dispose();
			}
		}
	}
}
