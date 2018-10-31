package com.cas.sim.tis.view.control.imp.typical;

import java.util.List;

import com.cas.sim.tis.view.control.imp.ElecFlowItem;
import com.cas.sim.tis.view.control.imp.typical.StepItem.State;
import com.jme3.scene.Spatial.CullHint;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

public class TypicalFlowItem extends ElecFlowItem {

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

	private final ReadOnlyObjectWrapper<StepItem> selected = new ReadOnlyObjectWrapper<StepItem>() {
		@Override
		public void set(final StepItem newSelectedRow) {
			int index = items.indexOf(newSelectedRow);
			for (int i = 0; i < items.size(); i++) {
				StepItem item = (StepItem) items.get(i);
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
			StepItem item = (StepItem) items.get(--index);
			scroll.setVvalue((item.getBoundsInParent().getMaxY() - item.getBoundsInLocal().getHeight()) / getHeight());
			selected.set(item);
		}
	}

	public void next(ScrollPane scroll) {
		if (index >= items.size() - 1) {
			for (int i = 0; i < items.size(); i++) {
				StepItem item = (StepItem) items.get(i);
				item.getMdl().setCullHint(CullHint.Dynamic);
				item.setState(State.DONE);
			}
		} else {
			StepItem item = (StepItem) items.get(++index);
			scroll.setVvalue((item.getBoundsInParent().getMaxY() + item.getBoundsInLocal().getHeight()) / getHeight());
			selected.set(item);
		}
	}
}
