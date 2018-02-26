package com.cas.sim.tis.test.office;

import javax.swing.JPanel;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WordViewerTest extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		SWTPanel swt = new SWTPanel();
		swt.setSize(1366, 768);
		JPanel panel = new JPanel();
		panel.setSize(1366, 768);
		panel.add(swt);
		
		SwingNode node = new SwingNode();
		node.setContent(panel);
		
		StackPane root = new StackPane(node);
		
		Scene scene = new Scene(root);
		scene.setFill(null);
		primaryStage.setScene(scene);
		primaryStage.setWidth(1366);
		primaryStage.setHeight(768);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
