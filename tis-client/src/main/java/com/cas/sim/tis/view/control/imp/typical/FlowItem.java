package com.cas.sim.tis.view.control.imp.typical;

import java.util.List;

import com.cas.sim.tis.view.control.imp.typical.StepItem.State;
import com.jme3.scene.Spatial.CullHint;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class FlowItem extends VBox {
	private ObservableList<StepItem> items = FXCollections.observableArrayList();

	private int index;

	public FlowItem() {
		setSpacing(10);
		setPadding(new Insets(10));
//		setStyle("-fx-background-radius:5px");
//		getStyleClass().add("cover");
		getChildren().addListener(new ListChangeListener<Node>() {

			@Override
			public void onChanged(Change<? extends Node> c) {
				while (c.next()) {
					final List<? extends Node> added = c.getAddedSubList();
					final List<? extends Node> removed = c.getRemoved();
					if (c.wasRemoved()) {
						for (Node node : removed) {
							items.remove((StepItem) node);
						}
					}
					if (c.wasAdded()) {
						for (Node node : added) {
							items.add((StepItem) node);
						}
					}
				}
				index = -1;
			}
		});
	}

	private final ReadOnlyObjectWrapper<StepItem> selected = new ReadOnlyObjectWrapper<StepItem>() {
		@Override
		public void set(final StepItem newSelectedRow) {
			int index = items.indexOf(newSelectedRow);
			for (int i = 0; i < items.size(); i++) {
				StepItem item = items.get(i);
				if (i == index) {
					item.getMdl().setCullHint(CullHint.Always);
					item.setState(State.DOING);
				} else if (i < index) {
					item.getMdl().setCullHint(CullHint.Dynamic);
					item.setState(State.DONE);
				} else {
					item.getMdl().setCullHint(CullHint.Always);
					item.setState(State.TODO);
				}
			}
			super.set(newSelectedRow);
		}
	};

	public final StepItem getSelectedStepItem() {
		return selected.get();
	}

	public final ReadOnlyObjectProperty<StepItem> selectedStepItemProperty() {
		return selected.getReadOnlyProperty();
	}

	public final void selectStepItem(StepItem value) {
		selected.set(value);
	}

	public final void clearSelectedStepItem() {
		if (selected.get() == null) {
			return;
		}
		selected.get().setState(State.TODO);
		selected.set(null);
	}

	public void prev(ScrollPane scroll) {
		if (index <= 0) {
			selected.set(null);
		} else {
			StepItem item = items.get(--index);
			scroll.setVvalue((item.getLayoutY() + item.getBoundsInLocal().getHeight()) / getHeight());
			selected.set(item);
		}
	}

	public void next(ScrollPane scroll) {
		if (index >= items.size() - 1) {
			for (int i = 0; i < items.size(); i++) {
				StepItem item = items.get(i);
				item.getMdl().setCullHint(CullHint.Dynamic);
				item.setState(State.DONE);
			}
			index = items.size();
		} else {
			StepItem item = items.get(++index);
			scroll.setVvalue((item.getLayoutY() + item.getBoundsInLocal().getHeight()) / getHeight());
			selected.set(item);
		}
	}
}
