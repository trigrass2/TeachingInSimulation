package com.jme3x.jfx;

import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JFxShadowTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Canvas canvas = new Canvas();
		canvas.setFocusTraversable(true);
		canvas.setOnMouseClicked(event -> canvas.requestFocus());
		
		StackPane pane = new StackPane(canvas);
		primaryStage.setScene(new Scene(pane));
		
		JmeShadowTest app = new JmeShadowTest();
		JmeToJFXIntegrator.startAndBind(app, canvas, Thread::new);
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
