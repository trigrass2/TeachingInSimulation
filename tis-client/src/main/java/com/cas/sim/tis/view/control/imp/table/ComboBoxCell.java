package com.cas.sim.tis.view.control.imp.table;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxCell<T>extends Cell<T> {

	@SafeVarargs
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(final T... items) {
		return forTableColumn(null, items);
	}

	@SafeVarargs
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(final StringConverter<T> converter, final T... items) {
		return forTableColumn(converter, FXCollections.observableArrayList(items));
	}

	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(final ObservableList<T> items) {
		return forTableColumn(null, items);
	}

	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(final StringConverter<T> converter, final ObservableList<T> items) {
		return column -> new ComboBoxCell<T>(converter, items);
	}

	private final ObservableList<T> items;

	private ComboBox<T> box = new ComboBox<T>();

	private ChangeListener<T>  listener;

	public ComboBoxCell(StringConverter<T> converter, ObservableList<T> items) {
		super(converter);
		this.items = items;
		box.setConverter(converter);
		box.setItems(items);
		bind();
	}
	
	private void bind() {
		listener = (ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
			if (oldValue == newValue) {
				return;
			}
			updateItem(newValue);
		};
		box.selectionModelProperty().get().selectedItemProperty().addListener(listener);
	}
	
	@Override
	public void destory() {
		super.destory();
		box.selectionModelProperty().get().selectedItemProperty().removeListener(listener);
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
			if (box == null) {
				box = new ComboBox<>();
				box.getItems().setAll(items);
			}
			box.getSelectionModel().select(getItem());
			this.setText(null);
			this.setGraphic(box);
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
			if (box != null) {
				box.getSelectionModel().select(getItem());
			}
			setText(null);
			setGraphic(box);
		} else {
			setText(getConverter().toString(getItem()));
			setGraphic(null);
		}
	}
}
