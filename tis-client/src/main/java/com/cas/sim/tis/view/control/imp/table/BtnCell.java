package com.cas.sim.tis.view.control.imp.table;

import java.util.function.Consumer;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 * 注意：删除不会真的删除行，需要自行添加删除监听处理事件
 * @功能 BtnsCell.java
 * @作者 Administrator
 * @创建日期 2017年3月7日
 * @修改人 Administrator
 */
public class BtnCell<T>extends Cell<T> {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(String btnName, String btnStlyeClass, Consumer<Object> action) {
		return column -> new BtnCell<T>(btnName, Priority.SOMETIMES, btnStlyeClass, action);
	}
	
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(String btnName, Priority priority, String btnStlyeClass, Consumer<Object> action) {
		return column -> new BtnCell<T>(btnName, priority, btnStlyeClass, action);
	}

	private Row row;

	private Button btn = new Button();
	private ChangeListener<Boolean> hoverListener;

	public BtnCell(String btnName, Priority priority, String btnStlyeClass, Consumer<Object> action) {
		super(null);
		this.setTooltip(null);
		this.setPadding(new Insets(0, 10, 0, 0));
		btn.setText(btnName);
		btn.getStyleClass().add(btnStlyeClass);
		btn.setOnAction(event -> {
			Object id = row.primaryCellProperty().get().getItem();
			action.accept(id);
		});
		HBox.setHgrow(this, priority);
		bind();
	}

	private void bind() {
		hoverListener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue == newValue || isEditing()) {
				return;
			}
			if (newValue) {
				this.setGraphic(btn);
			} else {
				this.setGraphic(null);
			}
		};
		hoveringPropertyImpl().addListener(hoverListener);
	}

	public void destory() {
		super.destory();
		this.btn.removeEventHandler(ActionEvent.ACTION, this.btn.getOnAction());
		hoveringPropertyImpl().removeListener(hoverListener);
	}

	// --- Hovering
	private ReadOnlyBooleanWrapper hovering;

	public void setHovering(boolean value) {
		hoveringPropertyImpl().set(value);
	}

	public final boolean isHovering() {
		return hovering == null ? false : hovering.get();
	}

	public final ReadOnlyBooleanProperty hoveringProperty() {
		return hoveringPropertyImpl().getReadOnlyProperty();
	}

	private ReadOnlyBooleanWrapper hoveringPropertyImpl() {
		if (hovering == null) {
			hovering = new ReadOnlyBooleanWrapper(this, "hovering");
		}
		return hovering;
	}

	public void setRow(Row row) {
		this.row = row;
	}
}
