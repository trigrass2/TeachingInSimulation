package com.cas.sim.tis.view.control.imp.question;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.LibraryPublishAction;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;

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

public class ExamingDialog extends DialogPane<Boolean> {

	public ExamingDialog(LibraryPublish publish) {
		Class clazz = publish.getClazz();

		Label className = new Label(clazz.getName());
		className.getStyleClass().add("orange");

		Library library = publish.getLibrary();

		Label libraryName = new Label(library.getName());
		libraryName.getStyleClass().add("orange");

		Label time = new Label(MsgUtil.getMessage("library.time.expand", library.getTime()));
		time.getStyleClass().add("orange");

		SVGGlyph glyph = new SVGGlyph("iconfont.svg.refresh", Color.web("#19b0c6"), 25);

		RotateTransition transition = new RotateTransition(Duration.millis(500), glyph);
		transition.setFromAngle(0);
		transition.setToAngle(360);

		Button refresh = new Button();
		refresh.setGraphic(refresh);
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
		box.setPadding(new Insets(20));
		box.getChildren().addAll(className, libraryName, time);

		Table table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setRowHeight(45);

		// 数据库唯一表示
		Column<Integer> primary = new Column<>();
		primary.setPrimary(true);
		primary.setVisible(false);
		primary.setKey("id");
		// 学生学号
		Column<String> code = new Column<>();
		code.setAlignment(Pos.CENTER);
		code.setKey("code");
		code.setText(MsgUtil.getMessage("student.code"));
		code.setPrefWidth(150);
		// 学生名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("student.name"));
		name.setPrefWidth(100);
		table.getColumns().addAll(primary, code, name);
		// 提交状态
		Column<String> submit = new Column<>();
		submit.setAlignment(Pos.CENTER);
		submit.setKey("state");
		submit.setText(MsgUtil.getMessage("exam.submit.state"));
		submit.setPrefWidth(100);
		table.getColumns().addAll(primary, code, submit);

		ScrollPane scroll = new ScrollPane();
		scroll.setFitToWidth(true);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setContent(table);
		VBox.setVgrow(scroll, Priority.ALWAYS);

		Button finish = new Button();

		HBox btns = new HBox(50);

		this.getChildren().addAll(box, scroll, btns);
	}

	private void refresh(Integer pid) {
		SpringUtil.getBean(LibraryPublishAction.class).findSubmitStateByPublishId(pid);
	}
}
