package com.cas.sim.tis.test.xdoc;

import javax.swing.JPanel;

import com.cas.sim.tis.view.control.imp.ResourceViewer;
import com.hg.xdoc.XDocViewer;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class XDocViewerTest extends AbstractJavaFxApplicationSupport {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		XDocViewer docViewer = new XDocViewer();
		docViewer.setBarVisible(false);
		System.out.println(docViewer.isLightweight());
		// FIXME
		docViewer.open("http://192.168.1.19:8082/Test/魔方墙.xlsx");
		SwingNode node = new SwingNode();
		JPanel panel = new JPanel();
		panel.add(docViewer);
		node.setContent(panel);

		ResourceViewer viewer = new ResourceViewer(null);
		viewer.setPrefWidth(1366);
		viewer.setPrefHeight(768);
		viewer.getViewer().getChildren().add(node);

		Scene scene = new Scene(viewer);
		scene.setFill(null);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
