package com.cas.sim.tis.view.control.imp.question;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.vo.SubmitInfo;

import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class ExamingDialog extends DialogPane<Boolean> {

	private Table table = new Table("table-row", "table-row-hover", "table-row-selected");

	public ExamingDialog(LibraryPublish publish) {
		Class clazz = publish.getClazz();

		Label className = new Label(clazz.getName());
		className.getStyleClass().add("orange");

		Library library = publish.getLibrary();

		Label libraryName = new Label(library.getName());
		libraryName.getStyleClass().add("orange");
		HBox.getHgrow(libraryName);

		HBox libraryBox = new HBox();
		libraryBox.setAlignment(Pos.CENTER);
		libraryBox.getChildren().add(libraryName);
		HBox.getHgrow(libraryBox);

		Label time = new Label(MsgUtil.getMessage("library.time.expand", library.getTime()));
		time.getStyleClass().add("orange");

		SVGGlyph glyph = new SVGGlyph("iconfont.svg.refresh", Color.web("#19b0c6"), 20);

		RotateTransition transition = new RotateTransition(Duration.millis(500), glyph);
		transition.setFromAngle(0);
		transition.setToAngle(360);

		Button refresh = new Button();
		refresh.setGraphic(glyph);
		refresh.getStyleClass().add("img-btn");
		refresh.hoverProperty().addListener((b, o, n) -> {
			if (n.booleanValue()) {
				transition.playFromStart();
			}
		});
		refresh.setOnAction(e -> {
			refresh(publish.getId());
		});

		HBox box = new HBox(30);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(className, libraryBox, time, refresh);

		table.setRowHeight(45);
		table.setSeparatorable(false);
		table.setRowsSpacing(1);

		// 学生学号
		Column<String> code = new Column<>();
		code.setAlignment(Pos.CENTER);
		code.setKey("code");
		code.setText(MsgUtil.getMessage("student.code"));
		code.setPrefWidth(100);
		// 学生名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("student.name"));
		name.setPrefWidth(100);
		// 提交状态
		Column<Boolean> submit = new Column<>();
		submit.setAlignment(Pos.CENTER);
		submit.setKey("submited");
		submit.setText(MsgUtil.getMessage("exam.submit.state"));
		submit.setPrefWidth(100);
		submit.setStyleFactory(new StringConverter<Boolean>() {

			@Override
			public String toString(Boolean submited) {
				if (submited) {
					return "blue";
				} else {
					return "orange";
				}
			}

			@Override
			public Boolean fromString(String string) {
				return null;
			}
		});
		submit.setCellFactory(Cell.forTableColumn(new StringConverter<Boolean>() {

			@Override
			public String toString(Boolean submited) {
				if (submited) {
					return MsgUtil.getMessage("exam.submited");
				} else {
					return MsgUtil.getMessage("exam.unsubmit");
				}
			}

			@Override
			public Boolean fromString(String string) {
				return null;
			}
		}));
		table.getColumns().addAll(code, name, submit);

		ScrollPane scroll = new ScrollPane();
		scroll.setFitToWidth(true);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setContent(table);
		VBox.setVgrow(scroll, Priority.ALWAYS);

		Button finish = new Button(MsgUtil.getMessage("exam.to.finish"));
		finish.getStyleClass().add("blue-btn");
		finish.setPrefSize(100, 40);
		finish.setOnAction(e -> {
			setResult(true);
		});

		
		VBox content = new VBox(20);
		content.getChildren().addAll(box, scroll, finish);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(20));
		
		this.getChildren().add(content);
		
		refresh(publish.getId());
	}

	private void refresh(Integer pid) {
		List<SubmitInfo> submits = SpringUtil.getBean(LibraryPublishAction.class).findSubmitStateById(pid);
		JSONArray array = new JSONArray();
		array.addAll(submits);
		table.setItems(array);
		table.build();
	}
}
