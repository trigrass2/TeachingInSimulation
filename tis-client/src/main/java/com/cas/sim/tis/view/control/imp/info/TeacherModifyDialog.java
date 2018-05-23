package com.cas.sim.tis.view.control.imp.info;

import javax.annotation.Nonnull;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

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

public class TeacherModifyDialog extends DialogPane<User> {
	public TeacherModifyDialog(@Nonnull User teacher) {

		super();

		VBox box = new VBox(25);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);

		GridPane grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(0, 80, 0, 80));

		RowConstraints row1 = new RowConstraints();
		RowConstraints row2 = new RowConstraints();

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();

		grid.getRowConstraints().add(row1);// 2*50 percent
		grid.getRowConstraints().add(row2);

		grid.getColumnConstraints().add(col1); // 25 percent
		grid.getColumnConstraints().add(col2); // 50 percent

		Label codeLabel = new Label(MsgUtil.getMessage("teacher.code"));
		GridPane.setConstraints(codeLabel, 0, 0);

		TextField code = new TextField();
		code.setMaxSize(380, 40);
		code.setMinSize(380, 40);
		code.setText(teacher.getCode());
		GridPane.setConstraints(code, 1, 0);

		Label nameLabel = new Label(MsgUtil.getMessage("teacher.name"));
		GridPane.setConstraints(nameLabel, 0, 1);

		TextField name = new TextField();
		name.setMaxSize(380, 40);
		name.setMinSize(380, 40);
		name.setText(teacher.getName());
		GridPane.setConstraints(name, 1, 1);

		grid.getChildren().addAll(codeLabel, code, nameLabel, name);
		box.getChildren().add(grid);

		Label error = new Label();
		error.getStyleClass().add("red");
		box.getChildren().add(error);

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setPrefSize(100, 40);
		ok.setOnAction(e -> {
			String codeText = code.getText();
			if (StringUtils.isEmpty(codeText)) {
				error.setText(MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("teacher.code")));
				return;
			}
			String nameText = name.getText();
			if (StringUtils.isEmpty(nameText)) {
				error.setText(MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("teacher.name")));
				return;
			}
			teacher.setCode(codeText);
			teacher.setName(nameText);
			dialog.setResult(teacher);
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
