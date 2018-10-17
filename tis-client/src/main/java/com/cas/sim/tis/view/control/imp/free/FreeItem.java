package com.cas.sim.tis.view.control.imp.free;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FreeItem extends Text {

	public FreeItem(String desc) {
		setText(desc);
		setFill(Color.WHITE);
		setWrappingWidth(280);
	}

}
