package com.cas.sim.tis.view.control.imp.exam;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class JudgmentOption extends VBox implements IOption {

	private static final String RIGHT = "正确";
	private static final String WRONG = "错误";

	private LibraryAnswer libraryAnswer;

	private ToggleGroup group = new ToggleGroup();

	public JudgmentOption(int index, LibraryAnswer libraryAnswer, boolean showRefrence) {
		this.setSpacing(20);
		this.libraryAnswer = libraryAnswer;

		Question question = libraryAnswer.getQuestion();

		Text text = new Text(index + "、" + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		text.getStyleClass().add("font14");
		text.setWrappingWidth(660);
		this.getChildren().add(text);

		String answer = libraryAnswer.getAnswer();
		createRadioButton(RIGHT, RIGHT.equals(answer));
		createRadioButton(WRONG, WRONG.equals(answer));
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				libraryAnswer.setAnswer(null);
			} else {
				libraryAnswer.setAnswer((String) n.getUserData());
			}
			if (question.getReference().equals(n.getUserData())) {
				libraryAnswer.setCorrected(true);
				libraryAnswer.setScore(question.getPoint());
			} else {
				libraryAnswer.setCorrected(false);
				libraryAnswer.setScore(0f);
			}
		});
		
		if (showRefrence) {
			Label reference = new Label(MsgUtil.getMessage("exam.answer", question.getReference()));
			reference.getStyleClass().add("green");
			this.getChildren().add(reference);
		}
	}

	private void createRadioButton(String text, boolean selected) {
		RadioButton button = new RadioButton(text);
		// 单选框要设置选中图标左右位置
//		for (Node child : button.getSkin().getNode().) {
//			if (child instanceof StackPane) {
//				child.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//			}
//		}
		button.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		button.getStyleClass().add("option");
		button.setUserData(text);
		button.setWrapText(true);
		button.setSelected(selected);
		group.getToggles().add(button);
		this.getChildren().add(button);
		VBox.setVgrow(button, Priority.ALWAYS);

	}

	@Override
	public LibraryAnswer getAnswer() {
		return libraryAnswer;
	}

}
