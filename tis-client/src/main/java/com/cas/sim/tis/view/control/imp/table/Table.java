package com.cas.sim.tis.view.control.imp.table;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cas.util.Util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Table extends VBox {
	private ObservableList<Row> rows = FXCollections.observableArrayList();

	protected String normalStyleClass;
	protected String hoverStyleClass;
	protected String selectedStyleClass;

	private VBox content = new VBox(10);

	public Table() {
		this(null, null, null);
	}

	public Table(String normalStyleClass, String hoverStyleClass, String selectedStyleClass) {
		this.getStyleClass().add("table");
		this.getColumns().addListener(weakColumnsObserver);

		this.normalStyleClass = normalStyleClass;
		this.hoverStyleClass = hoverStyleClass;
		this.selectedStyleClass = selectedStyleClass;

	}

	// --- Headerable
	private BooleanProperty headerable;

	public final void setHeaderable(boolean value) {
		headerableProperty().set(value);
	}

	public final boolean isHeaderable() {
		return headerable == null ? true : headerable.get();
	}

	public final BooleanProperty headerableProperty() {
		if (headerable == null) {
			headerable = new SimpleBooleanProperty(this, "headerable", true);
		}
		return headerable;
	}

	// --- Separatorable
	private BooleanProperty separatorable;

	public final void setSeparatorable(boolean value) {
		separatorableProperty().set(value);
	}

	public final boolean isSeparatorable() {
		return separatorable == null ? true : separatorable.get();
	}

	public final BooleanProperty separatorableProperty() {
		if (separatorable == null) {
			separatorable = new SimpleBooleanProperty(this, "separatorable", true);
		}
		return separatorable;
	}

	// --- Selected
	private final ReadOnlyObjectWrapper<Row> selected = new ReadOnlyObjectWrapper<Row>() {
		@Override
		public void set(final Row newSelectedRow) {
			final Row old = get();
			if (newSelectedRow == null) {
				if (old != null) {
					old.setSelected(false);
				}
				super.set(null);
				return;
			}
			if (old == newSelectedRow && newSelectedRow.isSelected()) {
				newSelectedRow.setSelected(false);
				super.set(null);
				return;
			}
			for (Row row : rows) {
				if (newSelectedRow == row) {
					row.setSelected(true);
				} else {
					row.setSelected(false);
				}
			}
			super.set(newSelectedRow);
		}
	};

	public final Row getSelectedRow() {
		return selected.get();
	}

	public final ReadOnlyObjectProperty<Row> selectedRowProperty() {
		return selected.getReadOnlyProperty();
	}

	public final void selectRow(Row value) {
		selected.set(value);
	}

	public final void clearSelectedRow() {
		if (selected.get() == null) {
			return;
		}
		selected.get().setSelected(false);
		selected.set(null);
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

	// --- Editing
	private final ReadOnlyObjectWrapper<Row> editingRow = new ReadOnlyObjectWrapper<Row>() {
		@Override
		public void set(final Row newEditingRow) {
			final Row old = get();
			if (old == newEditingRow) {
				return;
			}
			for (Row row : rows) {
				if (newEditingRow == row) {
					super.set(newEditingRow);
				} else {
					row.setEditing(false);
				}
			}
		}
	};

	public final Row getEditingRow() {
		return editingRow.get();
	}

	public final ReadOnlyObjectProperty<Row> editingRowProperty() {
		return editingRow.getReadOnlyProperty();
	}

	public final void setEditingRow(Row value) {
		editingRow.set(value);
	}

	public final void clearEditingRow() {
		if (!editingRow.getValue().isEditing()) {
			for (Row row : rows) {
				if (row.isEditing()) {
					return;
				}
			}
		}
		editingRow.set(null);
	}

	/**
	 * 当前表单被修改保存的行
	 */
	private final ReadOnlyObjectWrapper<Row> commitedRow = new ReadOnlyObjectWrapper<Row>() {
		@Override
		public void set(Row newValue) {
			JSONArray items = new JSONArray();
			for (Row row : rows) {
				JSONObject obj = row.getItems();
				items.add(obj);
			}
			setItems(items);
			super.set(newValue);
		};
	};

	public final Row getCommitedRow() {
		return commitedRow.get();
	}

	public final ReadOnlyObjectProperty<Row> commitedRowProperty() {
		return commitedRow.getReadOnlyProperty();
	}

	public final void setCommitedRow(Row value) {
		commitedRow.set(value);
	}

	private ObjectProperty<Insets> rowPadding = new SimpleObjectProperty<Insets>(Insets.EMPTY);

	public final void setRowPadding(Insets value) {
		rowPadding.set(value);
	}

	public final Insets getRowPadding() {
		return rowPadding.get();
	}

	public final ObjectProperty<Insets> rowpPaddingProperty() {
		return rowPadding;
	}

	// --- Height
	private SimpleDoubleProperty rowHeight = new SimpleDoubleProperty(-1);

	public final SimpleDoubleProperty rowHeightProperty() {
		return rowHeight;
	}

	public final double getRowHeight() {
		return rowHeight.get();
	}

	public final void setRowHeight(double value) {
		rowHeight.set(value);
	}

	// --- Indexable
	private SimpleBooleanProperty serial = new SimpleBooleanProperty(false);

	public final SimpleBooleanProperty serialProperty() {
		return serial;
	}

	public final boolean getSerial() {
		return serial.get();
	}

	public final void setSerial(boolean serial) {
		this.serial.set(serial);
	}

	private void addHeader(Header header) {
		header.setSpacing(10);
		header.setAlignment(Pos.CENTER_LEFT);
		header.setPadding(getRowPadding());
		VBox.setVgrow(header, Priority.ALWAYS);
		this.getChildren().add(0, header);
		this.layout();
	}

	private void addRow(Row row) {
		row.setTable(this);
		row.setSpacing(10);
		row.setAlignment(Pos.CENTER_LEFT);
		row.setPadding(getRowPadding());
		VBox.setVgrow(row, Priority.ALWAYS);
//		this.getChildren().add(row);
		this.content.getChildren().add(row);
		this.layout();
		this.rows.add(row);
	}

	private void addSeparator() {
		Separator separator = new Separator();
		separator.setMaxHeight(1);
		separator.getStyleClass().add("lightgray-separator");
		VBox.setVgrow(separator, Priority.ALWAYS);
//		this.getChildren().add(separator);
		this.content.getChildren().add(separator);
		this.layout();
	}

	private final ObservableList<Column<?>> columns = FXCollections.observableArrayList();

	public final ObservableList<Column<?>> getColumns() {
		return columns;
	}

	public final ObjectProperty<JSONArray> itemsProperty() {
		return items;
	}

	private ObjectProperty<JSONArray> items = new SimpleObjectProperty<JSONArray>(this, "items") {
		WeakReference<JSONArray> oldItemsRef;

		@Override
		protected void invalidated() {
			final JSONArray oldItems = oldItemsRef == null ? null : oldItemsRef.get();
			final JSONArray newItems = getItems();

			if (newItems != null && newItems == oldItems) {
				return;
			}

			oldItemsRef = new WeakReference<>(newItems);
		}
	};

	public final void setItems(JSONArray value) {
		itemsProperty().set(value);
	}

	public final JSONArray getItems() {
		return items.get();
	}

	@SuppressWarnings("unchecked")
	private void loadHeader() {
		Header header = new Header();
		this.addHeader(header);
		if (getSerial()) {
			Cell<String> index = new Cell<>(null);
			index.setMinWidth(20);
			index.setAlignment(Pos.CENTER_RIGHT);
			header.addHeaderCell(index);
		}
		for (Column<?> column : getColumns()) {
			String key = column.getKey();
			Object value = column.getText();
			Cell<Object> cell = new Cell<>(null);
			if (Util.notEmpty(key)) {
				cell.setUserData(key);
			}
			cell.setItem(value);
			if (getRowHeight() != -1) {
				cell.setMinHeight(getRowHeight());
				cell.setMaxHeight(getRowHeight());
				cell.setPrefHeight(getRowHeight());
			}
			cell.updateTableColumn((Column<Object>) column);
			header.addHeaderCell(cell);
		}
		if (isSeparatorable()) {
			addSeparator();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadItems(JSONArray newItems) {
		// 添加新的数据
		if (newItems == null) {
			return;
		}
		try {
			content.getChildren().clear();
			ScrollPane scrollPane = new ScrollPane(content);
			scrollPane.setFitToWidth(true);
			scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
			this.getChildren().add(scrollPane);
			for (int i = 0; i < newItems.size(); i++) {
				JSONObject object = newItems.getJSONObject(i);
				Row row = null;
				row = new Row(normalStyleClass, hoverStyleClass, selectedStyleClass);
				row.setEditable(isEditable());
				this.addRow(row);
				if (getSerial()) {
					Cell<String> index = new Cell<>(null);
					index.setItem((i + 1) + ".");
					index.setMinWidth(20);
					index.setAlignment(Pos.CENTER_RIGHT);
					row.addCell(index);
				}
				for (Column<?> column : getColumns()) {
					String key = column.getKey();
					Cell<Object> cell = ((Column<Object>) column).cellFactoryProperty().get().call((Column<Object>) column);
					if (Util.notEmpty(key)) {
						cell.setUserData(key);
						cell.setItem(object.get(key));
					}
					if (getRowHeight() != -1) {
						cell.setPrefHeight(getRowHeight());
					}
					cell.updateTableColumn((Column<Object>) column);
					row.addCell(cell);
				}
				if (isSeparatorable()) {
					addSeparator();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final ListChangeListener<Column<?>> columnsObserver = new ListChangeListener<Column<?>>() {
		@Override
		public void onChanged(Change<? extends Column<?>> c) {
			final List<Column<?>> columns = getColumns();

			while (c.next()) {
				if (c.wasAdded()) {
					List<Column<?>> duplicates = new ArrayList<>();
					List<Column<?>> primaries = new ArrayList<>();
					for (Column<?> addedColumn : c.getAddedSubList()) {
						if (addedColumn == null) continue;

						int count = 0;
						int primary = 0;
						for (Column<?> column : columns) {
							if (addedColumn == column) {
								count++;
							}
							if (column.isPrimary()) {
								primary++;
							}
						}

						if (count > 1) {
							duplicates.add(addedColumn);
						}
						if (primary > 1) {
							primaries.add(addedColumn);
						}
					}

					if (!duplicates.isEmpty()) {
						String titleList = "";
						for (Column<?> dupe : duplicates) {
							titleList += "'" + dupe.getText() + "', ";
						}
						throw new IllegalStateException("Duplicate Columns detected in Table columns list with titles " + titleList);
					}
					if (!primaries.isEmpty()) {
						String titleList = "";
						for (Column<?> primary : primaries) {
							titleList += "'" + primary.getText() + "', ";
						}
						throw new IllegalStateException("Duplicate Primary TColumns detected in Table columns list with titles " + titleList);
					}
				}
			}
			c.reset();
		}
	};

	private final WeakListChangeListener<Column<?>> weakColumnsObserver = new WeakListChangeListener<Column<?>>(columnsObserver);

	// --- Delete
	private final ReadOnlyObjectWrapper<Row> deleteRow = new ReadOnlyObjectWrapper<Row>()
//	{
//
//		@Override
//		public void set(final Row newValue) {
//			if (newValue == null) {
//				return;
//			}
//			if (rows.contains(newValue)) {
//				rows.remove(newValue);
//				JSONArray value = new JSONArray();
//				for (Row row : rows) {
//					JSONObject obj = row.getItems();
//					value.put(obj);
//				}
//				setItems(value);
//			}
//			build();
//			super.set(newValue);
//		};
//	}
	;

	public final Row getDeleteRow() {
		return deleteRow.get();
	}

	public final ReadOnlyObjectProperty<Row> deleteRowProperty() {
		return deleteRow.getReadOnlyProperty();
	}

	public final void setDeleteRow(Row value) {
		deleteRow.set(value);
	}

	public void updateEditingRow(Row editing) {
		for (Row row : rows) {
			if (row == editing) {
				row.setEditing(true);
			} else {
				row.setEditing(false);
			}
		}
	}

	/**
	 * 在表格创建完成之后必须调用
	 */
	public void build() {
		this.getChildren().clear();
		for (Row row : rows) {
			row.destory();
		}
		this.rows.clear();
		if (isHeaderable()) {
			loadHeader();
		}
		loadItems(getItems());
	}

	public String getNormalStyleClass() {
		return normalStyleClass;
	}

	public void setNormalStyleClass(String normalStyleClass) {
		this.normalStyleClass = normalStyleClass;
	}

	public String getHoverStyleClass() {
		return hoverStyleClass;
	}

	public void setHoverStyleClass(String hoverStyleClass) {
		this.hoverStyleClass = hoverStyleClass;
	}

	public String getSelectedStyleClass() {
		return selectedStyleClass;
	}

	public void setSelectedStyleClass(String selectedStyleClass) {
		this.selectedStyleClass = selectedStyleClass;
	}

	public void destory() {
		for (Row row : rows) {
			row.destory();
		}
	}
}
