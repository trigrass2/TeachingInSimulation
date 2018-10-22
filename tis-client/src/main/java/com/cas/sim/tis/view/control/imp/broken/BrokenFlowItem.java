package com.cas.sim.tis.view.control.imp.broken;

import java.util.List;

import com.cas.sim.tis.view.control.imp.ElecFlowItem;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class BrokenFlowItem extends ElecFlowItem {

	@Override
	protected void bindListener() {
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
