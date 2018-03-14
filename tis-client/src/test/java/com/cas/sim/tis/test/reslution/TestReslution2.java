package com.cas.sim.tis.test.reslution;
import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;

public class TestReslution2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final Pane pane = new StackPane();
        Scene scene = new Scene(pane, 600, 300);
        stage.setScene(scene);
        Button b = new Button("Snap");
        final ImageView iv = new ImageView();
        iv.fitWidthProperty().bind(pane.widthProperty());
        iv.fitHeightProperty().bind(pane.heightProperty());
        pane.getChildren().add(iv);
        pane.getChildren().add(b);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        doSnap(iv);
                        display();
                    }
                });
            }
        });
        stage.show();
    }

    protected void doSnap(final ImageView iv) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        Robot robot = null;
        try {
            robot = new Robot(gs[gs.length-1]);
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }
        DisplayMode mode = gs[0].getDisplayMode();
        Rectangle bounds = new Rectangle(0, 0, mode.getWidth(), mode.getHeight());
        final BufferedImage bi = robot.createScreenCapture(bounds);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Image im = SwingFXUtils.toFXImage(bi, null);
                iv.setImage(im);
            }
        });
    }

    private void display() {
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = g.getDefaultScreenDevice();
		DisplayMode[] dm = sd.getDisplayModes();

		List<String> resolution = new ArrayList<>();
		for (int i = 0; i < dm.length; i++) {
			String res = String.format("%sx%s", dm[i].getWidth(), dm[i].getHeight());
			if (resolution.indexOf(res) == -1) {
				resolution.add(res);
			}
		}
		resolution.forEach(System.out::println);
	}
    
    public static void main(String[] args) {
        java.awt.Toolkit.getDefaultToolkit();
        Application.launch(args);
    }

}