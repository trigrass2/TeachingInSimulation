package com.cas.sim.tis.view.control.imp.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * 表头
 * @功能 Header.java
 * @作者 Caowj
 * @创建日期 2017年2月15日
 * @修改人 Caowj
 */
public class Header extends HBox {

	private final ObservableList<Cell<?>> cells = FXCollections.observableArrayList();

	public Header() {
		this.getStyleClass().add("header");
	}

	public void addHeaderCell(Cell<?> cell) {
		HBox.setHgrow(cell, Priority.ALWAYS);
		this.getChildren().add(cell);
		this.layout();
		this.getHeaderCells().add(cell);
	}

	public void addHeaderCell(int index, Cell<?> cell) {
		HBox.setHgrow(cell, Priority.ALWAYS);
		this.getChildren().add(cell);
		this.layout();
		this.getHeaderCells().add(index, cell);
	}

	public void removeHeaderCell(int index) {
		this.getHeaderCells().remove(index);
	}

	public final ObservableList<Cell<?>> getHeaderCells() {
		return cells;
	}
}
