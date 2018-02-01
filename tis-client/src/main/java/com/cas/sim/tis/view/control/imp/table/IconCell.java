package com.cas.sim.tis.view.control.imp.table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class IconCell<T>extends Cell<T> {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(StringConverter<T> converter) {
		return column -> new IconCell<T>(converter);
	}

	private ImageView view = new ImageView();

	public IconCell(StringConverter<T> converter) {
		super(converter);
		view.setFitHeight(25);
		view.setFitWidth(25);
		this.setGraphic(view);
		this.setTooltip(null);
		this.item = new SimpleObjectProperty<T>(this, "item") {
			@Override
			public void set(T newValue) {
				if (getConverter() != null && getConverter().toString(newValue) != null) {
					view.setImage(new Image(getConverter().toString(newValue)));
				}
				super.set(newValue);
			};
		};
	}

}
