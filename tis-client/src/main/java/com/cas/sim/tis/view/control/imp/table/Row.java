package com.cas.sim.tis.view.control.imp.table;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cas.util.Util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class Row extends HBox {
	protected Table table;

	protected List<Cell<?>> cells = new ArrayList<Cell<?>>();

	protected String hoverStyleClass;
	protected String selectedStyleClass;

	protected BooleanProperty selected;

	private ChangeListener<Boolean> listener;

	public Row() {
		this("row", null, null);
	}

	public Row(String styleClass, String hoverStyleClass, String selectedStyleClass) {
		if (Util.notEmpty(styleClass)) {
			this.getStyleClass().add(styleClass);
		}
		this.hoverStyleClass = hoverStyleClass;
		this.selectedStyleClass = selectedStyleClass;
		bind();
	}

	private void bind() {
		this.setOnMouseEntered(event -> {
			entered();
		});
		this.setOnMouseExited(event -> {
			exited();
		});
		this.setOnMouseClicked(event -> {
			table.selectRow(Row.this);
		});
		// 使用弱引用监听
		listener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue == newValue) {
				return;
			}
			if (newValue) {
				table.setEditingRow(Row.this);
			}
			for (Cell<?> cell : cells) {
				if (cell.editableProperty().get()) {
					cell.setEditing(newValue);
				}
			}
		};
		editingProperty().addListener(listener);
	}

	private void unbind() {
		this.removeEventHandler(MouseEvent.MOUSE_EXITED, getOnMouseExited());
		this.removeEventHandler(MouseEvent.MOUSE_ENTERED, getOnMouseEntered());
		this.removeEventHandler(MouseEvent.MOUSE_CLICKED, getOnMouseClicked());
		this.editingProperty().removeListener(listener);
		for (Cell<?> cell : cells) {
			cell.destory();
		}
	}

	protected void entered() {
		if (Util.notEmpty(hoverStyleClass)) {
			getStyleClass().add(hoverStyleClass);
		}
		if (editingProperty().get()) {
			return;
		}
		for (Cell<?> cell : cells) {
			if (cell instanceof BtnsCell<?>) {
				((BtnsCell<?>) cell).setHovering(true);
			}
		}
	}

	protected void exited() {
		if (Util.notEmpty(hoverStyleClass)) {
			getStyleClass().remove(hoverStyleClass);
		}
		if (editingProperty().get()) {
			return;
		}
		for (Cell<?> cell : cells) {
			if (cell instanceof BtnsCell<?>) {
				((BtnsCell<?>) cell).setHovering(false);
			}
		}
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void addCell(Cell<?> cell) {
		cell.setRow(this);
		HBox.getHgrow(cell);
		this.getChildren().add(cell);
		this.layout();
		this.cells.add(cell);
		if (cell.isPrimary()) {
			setPrimaryCell(cell);
		}
	}

	public void commitEdit() {
		table.setCommitedRow(this);
	}

	public void commitDelete() {
		table.setDeleteRow(this);
	}

	public void commitCancel() {
		table.build();
	}

	public final void pseudoClassStateChanged(boolean selected) {
		if (selected) {
			getStyleClass().add(selectedStyleClass);
		} else {
			getStyleClass().remove(selectedStyleClass);
		}
	}

	public final void setSelected(boolean selected) {
		if (Util.notEmpty(selectedStyleClass)) {
			pseudoClassStateChanged(selected);
		}
		selectedProperty().set(selected);
	}

	public final boolean isSelected() {
		return selected == null ? false : selectedProperty().get();
	}

	public final BooleanProperty selectedProperty() {
		if (selected == null) {
			selected = new SimpleBooleanProperty();
		}
		return selected;
	}

	// --- Editing
	private ReadOnlyBooleanWrapper editing;

	public void setEditing(boolean value) {
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

	// -- Primary
	private ReadOnlyObjectWrapper<Cell<?>> primaryCell = new ReadOnlyObjectWrapper<>(this, "primaryCell");

	public final Cell<?> getPrimaryCell() {
		return primaryCell.get();
	}

	public final ReadOnlyObjectProperty<Cell<?>> primaryCellProperty() {
		return primaryCell.getReadOnlyProperty();
	}

	private final void setPrimaryCell(Cell<?> value) {
		primaryCell.set(value);
	}

	public JSONObject getItems() {
		JSONObject object = new JSONObject();
		try {
			for (Cell<?> cell : cells) {
				if (cell.isPrimary() || cell.isEditable()) {
					String key = String.valueOf(cell.getUserData());
					object.put(key, cell.getItem());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public String toString() {
		return getItems().toString();
	}

	public void destory() {
		unbind();
	}
}
