package com.cas.sim.tis.view.control.imp.question;

import com.cas.sim.tis.action.BrokenPublishAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.action.PreparationPublishAction;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.BrokenPublish;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SocketUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.broken.BrokenExamingDialog;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.preparation.PublishLibraryFeedback;

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

	private RotateTransition rotateTransition;
	private Button examing;
	private int examType;

	private LibraryPublish libraryPublish;
	private PreparationPublish preparationPublish;
	private BrokenPublish repairPublish;

	public ExamingMenuItem(int examType) {
		this.examType = examType;
		// 预置考试进行中提示
		SVGGlyph glyph = new SVGGlyph("iconfont.svg.clock", Color.WHITE, 22);
//		此时的rotateTransition必然是null值
//		if (rotateTransition != null) {
//			rotateTransition.stop();
//		}
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
		examing.getStyleClass().add("left-menu-orange");
		examing.setOnAction(e -> {
			if (ExamMessage.EXAM_TYPE_LIBRARY == examType) {
				showLibraryDialog();
			} else if (ExamMessage.EXAM_TYPE_PREPARATION == examType) {
				showPreparationLibrary();
			} else if (ExamMessage.EXAM_TYPE_BROKEN == examType) {
				showBrokenExamDialog();
			}
		});

		VBox.setVgrow(examing, Priority.ALWAYS);
		getChildren().add(examing);
		setAlignment(Pos.CENTER);
		setVisible(false);
	}

	public void load(int id) {
		String name = null;
		if (ExamMessage.EXAM_TYPE_LIBRARY == examType) {
			this.libraryPublish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(id);
			name = libraryPublish.getLibrary().getName();
		} else if (ExamMessage.EXAM_TYPE_PREPARATION == examType) {
			this.preparationPublish = SpringUtil.getBean(PreparationPublishAction.class).findPublishById(id);
			name = preparationPublish.getLibrary().getName();
		} else if (ExamMessage.EXAM_TYPE_BROKEN == examType) {
			this.repairPublish = SpringUtil.getBean(BrokenPublishAction.class).findPublishById(id);
			name = repairPublish.getBrokenCase().getName();
		}
		examing.setText(name);
		examing.setTooltip(new Tooltip(name));
		setVisible(true);
	}

	private void showLibraryDialog() {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setDialogPane(new ExamingDialog(libraryPublish));
		dialog.setTitle(MsgUtil.getMessage("exam.dialog.progress"));
		dialog.setPrefSize(640, 380);
		dialog.showAndWait().ifPresent(finish -> {
			if (finish) {
				ExamMessage message = new ExamMessage();
				message.setPid(libraryPublish.getId());
				message.setMessageType(ExamMessage.MESSAGE_TYPE_OVER);
				message.setExamType(examType);

				SocketUtil.INSTENCE.send(message);
				ExamingMenuItem.this.setVisible(false);
				rotateTransition.stop();
				Session.set(Session.KEY_LIBRARY_PUBLISH_ID, null);
			}
		});
	}

	private void showPreparationLibrary() {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setDialogPane(new PreparationExamingDialog(preparationPublish));
		dialog.setTitle(MsgUtil.getMessage("exam.dialog.progress"));
		dialog.setPrefSize(640, 380);
		dialog.showAndWait().ifPresent(finish -> {
			if (finish) {
				ExamMessage message = new ExamMessage();
				message.setPid(preparationPublish.getId());
				message.setMessageType(ExamMessage.MESSAGE_TYPE_OVER);
				message.setExamType(examType);

				SocketUtil.INSTENCE.send(message);
				ExamingMenuItem.this.setVisible(false);
				rotateTransition.stop();
				Session.set(Session.KEY_PREPARATION_PUBLISH_ID, null);

				showLibraryTestDialog(preparationPublish.getId(), preparationPublish.getLibrary().getId());
			}
		});
	}

	private void showLibraryTestDialog(Integer pid, Integer preparationLibraryId) {
		PublishLibraryFeedback pane = new PublishLibraryFeedback(pid, preparationLibraryId);
		Dialog<Void> dialog = new Dialog<>();
		dialog.setDialogPane(pane);
		dialog.setTitle(MsgUtil.getMessage("preparation.question.library.result"));
		dialog.setPrefSize(1136, 693);
		dialog.showAndWait();
	}

	private void showBrokenExamDialog() {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setDialogPane(new BrokenExamingDialog(repairPublish));
		dialog.setTitle(MsgUtil.getMessage("exam.dialog.progress"));
		dialog.setPrefSize(640, 380);
		dialog.showAndWait().ifPresent(finish -> {
			if (finish) {
				ExamMessage message = new ExamMessage();
				message.setPid(repairPublish.getId());
				message.setMessageType(ExamMessage.MESSAGE_TYPE_OVER);
				message.setExamType(examType);

				SocketUtil.INSTENCE.send(message);
				ExamingMenuItem.this.setVisible(false);
				rotateTransition.stop();
				Session.set(Session.KEY_PREPARATION_PUBLISH_ID, null);
			}
		});
	}

	@Override
	public void distroy() {
		rotateTransition.stop();
	}
}
