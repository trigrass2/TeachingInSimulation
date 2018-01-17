package com.cas.sim.tis.view.control.imp.table;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class TextFieldCell<T>extends Cell<T> {

	public static <T> Callback<Column<String>, Cell<String>> forTableColumn() {
		return forTableColumn(new DefaultStringConverter());
	}

	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(final StringConverter<T> converter) {
		return column -> new TextFieldCell<T>(converter);
	}

	private TextField textField = new TextField();
	private ChangeListener<String> listener;

	public TextFieldCell(StringConverter<T> converter) {
		super(converter);
		bind();
	}

	private void bind() {
		listener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (oldValue == newValue) {
				return;
			}
			updateItem(getConverter().fromString(newValue));
		};
		textField.textProperty().addListener(listener);
	}
	
	@Override
	public void destory() {
		super.destory();
		textField.textProperty().removeListener(listener);
	}
	
	/***************************************************************************
	 * * Public API * *
	 **************************************************************************/

	@Override
	public void startEdit() {
		if (!isEditable()) {
			return;
		}
		if (isEditing()) {
			if (textField == null) {
				textField = new TextField(getConverter().toString(getItem()));
			} else {
				textField.setText(getConverter().toString(getItem()));
			}
			this.setText(null);
			this.setGraphic(textField);
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		this.setText(getConverter().toString(getItem()));
		this.setGraphic(null);
	}

	@Override
	protected void updateItem(T item) {
		super.updateItem(item);
		if (isEditing()) {
			if (textField != null) {
				textField.setText(getConverter().toString(getItem()));
			}
			setText(null);
			setGraphic(textField);
		} else {
			setText(getConverter().toString(getItem()));
			setGraphic(null);
		}
	}
}
