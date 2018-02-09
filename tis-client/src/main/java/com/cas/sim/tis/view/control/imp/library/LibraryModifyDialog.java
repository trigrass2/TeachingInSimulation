package com.cas.sim.tis.view.control.imp.library;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LibraryModifyDialog extends DialogPane<String> {
	private TextField libName = new TextField();
	private Label error = new Label();

	public LibraryModifyDialog() {
		super();

		libName.setPromptText(MsgUtil.getMessage("library.name.prompt"));
		libName.setMaxSize(415, 40);
		libName.setMinSize(415, 40);

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setPrefSize(100, 40);
		ok.setOnAction(e -> {
			String libName = this.libName.getText();
			if (StringUtils.isEmpty(libName)) {
				error.setText(MsgUtil.getMessage("library.name.prompt"));
				dialog.setResult(null);
				return;
			}
			dialog.setResult(libName);
		});

		Button cancel = new Button(MsgUtil.getMessage("button.cancel"));
		cancel.getStyleClass().add("blue-btn");
		cancel.setPrefSize(100, 40);
		cancel.setOnAction(e -> {
			dialog.setResult(null);
		});

		HBox btns = new HBox(40);
		btns.setAlignment(Pos.CENTER);
		btns.getChildren().addAll(ok, cancel);

		VBox box = new VBox(25);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(libName);
		box.getChildren().add(error);
		box.getChildren().add(btns);

		this.getChildren().add(box);
	}
}
