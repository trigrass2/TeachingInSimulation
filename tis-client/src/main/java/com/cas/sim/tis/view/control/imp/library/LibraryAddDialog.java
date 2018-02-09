package com.cas.sim.tis.view.control.imp.library;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.consts.LibraryType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.util.Util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class LibraryAddDialog extends DialogPane<Library> {

	public LibraryAddDialog(LibraryType type) {
		super();

		VBox box = new VBox(25);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
		
		GridPane grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(0,80,0,80));

		RowConstraints row1 = new RowConstraints();
		RowConstraints row2 = new RowConstraints();

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();

		grid.getRowConstraints().add(row1);// 2*50 percent
		grid.getRowConstraints().add(row2);

		grid.getColumnConstraints().add(col1); // 25 percent
		grid.getColumnConstraints().add(col2); // 50 percent

		Label nameLabel = new Label(MsgUtil.getMessage("library.name"));
        GridPane.setConstraints(nameLabel, 0, 0);
        
        TextField libName = new TextField();
		libName.setPromptText(MsgUtil.getMessage("library.name.prompt"));
		libName.setMaxSize(380, 40);
		libName.setMinSize(380, 40);
		GridPane.setConstraints(libName, 1, 0);
		
		Label timeLabel = new Label(MsgUtil.getMessage("library.time"));
		GridPane.setConstraints(timeLabel, 0, 1);
		
		TextField timeText = new TextField("100");
		timeText.setPromptText(MsgUtil.getMessage("library.time.prompt"));
		timeText.setMaxSize(380, 40);
		timeText.setMinSize(380, 40);
		timeText.textProperty().addListener((b, o, n) -> {
			if (Util.isEmpty(n)) {
				return;
			}
			if (!Util.isInteger(n)) {
				timeText.setText(o);
				return;
			}
			int val = Integer.parseInt(n);
			if (val > 999) {
				timeText.setText("999");
			} else if (val <= 0) {
				timeText.setText("1");
			} else {
				timeText.setText(String.valueOf(val));
			}
		});
		GridPane.setConstraints(timeText, 1, 1);
		
		grid.getChildren().addAll(nameLabel, libName, timeLabel, timeText);
		box.getChildren().add(grid);
		
		Label error = new Label();
		error.getStyleClass().add("error");
		box.getChildren().add(error);
		
		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setPrefSize(100, 40);
		ok.setOnAction(e -> {
			String name = libName.getText();
			if (StringUtils.isEmpty(name)) {
				error.setText(MsgUtil.getMessage("library.name.prompt"));
				return;
			}
			String time = timeText.getText();
			if (StringUtils.isEmpty(time)) {
				error.setText(MsgUtil.getMessage("library.time.prompt"));
				return;
			}
			Library library = new Library();
			library.setName(name);
			library.setTime(Integer.valueOf(time));
			library.setType(type.getType());

			dialog.setResult(library);
		});

		Button cancel = new Button(MsgUtil.getMessage("button.cancel"));
		cancel.getStyleClass().add("blue-btn");
		cancel.setPrefSize(100, 40);
		cancel.setOnAction(e -> {
			dialog.setResult(null);
			dialog.close();
		});

		HBox btns = new HBox(40);
		btns.setAlignment(Pos.CENTER);
		btns.getChildren().addAll(ok, cancel);
		box.getChildren().add(btns);

		this.getChildren().add(box);
	}
}
