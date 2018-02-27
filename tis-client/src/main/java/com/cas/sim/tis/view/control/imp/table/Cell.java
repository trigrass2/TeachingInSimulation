package com.cas.sim.tis.view.control.imp.table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Cell<T>extends Label {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(final StringConverter<T> converter) {
		return column -> new Cell<T>(converter);
	}

	protected Row row;

	protected Tooltip tooltip = new Tooltip();

	private ChangeListener<Boolean> editListener;

	private boolean header;

	public Cell(StringConverter<T> converter) {
		this.setText("");
		this.getStyleClass().add("cell");
		this.setWrapText(false);
		this.setTooltip(tooltip);
		HBox.setHgrow(this, Priority.ALWAYS);
		setConverter(converter);
		bind();
	}

	private void bind() {
		editListener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue == newValue) {
				return;
			}
			if (newValue) {
				startEdit();
			} else {
				cancelEdit();
			}
		};
		editingPropertyImpl().addListener(editListener);
	}

	private void unbind() {
		editingPropertyImpl().removeListener(editListener);
		updateTableColumn(null);
	}

	/***************************************************************************
	 * * Properties * *
	 **************************************************************************/

	// --- converter
	private ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<StringConverter<T>>(this, "converter");

	/**
	 * The {@link StringConverter} property.
	 */
	public final ObjectProperty<StringConverter<T>> converterProperty() {
		return converter;
	}

	/**
	 * Sets the {@link StringConverter} to be used in this cell.
	 */
	public final void setConverter(StringConverter<T> value) {
		converterProperty().set(value);
	}

	/**
	 * Returns the {@link StringConverter} used in this cell.
	 */
	public final StringConverter<T> getConverter() {
		return converterProperty().get();
	}

	// --- item
	protected ObjectProperty<T> item = new SimpleObjectProperty<T>(this, "item") {
		@Override
		public void set(T newValue) {
			if (newValue == null) {
				setText("");
				setTooltip(null);
			} else if (getConverter() == null) {
				setText(String.valueOf(newValue));
				tooltip.setText(String.valueOf(newValue));
			} else {
				setText(getConverter().toString(newValue));
				tooltip.setText(getConverter().toString(newValue));
			}
			super.set(newValue);
		};
	};

	public final ObjectProperty<T> itemProperty() {
		return item;
	}

	public final void setItem(T value) {
		item.set(value);
	}

	public final T getItem() {
		return item.get();
	}

	protected void updateItem(T item) {
		setItem(item);
	}

	// --- primary
	private BooleanProperty primary;

	public final void setPrimary(boolean value) {
		primaryProperty().set(value);
	}

	public final boolean isPrimary() {
		return primary == null ? false : primary.get();
	}

	public final BooleanProperty primaryProperty() {
		if (primary == null) {
			primary = new SimpleBooleanProperty(this, "primary", false);
		}
		return primary;
	}

	// --- Editing
	private ReadOnlyBooleanWrapper editing;

	protected void setEditing(boolean value) {
		editingPropertyImpl().set(value);
	}

	public final boolean isEditing() {
		return editing == null ? false : editing.get();
	}

	public final ReadOnlyBooleanProperty editingProperty() {
		return editingPropertyImpl().getReadOnlyProperty();
	}

	protected ReadOnlyBooleanWrapper editingPropertyImpl() {
		if (editing == null) {
			editing = new ReadOnlyBooleanWrapper(this, "editing", false);
		}
		return editing;
	}

	// --- Editable
	private BooleanProperty editable;

	public final void setEditable(boolean value) {
		editableProperty().set(value);
	}

	public final boolean isEditable() {
		return editable == null ? true : editable.get();
	}

	public final BooleanProperty editableProperty() {
		if (editable == null) {
			editable = new SimpleBooleanProperty(this, "editable", true);
		}
		return editable;
	}

	public void startEdit() {
		if (isEditable() && !isEditing()) {
			setEditing(true);
		}
	}

	public void cancelEdit() {
		if (isEditing()) {
			setEditing(false);
		}
	}

	public void commitEdit(T newValue) {
		if (isEditing()) {
			setEditing(false);
		}
	}

	// --- Column
	private ReadOnlyObjectWrapper<Column<T>> column = new ReadOnlyObjectWrapper<Column<T>>() {
		@Override
		protected void invalidated() {
		}

		@Override
		public Object getBean() {
			return Cell.this;
		}

		@Override
		public String getName() {
			return "Column";
		}
	};

	public final ReadOnlyObjectProperty<Column<T>> tableColumnProperty() {
		return column.getReadOnlyProperty();
	}

	private void setTableColumn(Column<T> value) {
		column.set(value);
	}

	public final Column<T> getTableColumn() {
		return column.get();
	}

	public void updateTableColumn(Column<T> newCol) {
		Column<T> oldCol = getTableColumn();
		if (oldCol != null) {
			oldCol.getStyleClass().removeListener(weakColumnStyleClassListener);
			getStyleClass().removeAll(oldCol.getStyleClass());
			if (oldCol.getStyleFactory() != null) {
				oldCol.styleFactoryProperty().removeListener(weakColumnStyleFactrayListener);
				getStyleClass().remove(oldCol.getStyleFactory().toString(getItem()));
			}
			oldCol.alignmentProperty().removeListener(weakColumnAlignmentListener);
			oldCol.minWidthProperty().removeListener(weakColumnMinWidthListener);
			oldCol.prefWidthProperty().removeListener(weakColumnPrefWidthListener);
			oldCol.maxWidthProperty().removeListener(weakColumnMaxWidthListener);
			oldCol.visibleProperty().removeListener(weakColumnVisibleListener);
		}
		if (newCol == null) {
			return;
		}
		newCol.getStyleClass().addListener(weakColumnStyleClassListener);
		getStyleClass().addAll(newCol.getStyleClass());
		newCol.styleFactoryProperty().addListener(weakColumnStyleFactrayListener);
		if (newCol.getStyleFactory() != null && !header) {
			getStyleClass().add(newCol.getStyleFactory().toString(getItem()));
		}
		newCol.alignmentProperty().addListener(weakColumnAlignmentListener);
		setAlignment(newCol.getAlignment());
		newCol.minWidthProperty().addListener(weakColumnMinWidthListener);
		setMinWidth(newCol.getMinWidth());
		newCol.prefWidthProperty().addListener(weakColumnPrefWidthListener);
		setPrefWidth(newCol.getPrefWidth());
		newCol.maxWidthProperty().addListener(weakColumnMaxWidthListener);
		setMaxWidth(newCol.getMaxWidth());
		newCol.visibleProperty().addListener(weakColumnVisibleListener);
		setVisible(newCol.isVisible());

		setPrimary(newCol.isPrimary());
		setTableColumn((Column<T>) newCol);
	}

	private final WeakChangeListener<Boolean> weakColumnVisibleListener = new WeakChangeListener<>((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
		if (oldValue == newValue) {
			return;
		}
		this.setVisible(newValue);
	});
	private final WeakChangeListener<? super Number> weakColumnMinWidthListener = new WeakChangeListener<>((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
		if (oldValue == newValue) {
			return;
		}
		this.setMinWidth(newValue.doubleValue());
	});
	private final WeakChangeListener<? super Number> weakColumnPrefWidthListener = new WeakChangeListener<>((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
		if (oldValue == newValue) {
			return;
		}
		this.setPrefWidth(newValue.doubleValue());
	});
	private final WeakChangeListener<? super Number> weakColumnMaxWidthListener = new WeakChangeListener<>((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
		if (oldValue == newValue) {
			return;
		}
		this.setMaxWidth(newValue.doubleValue());
	});
	private final WeakChangeListener<Pos> weakColumnAlignmentListener = new WeakChangeListener<>((ObservableValue<? extends Pos> observable, Pos oldValue, Pos newValue) -> {
		if (oldValue == newValue) {
			return;
		}
		this.setAlignment(newValue);
	});
	private final WeakChangeListener<StringConverter<T>> weakColumnStyleFactrayListener = new WeakChangeListener<>((ObservableValue<? extends StringConverter<T>> observable, StringConverter<T> oldValue, StringConverter<T> newValue) -> {
		if (oldValue == newValue) {
			return;
		}
		this.getStyleClass().add(newValue.toString(getItem()));
	});
	private final WeakListChangeListener<String> weakColumnStyleClassListener = new WeakListChangeListener<String>(c -> {
		while (c.next()) {
			if (c.wasRemoved()) {
				getStyleClass().removeAll(c.getRemoved());
			}

			if (c.wasAdded()) {
				getStyleClass().addAll(c.getAddedSubList());
			}
		}
	});

	public void setRow(Row row) {
		this.row = row;
	}

	public void destory() {
		unbind();
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public boolean isHeader() {
		return header;
	}
}
