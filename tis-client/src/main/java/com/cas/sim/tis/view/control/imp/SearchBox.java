/* ....Show License.... */

package com.cas.sim.tis.view.control.imp;

import com.cas.sim.tis.util.MsgUtil;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class SearchBox extends Region {

	private TextField textBox;

	private Button clearButton;

	public SearchBox() {
		setId("SearchBox");
		setMinHeight(24);
		setPrefSize(200, 24);
		setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		textBox = new TextField();
		textBox.setPromptText(MsgUtil.getMessage("resource.search.keys"));
		textBox.getStyleClass().add("search-box");
		clearButton = new Button();
		clearButton.setVisible(false);
		clearButton.getStyleClass().add("search-btn");
		getChildren().addAll(textBox, clearButton);

		clearButton.setOnAction((ActionEvent actionEvent) -> {
			textBox.setText("");
			textBox.requestFocus();
		});

		textBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			clearButton.setVisible(textBox.getText().length() != 0);
		});

	}

	@Override
	protected void layoutChildren() {
		textBox.resize(getWidth(), getHeight());
		clearButton.resizeRelocate(getWidth() - 18, 6, 12, 13);
	}

	public String getText() {
		return textBox.getText();
	}

}