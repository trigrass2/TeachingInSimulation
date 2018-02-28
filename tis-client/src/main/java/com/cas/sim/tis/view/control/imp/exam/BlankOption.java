package com.cas.sim.tis.view.control.imp.exam;

import java.util.List;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.util.MathUtil;
import com.cas.util.StringUtil;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BlankOption extends VBox implements IOption {

	private LibraryAnswer libraryAnswer;

	private Question question;

	private String[] answers;

	private List<String> references;

	public BlankOption(int index, LibraryAnswer libraryAnswer, boolean showRefrence) {
		this.setSpacing(20);
		this.libraryAnswer = libraryAnswer;

		this.question = libraryAnswer.getQuestion();

		List<String> titles = StringUtil.split(question.getTitle());
		FlowPane flow = new FlowPane();
		flow.setPrefWrapLength(680);
		flow.getChildren().add(new Label(index + "、"));
		for (int i = 0; i < titles.size(); i++) {
			Label label = new Label(titles.get(i));
			flow.getChildren().add(label);
			if (i < titles.size() - 1) {
				Label option = new Label("   " + (char) (i + 65) + "   ");
				option.getStyleClass().add("orange");
				option.getStyleClass().add("bold");
				flow.getChildren().add(option);
			}
		}
		Label label = new Label(MsgUtil.getMessage("question.point", question.getPoint()));
		flow.getChildren().add(label);

		this.getChildren().add(flow);

		String answer = libraryAnswer.getAnswer();
		references = StringUtil.split(question.getReference());
		if (StringUtils.isEmpty(answer)) {
			this.answers = new String[references.size()];
		} else {
			this.answers = answer.split("\\|", references.size());
		}
		for (int i = 0; i < references.size(); i++) {
			createTextField(i);
		}

		if (showRefrence) {
			Text reference = new Text(MsgUtil.getMessage("question.reference", StringUtil.combine(references, '；')));
			reference.getStyleClass().add("green");
			reference.setWrappingWidth(680);
			this.getChildren().add(reference);
		}
	}

	private void createTextField(int index) {
		char option = (char) (index + 65);
		Label label = new Label(option + "、");
		TextField textField = new TextField();
		textField.setPrefHeight(55);
		textField.setText(answers[index]);
		textField.textProperty().addListener((b, o, n) -> {
			if (n == null || n.equals("null")) {
				answers[index] = "";
			} else {
				answers[index] = n;
			}
		});
		HBox.setHgrow(textField, Priority.ALWAYS);

		HBox box = new HBox();
		box.setSpacing(20);
		box.setPrefHeight(55);
		box.setMinWidth(680);
		box.setMaxWidth(1485);
		box.setAlignment(Pos.CENTER_LEFT);
		box.getChildren().addAll(label, textField);

		this.getChildren().add(box);
	}

	@Override
	public LibraryAnswer getAnswer() {
		String answer = "";
		boolean corrected = true;
		float score = 0f;
		float per = question.getPoint() / answers.length;
		for (int i = 0; i < answers.length; i++) {
			answer += answers[i] == null ? "" : answers[i];
			if (i != answers.length - 1) {
				answer += "|";
			}
			boolean answerCheck = references.get(i).equals(answers[i]);
			corrected = corrected && answerCheck;
			if (answerCheck) {
				score += per;
			}
		}
		libraryAnswer.setAnswer(answer);
		if (corrected) {
			libraryAnswer.setCorrected(AnswerState.ANSWER_STATE_RIGHT.getType());
		} else {
			libraryAnswer.setCorrected(AnswerState.ANSWER_STATE_WRONG.getType());
		}
		libraryAnswer.setScore(MathUtil.round(2, score));
		return libraryAnswer;
	}

}
