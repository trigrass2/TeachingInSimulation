package com.cas.sim.tis.view.control.imp.question;

import java.util.List;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.util.StringUtil;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecordQuestionItem extends VBox {

	private int index;
	private ExamLibraryAnswer answer;
	private Question question;

	public RecordQuestionItem(int index, ExamLibraryAnswer answer) {
		this.index = index;
		this.answer = answer;
		this.question = answer.getQuestion();
		this.getStyleClass().add("question");

		Question question = answer.getQuestion();
		loadQuestion(QuestionType.getQuestionType(question.getType()));
	}

	private void loadQuestion(QuestionType type) {
		switch (type) {
		case CHOICE:
			loadChoiceQuestion();
			break;
		case JUDGMENT:
			loadJudgmentQuestion();
			break;
		case BLANK:
			loadBlankQuestion();
			break;
		case SUBJECTIVE:
			loadSubjective();
			break;
		default:
			break;
		}
	}

	private void loadChoiceQuestion() {
		Text title = new Text(index + ". " + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);

		VBox box = new VBox(5);
		this.getChildren().add(box);

		List<String> options = StringUtil.split(question.getOptions());
		String reference = question.getReference();
		for (String option : options) {
			Text text = new Text(option);
			text.setWrappingWidth(600);
			if (reference.indexOf(option.substring(0, 1)) > -1) {
				text.getStyleClass().add("green");
			}
			VBox.setVgrow(text, Priority.ALWAYS);
			box.getChildren().add(text);
		}
		Label studentAnswer = new Label(MsgUtil.getMessage("question.answer", answer.getAnswer()));
		if (AnswerState.ANSWER_STATE_RIGHT.getType() == answer.getCorrected()) {
			studentAnswer.getStyleClass().add("green");
		} else {
			studentAnswer.getStyleClass().add("red");
		}
		box.getChildren().add(studentAnswer);

	}

	private void loadJudgmentQuestion() {
		Text title = new Text(index + ". " + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);

		Label reference = new Label(question.getReference());
		reference.getStyleClass().add("green");
		this.getChildren().add(reference);

		Label studentAnswer = new Label(MsgUtil.getMessage("question.answer", answer.getAnswer()));
		if (AnswerState.ANSWER_STATE_RIGHT.getType() == answer.getCorrected()) {
			studentAnswer.getStyleClass().add("green");
		} else {
			studentAnswer.getStyleClass().add("red");
		}
		this.getChildren().add(studentAnswer);
	}

	private void loadBlankQuestion() {
		String title = question.getTitle();
		List<String> titles = StringUtil.split(title);
		List<String> references = StringUtil.split(question.getReference());

		FlowPane pane = new FlowPane();
		pane.maxWidth(600);
		this.getChildren().add(pane);
		VBox.setVgrow(pane, Priority.ALWAYS);

		pane.getChildren().add(new Label(index + ". "));
		for (int i = 0; i < titles.size(); i++) {
			Label label = new Label(titles.get(i));
			pane.getChildren().add(label);
			if (i < references.size()) {
				Label reference = new Label(references.get(i));
				reference.getStyleClass().add("green");
				pane.getChildren().add(reference);
			}
		}
		Label label = new Label(MsgUtil.getMessage("question.point", question.getPoint()));
		pane.getChildren().add(label);

		Text studentAnswer = new Text(MsgUtil.getMessage("question.answer", answer.getAnswer().replaceAll("\\|", "；")));
		studentAnswer.setWrappingWidth(600);
		if (AnswerState.ANSWER_STATE_RIGHT.getType() == answer.getCorrected()) {
			studentAnswer.getStyleClass().add("green");
		} else {
			studentAnswer.getStyleClass().add("red");
		}
		this.getChildren().add(studentAnswer);
	}

	private void loadSubjective() {
		Text title = new Text(index + ". " + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);

		String answerStr = answer.getAnswer();
		Text studentAnswer = new Text(MsgUtil.getMessage("question.answer", answerStr == null ? "" : answerStr));
		studentAnswer.setWrappingWidth(600);
		if (AnswerState.ANSWER_STATE_RIGHT.getType() == answer.getCorrected()) {
			studentAnswer.getStyleClass().add("green");
		} else {
			studentAnswer.getStyleClass().add("red");
		}
		this.getChildren().add(studentAnswer);
	}
}
