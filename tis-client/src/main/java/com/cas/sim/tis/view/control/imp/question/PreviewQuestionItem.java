package com.cas.sim.tis.view.control.imp.question;

import java.util.List;

import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.util.StringUtil;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PreviewQuestionItem extends VBox {

	private int index;
	private boolean showReference;

	public PreviewQuestionItem(int index, QuestionType type, Question question, boolean showReference) {
		this.index = index;
		this.showReference = showReference;
		this.getStyleClass().add("preview");
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
			if (showReference && reference.indexOf(option.substring(0, 1)) > -1) {
				text.getStyleClass().add("green");
			}
			VBox.setVgrow(text, Priority.ALWAYS);
			box.getChildren().add(text);
		}
	}

	private void loadJudgmentQuestion(Question question) {
		Text title = new Text(index + ". " + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);

		if (showReference) {
			Label reference = new Label(question.getReference());
			reference.getStyleClass().add("green");
			this.getChildren().add(reference);
		}
	}

	private void loadBlankQuestion(Question question) {
		String title = question.getTitle();
		if (!showReference) {
			title = title.replaceAll("\\|", "");
			Text text = new Text(title);
			text.setWrappingWidth(600);
			this.getChildren().add(text);
		} else {
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
		}
	}

	private void loadSubjective(Question question) {
		Text title = new Text(index + ". " + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		title.setWrappingWidth(600);
		this.getChildren().add(title);
		VBox.setVgrow(title, Priority.ALWAYS);
	}
}
