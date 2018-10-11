package com.cas.sim.tis.view.control.imp.typical;

import com.cas.sim.tis.flow.Step;
import com.jme3.scene.Spatial;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StepItem extends Text {
	protected ObjectProperty<State> state;

	private int index;
	private Spatial mdl;

	public enum State {
		DONE, DOING, TODO
	}

	public StepItem(int index, Step step) {
		this.mdl = step.getModel();
		setText(step.getName());
		setFill(Color.WHITE);
		setWrappingWidth(280);
//		setStyle("-fx-font-size:14px;");
	}

	public final void setState(State state) {
		stateProperty().set(state);
	}

	public final State getState() {
		return stateProperty().get();
	}

	public final ObjectProperty<State> stateProperty() {
		if (state == null) {
			state = new SimpleObjectProperty<State>() {
				@Override
				public void set(State newValue) {
					super.set(newValue);
					if (State.DOING == newValue) {
						setFill(Color.web("#f6a23f"));
						getStyleClass().add("bold");
					} else if(State.TODO == newValue){
						setFill(Color.WHITE);
						getStyleClass().remove("bold");
					} else if (State.DONE == newValue) {
						setFill(Color.GRAY);
						getStyleClass().remove("bold");
					}
				}
			};
		}
		return state;
	}

	public int getIndex() {
		return index;
	}

	public Spatial getMdl() {
		return mdl;
	}
}
