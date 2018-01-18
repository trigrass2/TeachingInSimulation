package com.cas.sim.tis.view.control.imp.table;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class IconCell<T> extends Cell<T> {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(StringConverter<T> converter) {
		return column -> new IconCell<T>(converter);
	}
	
	private ImageView view;
	
	public IconCell(StringConverter<T> converter) {
		super(converter);
		view.setImage(new Image(converter.toString()));
		this.setGraphic(view);
	}
}
