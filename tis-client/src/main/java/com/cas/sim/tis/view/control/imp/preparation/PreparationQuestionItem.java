package com.cas.sim.tis.view.control.imp.preparation;

import java.util.function.Consumer;

import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.view.control.imp.question.PreviewQuestionItem;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class PreparationQuestionItem extends StackPane {

	private Question question;

	public PreparationQuestionItem(int index, QuestionType type, Question question, Consumer<Integer> onClose) {
		this.question = question;
		
		Button close = new Button();
		close.getStyleClass().add("img-btn");
		close.setGraphic(new SVGGlyph("iconfont.svg.close", Color.web("#f6a23f"), 15));
		close.setOnAction(e -> {
			onClose.accept(question.getId());
		});

		HBox box = new HBox();
		box.setAlignment(Pos.TOP_RIGHT);
		box.getChildren().add(close);

		PreviewQuestionItem item = new PreviewQuestionItem(index, type, question, true);
		item.getStyleClass().remove("question");
		this.getChildren().add(item);
		this.getStyleClass().add("question");

		this.setOnMouseEntered(e -> {
			this.getChildren().add(box);
		});

		this.setOnMouseExited(e -> {
			this.getChildren().remove(box);
		});
	}

	public Integer getQuestionId() {
		return question.getId();
	}

}
