package com.cas.sim.tis.view.control.imp.preparation;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.cas.sim.tis.action.CatalogAction;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PreparationMenu extends VBox implements ILeftContent {

	@FXML
	private Label subject;
	@FXML
	private Accordion projects;

	public PreparationMenu(Catalog subject) {
		loadFXML();
		initialize(subject);
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/preparation/LeftMenu.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 界面初始化
	 * @param catalog
	 */
	private void initialize(Catalog subject) {
		this.subject.setText(subject.getName());
		this.projects.getPanes().clear();

		List<Catalog> projects = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(subject.getId());
		for (Catalog project : projects) {
			TitledPane pane = new TitledPane();
			pane.setOnMousePressed(e -> {
				if (pane.getContent() == null) {
					initializeContent(pane, project);
					pane.setOnMousePressed(null);
				}
			});
			pane.setGraphic(createGraphicTitle(project));
			this.projects.getPanes().add(pane);
		}
	}

	private void initializeContent(TitledPane pane, Catalog project) {
		List<Catalog> tasks = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(project.getId());
		VBox box = new VBox(10);
		ToggleGroup group = new ToggleGroup();
		for (Catalog task : tasks) {
			ToggleButton taskBtn = new ToggleButton(task.getName());
			taskBtn.setGraphic(createGraphicTitle(task));
			taskBtn.getStyleClass().add("titled-content-btn");
			taskBtn.getStyleClass().remove("toggle-button");
			taskBtn.setOnAction(e -> {
				// TODO 加载备课详情
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new PreparationDetail(task), PageLevel.Level1);
				
			});
			box.getChildren().add(taskBtn);
			group.getToggles().add(taskBtn);
		}
		pane.setContent(box);
	}

	private HBox createGraphicTitle(Catalog catalog) {
		HBox title = new HBox();
		title.setPrefWidth(180);
		title.setAlignment(Pos.CENTER_LEFT);

		Label name = new Label(catalog.getName());
		name.setTextFill(Color.WHITE);
		name.setTooltip(new Tooltip(catalog.getName() + "    " + catalog.getLessons() + "'"));

		HBox box = new HBox();
		HBox.setHgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER_RIGHT);

		Label lessones = new Label(catalog.getLessons() + "'");
		lessones.setTextFill(Color.WHITE);

		box.getChildren().add(lessones);

		title.getChildren().addAll(name, box);
		return title;
	}

	@FXML
	private void switchSubject() {
		List<Catalog> catalogs = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(0);

		Dialog<Catalog> dialog = new Dialog<>();
		dialog.setDialogPane(new CatalogSelectDialog(catalogs));
		dialog.setTitle(MsgUtil.getMessage("class.dialog.select"));
		dialog.setPrefSize(652, 420);
		dialog.showAndWait().ifPresent(subject -> {
			if (subject == null) {
				return;
			}
			initialize(subject);
		});
	}
	
	@Override
	public Region getLeftContent() {
		return this;
	}

}
