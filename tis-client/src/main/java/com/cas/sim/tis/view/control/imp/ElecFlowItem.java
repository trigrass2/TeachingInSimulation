package com.cas.sim.tis.view.control.imp;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ElecFlowItem extends VBox {
	protected ObservableList<Text> items = FXCollections.observableArrayList();
	
	protected int index;
	
	public ElecFlowItem() {
		setSpacing(10);
		setPadding(new Insets(10));
		setPickOnBounds(false);
		setMouseTransparent(false);
		
		getChildren().addListener(new ListChangeListener<Node>() {

			@Override
			public void onChanged(Change<? extends Node> c) {
				while (c.next()) {
					final List<? extends Node> added = c.getAddedSubList();
					final List<? extends Node> removed = c.getRemoved();
					if (c.wasRemoved()) {
						for (Node node : removed) {
							items.remove(node);
						}
					}
					if (c.wasAdded()) {
						for (Node node : added) {
							items.add((Text) node);
						}
					}
				}
				index = -1;
			}
		});
	}

}
