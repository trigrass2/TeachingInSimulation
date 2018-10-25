package com.cas.sim.tis.view.control.imp.exam;

import java.util.ArrayList;
import java.util.List;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.util.StringUtil;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChoiceOption extends VBox implements IOption {

	private ExamLibraryAnswer libraryAnswer;

	private Question question;

	private List<CheckBox> boxs = new ArrayList<>();

	public ChoiceOption(int index, ExamLibraryAnswer libraryAnswer, boolean showRefrence) {
		this.setSpacing(20);
		this.libraryAnswer = libraryAnswer;
		this.question = libraryAnswer.getQuestion();

		Text text = new Text(index + "、" + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		text.getStyleClass().add("font14");
		text.setWrappingWidth(680);
		this.getChildren().add(text);

		List<String> options = StringUtil.split(question.getOptions());
		String answer = libraryAnswer.getAnswer();
		for (int i = 0; i < options.size(); i++) {
			char userData = (char) (i + 65);
			createCheckBox(options.get(i), userData, (answer != null && answer.indexOf(userData) > -1));
		}
		
		if (showRefrence) {
			Label reference = new Label(MsgUtil.getMessage("question.reference", question.getReference()));
			reference.getStyleClass().add("green");
			this.getChildren().add(reference);
		}
	}

	private void createCheckBox(String text, char userData, boolean selected) {
		CheckBox option = new CheckBox(text);
		option.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		option.getStyleClass().add("option");
		option.setUserData(userData);
		option.setWrapText(true);
		option.setSelected(selected);
		option.setOnAction(e -> {
			StringBuffer answer = new StringBuffer();
			for (CheckBox box : boxs) {
				if (box.isSelected()) {
					answer.append(box.getUserData());
				}
			}
			libraryAnswer.setAnswer(answer.toString());
			if (question.getReference().equals(answer.toString())) {
				libraryAnswer.setCorrected(AnswerState.ANSWER_STATE_RIGHT.getType());
				libraryAnswer.setScore(question.getPoint());
			} else {
				libraryAnswer.setCorrected(AnswerState.ANSWER_STATE_WRONG.getType());
				libraryAnswer.setScore(0f);
			}
		});
		this.boxs.add(option);
		this.getChildren().add(option);
		VBox.setVgrow(option, Priority.ALWAYS);
	}

	@Override
	public ExamLibraryAnswer getAnswer() {
		return libraryAnswer;
	}

}
