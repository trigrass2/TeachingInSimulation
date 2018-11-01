package com.cas.sim.tis.view.control.imp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class ElecFlowItem extends VBox {
	protected ObservableList<Text> items = FXCollections.observableArrayList();
	
	protected int index;
	
	public ElecFlowItem() {
		setSpacing(10);
		setPadding(new Insets(10));
		setPickOnBounds(false);
		setMouseTransparent(false);
		bindListener();
	}

	protected abstract void bindListener();
}
