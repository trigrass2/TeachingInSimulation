package com.cas.sim.tis.view.control.imp.question;

import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SocketUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ExamingMenuItem extends HBox implements IDistory {

	private LibraryPublish publish;
	private RotateTransition rotateTransition;
	private Button examing;

	public ExamingMenuItem() {
		// 预置考试进行中提示
		SVGGlyph glyph = new SVGGlyph("iconfont.svg.clock", Color.WHITE, 22);
		if (rotateTransition != null) {
			rotateTransition.stop();
		}
		rotateTransition = new RotateTransition(Duration.millis(100), glyph);
		rotateTransition.setFromAngle(-20);
		rotateTransition.setToAngle(20);
		rotateTransition.setAutoReverse(true);

		rotateTransition.setOnFinished(e -> {
			if (rotateTransition.getToAngle() == 20) {
				rotateTransition.setFromAngle(20);
				rotateTransition.setToAngle(0);
				rotateTransition.setCycleCount(1);
				rotateTransition.setDelay(Duration.ZERO);
				rotateTransition.playFromStart();
			} else {
				rotateTransition.setFromAngle(-20);
				rotateTransition.setToAngle(20);
				rotateTransition.setCycleCount(3);
				rotateTransition.setDelay(Duration.seconds(2));
				rotateTransition.playFromStart();
			}
		});
		rotateTransition.playFromStart();


		examing = new Button();
		examing.setGraphic(glyph);
		examing.getStyleClass().add("examing-menu");
		examing.setOnAction(e -> {
			showDialog();
		});

		VBox.setVgrow(examing, Priority.ALWAYS);
		getChildren().add(examing);
		setAlignment(Pos.CENTER);
		setVisible(false);
	}

	public void load(int id) {
		this.publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(id);
		String name = publish.getLibrary().getName();
		examing.setText(name);
		examing.setTooltip(new Tooltip(name));
		setVisible(true);
	}
	
	private void showDialog() {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setDialogPane(new ExamingDialog(publish));
		dialog.setTitle(MsgUtil.getMessage("class.dialog.modify"));
		dialog.setPrefSize(640, 380);
		dialog.showAndWait().ifPresent(finish -> {
			if (finish) {
				ExamMessage message = new ExamMessage();
				message.setPid(publish.getId());
				message.setType(ExamMessage.EXAM_OVER);
				
				SocketUtil.INSTENCE.send(message);
				ExamingMenuItem.this.setVisible(false);
				rotateTransition.stop();
			}
		});
	}

	@Override
	public void distroy() {
		rotateTransition.stop();
	}
}
