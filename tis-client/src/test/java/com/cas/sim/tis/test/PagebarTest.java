package com.cas.sim.tis.test;

import com.cas.sim.tis.view.control.imp.pagination.PaginationBar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PagebarTest extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		PaginationBar pagination = new PaginationBar();
		pagination.setPageCount(1);
		Scene scene = new Scene(pagination);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		pagination.getPageBtn().getToggles().get(0).setSelected(true);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
