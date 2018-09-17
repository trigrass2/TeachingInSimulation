package com.cas.sim.tis.view.control.imp.table;

import java.util.function.Consumer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 注意：删除不会真的删除行，需要自行添加删除监听处理事件
 * @功能 BtnsCell.java
 * @作者 Administrator
 * @创建日期 2017年3月7日
 * @修改人 Administrator
 */
public class ToggelBtnCell<T>extends Cell<T> {
	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(String selectedName, String unselectName, String btnStlyeClass, Consumer<JSONObject> action, StringConverter<T> converter) {
		return column -> new ToggelBtnCell<T>(selectedName, unselectName, Priority.SOMETIMES, btnStlyeClass, action, converter);
	}

	public static <T> Callback<Column<T>, Cell<T>> forTableColumn(String selectedName, String unselectName, Priority priority, String btnStlyeClass, Consumer<JSONObject> action, StringConverter<T> converter) {
		return column -> new ToggelBtnCell<T>(selectedName, unselectName, priority, btnStlyeClass, action, converter);
	}

	private Row row;

	private ChangeListener<Boolean> listener;

	private ToggleButton btn = new ToggleButton();

	private String selectedName;
	private String unselectName;

	public ToggelBtnCell(String selectedName, String unselectName, Priority priority, String btnStlyeClass, Consumer<JSONObject> action, StringConverter<T> converter) {
		super(converter);
		this.setTooltip(null);
		this.setPadding(new Insets(0, 10, 0, 0));
		this.selectedName = selectedName;
		this.unselectName = unselectName;

		this.item = new SimpleObjectProperty<T>(this, "item") {
			@Override
			public void set(T newValue) {
				btn.setSelected(TypeUtils.castToBoolean(newValue));
				super.set(newValue);
			};
		};
		this.btn.setText(unselectName);
		this.btn.getStyleClass().add(btnStlyeClass);
		this.setGraphic(btn);
		HBox.setHgrow(this, priority);
		bind(action);
	}

	private void bind(Consumer<JSONObject> action) {
		this.listener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue == newValue) {
				return;
			}
			if (newValue) {
				this.btn.setText(selectedName);
			} else {
				this.btn.setText(unselectName);
			}
			if (row != null) {
				Object id = row.primaryCellProperty().get().getItem();
				JSONObject object = new JSONObject();
				object.put("id", id);
				object.put("selected", newValue);
				action.accept(object);
			}
		};
		this.btn.selectedProperty().addListener(listener);
	}

	public void destory() {
		super.destory();
		this.btn.selectedProperty().removeListener(listener);
	}

	public void setRow(Row row) {
		this.row = row;
	}
}
