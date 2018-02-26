package com.cas.sim.tis.view.control.imp.classes;

import java.util.List;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * 弹出窗口选择班级面板<br>
 * 结果返回班级表编号
 * @功能 ClassSelectDialog.java
 * @作者 Caowj
 * @创建日期 2018年2月10日
 * @修改人 Caowj
 */
public class ClassSelectDialog extends DialogPane<Integer> {

	private ToggleGroup group = new ToggleGroup();

	public ClassSelectDialog(List<Class> classes) {
		super();

		FlowPane flow = new FlowPane();
		flow.setHgap(15);
		flow.setVgap(15);
		flow.setPrefWrapLength(600);
		flow.setAlignment(Pos.TOP_CENTER);

		for (Class clazz : classes) {
			flow.getChildren().add(createSelectItem(clazz));
		}

		ScrollPane scroll = new ScrollPane(flow);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		scroll.setPadding(new Insets(10));
		VBox.setVgrow(scroll, Priority.ALWAYS);

		Label error = new Label();
		error.getStyleClass().add("red");

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setPrefSize(390, 40);
		ok.setOnAction(e -> {
			Toggle toggle = group.getSelectedToggle();
			if (toggle == null) {
				error.setText(MsgUtil.getMessage("class.select.error"));
				return;
			}
			Class clazz = (Class) toggle.getUserData();
			dialog.setResult(clazz.getId());
		});

		this.setSpacing(20);
		this.setPadding(new Insets(0, 0, 20, 0));
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(scroll, error, ok);
	}

	private ToggleButton createSelectItem(Class clazz) {
		ToggleButton toggle = new ToggleButton(clazz.getName());
		toggle.getStyleClass().add("class-select-item");
		toggle.setUserData(clazz);
		group.getToggles().add(toggle);
		return toggle;
	}
}
