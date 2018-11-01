package com.cas.sim.tis.view.control;

import com.cas.sim.tis.view.controller.PageController;

import javafx.scene.layout.Region;

public interface ILeftContent {
	Region getLeftContent();
	
	void onMenuAttached(PageController pageController);
}
