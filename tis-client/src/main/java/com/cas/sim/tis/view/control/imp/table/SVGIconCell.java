package com.cas.sim.tis.view.control.imp.table;

import java.util.function.Function;

import com.cas.sim.tis.svg.SVGGlyph;

import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;

public class SVGIconCell<T>extends Cell<T> {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(Function<Integer, SVGGlyph> converter) {
		return column -> new SVGIconCell<T>(converter);
	}

	public SVGIconCell(Function<Integer, SVGGlyph> converter) {
		super(null);
		this.setTooltip(null);
		this.item = new SimpleObjectProperty<T>(this, "item") {
			@Override
			public void set(T newValue) {
				setGraphic(converter.apply((Integer) newValue));
				super.set(newValue);
			};
		};
	}

}
