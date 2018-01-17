package com.cas.sim.tis.view.control.imp.jme;

import com.cas.sim.tis.view.control.ICanvasPossess;
import com.cas.sim.tis.view.control.IContent;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class Recongnize3D implements IContent, ICanvasPossess {

	private Canvas canvas;

	@Override
	public Region getContent() {
		StackPane stackPane = new StackPane();
//		创建一个Canva层s，用于显示JME
		canvas = new Canvas(1113, 718);
		stackPane.getChildren().add(canvas);
//		创建一个菜单层

		return stackPane;
	}

	@Override
	public Canvas getCanvas() {
		return canvas;
	}
}
