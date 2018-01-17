package com.cas.sim.tis.view.control.imp.table;

import com.sun.javafx.scene.control.skin.Utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Column<T> {
	static final double DEFAULT_WIDTH = 80.0F;
	static final double DEFAULT_MIN_WIDTH = 10.0F;
	static final double DEFAULT_MAX_WIDTH = 5000.0F;

	public Column() {
	}

	private StringProperty key = new SimpleStringProperty(this, "key", "");

	public final StringProperty keyProperty() {
		return key;
	}

	public final void setKey(String value) {
		key.set(value);
	}

	public final String getKey() {
		return key.get();
	}

	private StringProperty text = new SimpleStringProperty(this, "text", null);

	public final StringProperty textProperty() {
		return text;
	}

	public final void setText(String value) {
		text.set(value);
	}

	public final String getText() {
		return text.get();
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

	// --- Editable
	private BooleanProperty editable;

	public final void setEditable(boolean value) {
		editableProperty().set(value);
	}

	public final boolean isEditable() {
		return editable == null ? true : editable.get();
	}

	private final BooleanProperty editableProperty() {
		if (editable == null) {
			editable = new SimpleBooleanProperty(this, "editable", true);
		}
		return editable;
	}

	// --- Width
	public final ReadOnlyDoubleProperty widthProperty() {
		return width.getReadOnlyProperty();
	}

	public final double getWidth() {
		return width.get();
	}

	void setWidth(double value) {
		width.set(value);
	}

	private ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper(this, "width", DEFAULT_WIDTH);

	// --- Minimum Width
	private DoubleProperty minWidth;

	public final void setMinWidth(double value) {
		minWidthProperty().set(value);
	}

	public final double getMinWidth() {
		return minWidth == null ? DEFAULT_MIN_WIDTH : minWidth.get();
	}

	public final DoubleProperty minWidthProperty() {
		if (minWidth == null) {
			minWidth = new SimpleDoubleProperty(this, "minWidth", DEFAULT_MIN_WIDTH) {
				@Override
				protected void invalidated() {
					if (getMinWidth() < 0) {
						setMinWidth(0.0F);
					}

					impl_setWidth(getWidth());
				}
			};
		}
		return minWidth;
	}

	// --- Preferred Width
	public final DoubleProperty prefWidthProperty() {
		return prefWidth;
	}

	public final void setPrefWidth(double value) {
		prefWidthProperty().set(value);
	}

	public final double getPrefWidth() {
		return prefWidth.get();
	}

	private final DoubleProperty prefWidth = new SimpleDoubleProperty(this, "prefWidth", DEFAULT_WIDTH) {
		@Override
		protected void invalidated() {
			impl_setWidth(getPrefWidth());
		}
	};

	// --- Maximum Width
	public final DoubleProperty maxWidthProperty() {
		return maxWidth;
	}

	public final void setMaxWidth(double value) {
		maxWidthProperty().set(value);
	}

	public final double getMaxWidth() {
		return maxWidth.get();
	}

	private DoubleProperty maxWidth = new SimpleDoubleProperty(this, "maxWidth", DEFAULT_MAX_WIDTH) {
		@Override
		protected void invalidated() {
			impl_setWidth(getWidth());
		}
	};

	public void impl_setWidth(double width) {
		setWidth(Utils.boundedSize(width, getMinWidth(), getMaxWidth()));
	}

	private ObjectProperty<Pos> alignment;

	/**
	 * 用于规定该列单元格对齐方式
	 * @return
	 */
	public final ObjectProperty<Pos> alignmentProperty() {
		if (alignment == null) {
			alignment = new SimpleObjectProperty<Pos>();
		}
		return alignment;
	}

	public final void setAlignment(Pos value) {
		alignmentProperty().set(value);
	}

	public final Pos getAlignment() {
		return alignment == null ? Pos.TOP_LEFT : alignment.get();
	}

	// --- Style class
	private final ObservableList<String> styleClass = FXCollections.observableArrayList();

	public ObservableList<String> getStyleClass() {
		return styleClass;
	}

	// --- Visible
	private BooleanProperty visible = new SimpleBooleanProperty(this, "visible", true){
		@Override
		public void set(boolean newValue) {
			if (!newValue) {
				setMaxWidth(0);
			}
			super.set(newValue);
		};
	};

	public final void setVisible(boolean value) {
		visibleProperty().set(value);
	}

	public final boolean isVisible() {
		return visible.get();
	}

	public final BooleanProperty visibleProperty() {
		return visible;
	}

	// --- CellFaactory
	public static final Callback<Column<?>, Cell<?>> DEFAULT_CELL_FACTORY = new Callback<Column<?>, Cell<?>>() {

		@Override
		public Cell<?> call(Column<?> param) {

			return new Cell<Object>(null);
		}
	};

	private final ObjectProperty<Callback<Column<T>, Cell<T>>> cellFactory = new SimpleObjectProperty<Callback<Column<T>, Cell<T>>>(this, "cellFactory", (Callback<Column<T>, Cell<T>>) ((Callback) DEFAULT_CELL_FACTORY));

	public final void setCellFactory(Callback<Column<T>, Cell<T>> value) {
		cellFactory.set(value);
	}

	public final Callback<Column<T>, Cell<T>> getCellFactory() {
		return cellFactory.get();
	}

	public final ObjectProperty<Callback<Column<T>, Cell<T>>> cellFactoryProperty() {
		return cellFactory;
	}

	private final ObjectProperty<StringConverter<T>> styleFactory = new SimpleObjectProperty<StringConverter<T>>(this, "styleFactory");

	public final void setStyleFactory(StringConverter<T> value) {
		styleFactory.set(value);
	}

	public final StringConverter<T> getStyleFactory() {
		return styleFactory.get();
	}

	public final ObjectProperty<StringConverter<T>> styleFactoryProperty() {
		return styleFactory;
	}
}
