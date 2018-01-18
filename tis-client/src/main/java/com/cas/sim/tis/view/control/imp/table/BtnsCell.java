package com.cas.sim.tis.view.control.imp.table;

import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
public class BtnsCell<T>extends Cell<T> {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn() {
		return column -> new BtnsCell<T>();
	}

	private static final ResourceBundle resources = ResourceBundle.getBundle("i18n/messages");
	private Row row;

	private ButtonBar bar = new ButtonBar();
	private Button edit = new Button(resources.getString("EDIT"));
	private Button del = new Button(resources.getString("DEL"));
	private Button save = new Button(resources.getString("SAVE"));
	private Button cancel = new Button(resources.getString("CANCEL"));
	private ChangeListener<Boolean> hoverListener;
	private ChangeListener<Boolean> editListener;

	public BtnsCell() {
		super(null);
		HBox.setHgrow(this, Priority.ALWAYS);
		bind();
	}

	private void bind() {
		edit.getStyleClass().add("black-round-btn");
		edit.setOnAction(event -> {
			row.setEditing(true);
		});
		del.getStyleClass().add("red-round-btn");
		del.setOnAction(event -> {
			row.commitDelete();
		});
		save.getStyleClass().add("black-round-btn");
		save.setOnAction(event -> {
			row.commitEdit();
			row.setEditing(false);
		});
		cancel.getStyleClass().add("red-round-btn");
		cancel.setOnAction(event -> {
			row.commitCancel();
			row.setEditing(false);
		});
		bar.setButtonMinWidth(50);
		hoverListener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue == newValue || isEditing()) {
				return;
			}
			if (newValue) {
				bar.getButtons().remove(save);
				bar.getButtons().remove(cancel);
				bar.getButtons().add(edit);
				bar.getButtons().add(del);
				this.setGraphic(bar);
			} else {
				bar.getButtons().remove(edit);
				bar.getButtons().remove(del);
				bar.getButtons().remove(save);
				bar.getButtons().remove(cancel);
				this.setGraphic(null);
			}
		};
		hoveringPropertyImpl().addListener(hoverListener);
		editListener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue == newValue) {
				return;
			}
			if (newValue) {
				bar.getButtons().remove(edit);
				bar.getButtons().remove(del);
				bar.getButtons().add(save);
				bar.getButtons().add(cancel);
				this.setGraphic(bar);
			} else if (isHover()) {
				bar.getButtons().remove(save);
				bar.getButtons().remove(cancel);
				bar.getButtons().add(edit);
				bar.getButtons().add(del);
				this.setGraphic(bar);
			} else {
				bar.getButtons().remove(edit);
				bar.getButtons().remove(del);
				bar.getButtons().remove(save);
				bar.getButtons().remove(cancel);
				this.setGraphic(null);
			}
		};
		editingPropertyImpl().addListener(editListener);
	}

	public void destory() {
		super.destory();
		this.edit.removeEventHandler(ActionEvent.ACTION, this.edit.getOnAction());
		this.del.removeEventHandler(ActionEvent.ACTION, this.del.getOnAction());
		this.save.removeEventHandler(ActionEvent.ACTION, this.save.getOnAction());
		this.cancel.removeEventHandler(ActionEvent.ACTION, this.cancel.getOnAction());
		hoveringPropertyImpl().removeListener(hoverListener);
		editingPropertyImpl().removeListener(editListener);
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
