package com.cas.sim.tis.view.control.imp.broken;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BrokenItem extends Text {

	public BrokenItem(String desc) {
		setText(desc);
		setFill(Color.WHITE);
		setWrappingWidth(280);
	}

}
