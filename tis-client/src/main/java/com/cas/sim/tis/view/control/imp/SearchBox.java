/* ....Show License.... */

package com.cas.sim.tis.view.control.imp;

import java.util.function.Consumer;

import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class SearchBox extends HBox {

	private TextField textBox;

	private Button searchButton;

	private Consumer<String> onSearch;

	public SearchBox() {
		setId("SearchBox");
		setMinSize(220, 40);
		setPrefSize(220, 40);
		setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(5));
		setStyle("-fx-border-color:#d4d4d4;");
		
		textBox = new TextField();
		textBox.getStyleClass().removeAll("text-input","text-field");
		textBox.setPrefSize(200, 40);
		textBox.setPromptText(MsgUtil.getMessage("resource.search.keys"));
		searchButton = new Button();
		searchButton.setGraphic(new SVGGlyph("iconfont.svg.search", Color.web("#d4d4d4"), 20));
		searchButton.getStyleClass().add("img-btn");
		getChildren().addAll(textBox, searchButton);
		
		textBox.setOnKeyPressed(e -> {
			if (KeyCode.ENTER == e.getCode()) {
				onSearch.accept(textBox.getText());
			}
		});

		searchButton.setOnAction((ActionEvent actionEvent) -> {
			onSearch.accept(textBox.getText());
		});
	}

	public String getText() {
		return textBox.getText();
	}
	
	public void setOnSearch(Consumer<String> onSearch) {
		this.onSearch = onSearch;
	}
}