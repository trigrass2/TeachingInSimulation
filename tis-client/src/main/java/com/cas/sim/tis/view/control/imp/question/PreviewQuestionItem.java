package com.cas.sim.tis.view.control.imp.question;

import java.util.List;

import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.Question;
import com.cas.util.StringUtil;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PreviewQuestionItem extends VBox {

	private int index;

	public PreviewQuestionItem(int index, QuestionType type, Question question) {
		this.index = index;
		this.setSpacing(20);
		loadQuestion(type, question);
	}

	private void loadQuestion(QuestionType type, Question question) {
		switch (type) {
		case CHOICE:
			loadChoiceQuestion(question);
			break;
		case JUDGMENT:
			loadJudgmentQuestion(question);
			break;
		case BLANK:
			loadBlankQuestion(question);
			break;
		case SUBJECTIVE:
			loadSubjective(question);
			break;
		default:
			break;
		}
	}

	private void loadChoiceQuestion(Question question) {
		Text title = new Text(index + ". " + question.getTitle());
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
				text.getStyleClass().add("reference");
			}
			VBox.setVgrow(text, Priority.ALWAYS);
			box.getChildren().add(text);
		}
	}

	private void loadJudgmentQuestion(Question question) {
		Text title = new Text(index + ". " + question.getTitle());
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);

		Label reference = new Label(question.getReference());
		reference.getStyleClass().add("reference");
		this.getChildren().add(reference);
	}

	private void loadBlankQuestion(Question question) {
		List<String> titles = StringUtil.split(question.getTitle());
		List<String> references = StringUtil.split(question.getReference());

		FlowPane pane = new FlowPane();
		pane.maxWidth(600);
		this.getChildren().add(pane);
		VBox.setVgrow(pane, Priority.ALWAYS);

		pane.getChildren().add(new Label(index + ". "));
		for (int i = 0; i < titles.size(); i++) {
			Label title = new Label(titles.get(i));
			pane.getChildren().add(title);
			if (i < references.size()) {
				Label reference = new Label(references.get(i));
				reference.getStyleClass().add("reference");
				pane.getChildren().add(reference);
			}
		}
	}

	private void loadSubjective(Question question) {
		Text title = new Text(index + ". " + question.getTitle());
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);

		Text reference = new Text(question.getReference());
		reference.getStyleClass().add("reference");
		reference.setWrappingWidth(600);
		this.getChildren().add(reference);
		VBox.setVgrow(reference, Priority.ALWAYS);
	}
}
