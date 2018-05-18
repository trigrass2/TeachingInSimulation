package com.cas.sim.tis.view.control.imp.dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.svg.SVGGlyph;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Tip extends HBox {
	private static final Logger LOG = LoggerFactory.getLogger(Tip.class);
	@FXML
	private Label tip;
	@FXML
	private Button close;

	public enum TipType {
		INFO, WARN, ERROR
	}

	private Timeline timeline = new Timeline();
	private TipDialog dialog;

	public Tip() {
		initTip(TipType.INFO, "");
	}

	public Tip(@Nonnull TipType type) {
		initTip(type, "");
	}

	public Tip(@Nonnull TipType type, String msg) {
		initTip(type, msg);
	}

	public void initTip(TipType type, String msg) {
		loadFXML();
		// 图标
		switch (type) {
		case INFO:
			tip.setGraphic(new SVGGlyph("iconfont.svg.information", Color.WHITE, 32));
			break;
		case WARN:
			tip.setGraphic(new SVGGlyph("iconfont.svg.warning", Color.WHITE, 32));
			break;
		case ERROR:
			tip.setGraphic(new SVGGlyph("iconfont.svg.error", Color.WHITE, 32));
			break;
		default:
			break;
		}
		// 提示信息
		tip.setText(msg);
		tip.setGraphicTextGap(10);
		tip.setMinWidth(msg.length() * 14 + 40);
		// 显示3秒后自动关闭
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3)));
		timeline.setOnFinished(e -> {
			close();
		});
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Tip.fxml");
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

	public void setDialog(TipDialog dialog) {
		this.dialog = dialog;
		// 显示开始计时
		this.dialog.setOnShown(e -> {
			timeline.playFromStart();
		});
	}

	public void setTip(String msg) {
		tip.setText(msg);
	}

	@FXML
	private void close() {
		if (dialog != null) {
			dialog.close();
		}
	}
}
