package com.cas.sim.tis.view.control.imp.setting;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.action.UserAction;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.consts.SettingConsts;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SettingDialog extends DialogPane<Boolean> {

	private static final Logger LOG = LoggerFactory.getLogger(SettingDialog.class);

	private static List<String> languages = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			add(Locale.CHINA.toString());
			add(Locale.US.toString());
		}
	};

	@FXML
	private TextField oldPwd;
	@FXML
	private TextField newPwd;
	@FXML
	private TextField pwdAgain;
	@FXML
	private ComboBox<LangItem> language;
	@FXML
	private ComboBox<Resolution> resolution;
	@FXML
	private RadioButton win;
	@FXML
	private RadioButton full;

	public SettingDialog() {
		loadFXML();
		initialize();
	}

	private void loadFXML() {
		VBox box = new VBox();
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Setting.fxml");
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
		getChildren().add(box);
	}

	private void initialize() {
		initLanguageBomboBox();
		initResolution();
	}

	static class LangItem {
		private String lang;
		private String desc;

		public LangItem(String lang, String desc) {
			this.lang = lang;
			this.desc = desc;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((desc == null) ? 0 : desc.hashCode());
			result = prime * result + ((lang == null) ? 0 : lang.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			LangItem other = (LangItem) obj;
			if (desc == null) {
				if (other.desc != null) return false;
			} else if (!desc.equals(other.desc)) return false;
			if (lang == null) {
				if (other.lang != null) return false;
			} else if (!lang.equals(other.lang)) return false;
			return true;
		}

		@Override
		public String toString() {
			return desc;
		}

		public String getLang() {
			return lang;
		}
	}

	static class Resolution {
		private int width;
		private int height;

		public Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		@Override
		public String toString() {
			return width + " × " + height;
		}
	}

	private void initLanguageBomboBox() {
		for (String language : languages) {
//			lang = zh,en...
			String langDesc = MsgUtil.getMessage(language);
//			langDesc = "简体中文","English"...
//			在setting.properties中配置
			this.language.getItems().add(new LangItem(language, langDesc));
		}
//		TODO 获取注册表中的设置信息
		Preferences prefs = Preferences.userRoot().node(SettingConsts.REG_APP_PATH);
//		设置语言下拉框的默认值
//		userLang = zh
		String userLang = prefs.get(SettingConsts.LANGUAGE, Locale.CHINA.toString());

		String userLangDesc = MsgUtil.getMessage(userLang);
		language.getSelectionModel().select(new LangItem(userLang, userLangDesc));
	}

	private void initResolution() {
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = g.getDefaultScreenDevice();
		DisplayMode[] modes = sd.getDisplayModes();
		List<Resolution> resolutions = new ArrayList<Resolution>();
		for (DisplayMode mode : modes) {
			int width = mode.getWidth();
			int height = mode.getHeight();
			if (mode.getRefreshRate() != 60 || mode.getBitDepth() != 32) {
				continue;
			}
			if (width == 1366 && height == 768) {
				resolutions.add(new Resolution(width, height));
			} else if (width == 1600 && height == 900) {
				resolutions.add(new Resolution(width, height));
			} else if (width == 1920 && height == 1080) {
				resolutions.add(new Resolution(width, height));
			}
		}
		resolutions.sort(new Comparator<Resolution>() {

			@Override
			public int compare(Resolution o1, Resolution o2) {
				int width1 = o1.getWidth();
				int width2 = o2.getWidth();
				if (width1 == width2) {
					return 0;
				}
				return width1 > width2 ? 1 : -1;
			}
		});
		resolution.getItems().addAll(resolutions);
		Preferences prefs = Preferences.userRoot().node(SettingConsts.REG_APP_PATH);
//		分辨率
		int width = prefs.getInt(SettingConsts.RESOLUTION_WIDTH, 1366);
		if (width < 1366) {
			width = 1366;
		}
		int height = prefs.getInt(SettingConsts.RESOLUTION_HEIGHT, 768);
		if (height < 768) {
			height = 768;
		}
		resolution.getSelectionModel().select(new Resolution(width, height));

		boolean fullscreen = prefs.getBoolean(SettingConsts.SCREEN_MODE, false);
		full.setSelected(fullscreen);
	}

	@FXML
	private void modifyPwd() {
		String oldPwd = this.oldPwd.getText();
		if (StringUtils.isEmpty(oldPwd)) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("setting.password.old")));
			return;
		}
		String newPwd = this.newPwd.getText();
		if (StringUtils.isEmpty(newPwd)) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("setting.password.new")));
			return;
		}
		String pwdAgain = this.pwdAgain.getText();
		if (StringUtils.isEmpty(pwdAgain)) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("setting.password.again")));
			return;
		}
		if (!newPwd.equals(oldPwd)) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.password.cant.same"));
			return;
		}
		if (!newPwd.equals(pwdAgain)) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.password.unmatch"));
			return;
		}
		User user = SpringUtil.getBean(UserAction.class).findUserByID(Session.get(Session.KEY_LOGIN_ID));
		if (!user.getPassword().equals(oldPwd)) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.password.error"));
			return;
		}
		user.setPassword(newPwd);
		SpringUtil.getBean(UserAction.class).modifyUser(user);
		dialog.close();
		AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.data.update.success"));
	}

	@FXML
	private void saveSetting() {
		Preferences prefs = Preferences.userRoot().node(SettingConsts.REG_APP_PATH);
		LangItem item = language.getSelectionModel().getSelectedItem();
		prefs.put(SettingConsts.LANGUAGE, item.getLang());
		Resolution resolution = this.resolution.getValue();
		prefs.putInt(SettingConsts.RESOLUTION_WIDTH, resolution.getWidth());
		prefs.putInt(SettingConsts.RESOLUTION_HEIGHT, resolution.getHeight());
		prefs.putBoolean(SettingConsts.SCREEN_MODE, full.isSelected());
		dialog.close();
		AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.setting.after.reset"));
	}
}
