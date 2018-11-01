package com.cas.sim.tis.view.control;

import com.cas.sim.tis.view.controller.PageController;

import javafx.scene.Node;

public interface IContent extends IDistory {
//	StackPane的content
	Node[] getContent();

	void onContentAttached(PageController pageController);
}
