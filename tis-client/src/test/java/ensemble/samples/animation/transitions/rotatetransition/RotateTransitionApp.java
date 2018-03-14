/* ....Show License.... */

package ensemble.samples.animation.transitions.rotatetransition;

import java.io.IOException;

import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.svg.SVGHelper;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A sample in which a node rotates around its center over a given time.
 */

public class RotateTransitionApp extends Application {

	private RotateTransition rotateTransition;

	public Parent createContent() {

		Pane root = new Pane();

		root.setPrefSize(140, 140);

		root.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

		root.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

		// create rectangle
		try {
			SVGHelper.loadGlyphsFont(RotateTransitionApp.class.getResource("/svg/iconfont.svg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SVGGlyph glyph = new SVGGlyph("iconfont.svg.clock", Color.RED, 22);
		root.getChildren().add(new Button("TEST", glyph));

		shaking(glyph);
		
		return root;

	}

	private void shaking(Node rect) {
		rotateTransition = new RotateTransition(Duration.millis(100), rect);
		rotateTransition.setFromAngle(-20);
		rotateTransition.setToAngle(20);
		rotateTransition.setAutoReverse(true);
		
		rotateTransition.setOnFinished(e -> {
			System.out.println(rect.getRotate());
			if (rotateTransition.getToAngle() == 20) {
				rotateTransition.setFromAngle(20);
				rotateTransition.setToAngle(0);
				rotateTransition.setCycleCount(1);
				rotateTransition.setDelay(Duration.ZERO);
				rotateTransition.playFromStart();
			} else {
				rotateTransition.setFromAngle(-20);
				rotateTransition.setToAngle(20);
				rotateTransition.setCycleCount(3);
				rotateTransition.setDelay(Duration.seconds(2));
				rotateTransition.playFromStart();
			}
		});
	}
	
	public void play() {

		rotateTransition.play();

	}

	@Override

	public void stop() {

		rotateTransition.stop();

	}

	@Override

	public void start(Stage primaryStage) throws Exception {

		primaryStage.setResizable(false);

		primaryStage.setScene(new Scene(createContent()));

		primaryStage.show();

		play();

	}

	/**
	 * Java main for when running without JavaFX launcher
	 */

	public static void main(String[] args) {

		launch(args);

	}

}