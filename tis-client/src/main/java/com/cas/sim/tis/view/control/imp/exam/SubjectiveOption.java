package com.cas.sim.tis.view.control.imp.exam;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SubjectiveOption extends VBox implements IOption {

	private LibraryAnswer libraryAnswer;

	public SubjectiveOption(int index, LibraryAnswer libraryAnswer, boolean showRefrence) {
		this.setSpacing(20);
		this.libraryAnswer = libraryAnswer;

		Question question = libraryAnswer.getQuestion();

		Text text = new Text(index + "、" + question.getTitle() + MsgUtil.getMessage("question.point", question.getPoint()));
		text.getStyleClass().add("font14");
		text.setWrappingWidth(680);
		this.getChildren().add(text);

		TextArea area = new TextArea();
		area.setText(libraryAnswer.getAnswer());
		area.getStyleClass().add("option-text");
		area.textProperty().addListener((b, o, n) -> {
			this.libraryAnswer.setAnswer(n);
		});
		this.getChildren().add(area);

		if (showRefrence) {
			Text reference = new Text(MsgUtil.getMessage("question.reference", question.getReference() == null ? "" : question.getReference()));
			reference.getStyleClass().add("green");
			reference.setWrappingWidth(680);
			this.getChildren().add(reference);
		}
	}

	@Override
	public LibraryAnswer getAnswer() {
		return libraryAnswer;
	}

}
