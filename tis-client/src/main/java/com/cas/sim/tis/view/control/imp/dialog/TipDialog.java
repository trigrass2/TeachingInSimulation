package com.cas.sim.tis.view.control.imp.dialog;

import com.sun.javafx.tk.Toolkit;

import de.felixroske.jfxsupport.GUIState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

@SuppressWarnings("restriction")
public class TipDialog {

	final Stage stage = new Stage() {
		@Override
		public void centerOnScreen() {
			Window owner = getOwner();
			if (owner != null) {
				positionStage();
			} else {
				if (getWidth() > 0 && getHeight() > 0) {
					super.centerOnScreen();
				}
			}
		}
	};

	protected Scene scene;

	private final Parent DUMMY_ROOT = new Region();
	protected Tip tip;

	protected double prefX = Double.NaN;
	protected double prefY = Double.NaN;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/
	public TipDialog() {
		initModality(Modality.NONE);
		initStyle(StageStyle.TRANSPARENT);
		initOwner(GUIState.getStage());
		stage.setResizable(false);

		stage.setOnCloseRequest(windowEvent -> {
			this.close();
			windowEvent.consume();
		});

		stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ESCAPE) {
				if (!keyEvent.isConsumed()) {
					this.close();
					keyEvent.consume();
				}
			}
		});
	}

	/**************************************************************************
	 * Public API
	 **************************************************************************/

	public void initStyle(StageStyle style) {
		stage.initStyle(style);
	}

	public StageStyle getStyle() {
		return stage.getStyle();
	}

	public void initOwner(Window newOwner) {
		updateStageBindings(stage.getOwner(), newOwner);
		stage.initOwner(newOwner);
	}

	public Window getOwner() {
		return stage.getOwner();
	}

	public void initModality(Modality modality) {
		stage.initModality(modality == null ? Modality.APPLICATION_MODAL : modality);
	}

	public Modality getModality() {
		return stage.getModality();
	}

	public void setTip(String msg) {
		tip.setTip(msg);
	}

	public void setDialogPane(Tip tip) {
		this.tip = tip;
		this.tip.setDialog(this);

		if (scene == null) {
			scene = new Scene(tip);
			scene.setFill(null);
			stage.setScene(scene);
		} else {
			scene.setRoot(tip);
		}

		tip.autosize();
		stage.sizeToScene();
	}

	public void show() {
		Toolkit.getToolkit().checkFxUserThread();

		if (Double.isNaN(tip.getWidth()) && Double.isNaN(tip.getHeight())) {
			sizeToScene();
		}

		scene.setRoot(tip);
		positionStage();
		stage.show();
	}

	public void showAndWait() {
		Toolkit.getToolkit().checkFxUserThread();

		if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
			throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
		}

		if (Double.isNaN(tip.getWidth()) && Double.isNaN(tip.getHeight())) {
			sizeToScene();
		}

		// this is slightly odd - we fire the SHOWN event before the show()
		// call, so that users get the event before the dialog blocks
		scene.setRoot(tip);
		positionStage();
		stage.showAndWait();
	}

	public void close() {
		if (stage.isShowing()) {
			stage.hide();
		}

		// Refer to RT-40687 for more context
		if (scene != null) {
			scene.setRoot(DUMMY_ROOT);
		}
	}

	public ReadOnlyBooleanProperty showingProperty() {
		return stage.showingProperty();
	}

	public Window getWindow() {
		return stage;
	}

	public Node getRoot() {
		return stage.getScene().getRoot();
	}

	// --- x
	public double getX() {
		return stage.getX();
	}

	public void setX(double x) {
		stage.setX(x);
	}

	public ReadOnlyDoubleProperty xProperty() {
		return stage.xProperty();
	}

	// --- y
	public double getY() {
		return stage.getY();
	}

	public void setY(double y) {
		stage.setY(y);
	}

	public ReadOnlyDoubleProperty yProperty() {
		return stage.yProperty();
	}

	ReadOnlyDoubleProperty heightProperty() {
		return stage.heightProperty();
	}

	void setHeight(double height) {
		stage.setHeight(height);
	}

	double getSceneHeight() {
		return scene == null ? 0 : scene.getHeight();
	}

	ReadOnlyDoubleProperty widthProperty() {
		return stage.widthProperty();
	}

	void setWidth(double width) {
		stage.setWidth(width);
	}

	StringProperty titleProperty() {
		return stage.titleProperty();
	}

	public void sizeToScene() {
		stage.sizeToScene();
	}

	/***************************************************************************
	 * Events
	 **************************************************************************/
	public void setOnShown(EventHandler<WindowEvent> value) {
		stage.setOnShown(value);
	}

	/**************************************************************************
	 * Private implementation
	 **************************************************************************/

	protected void positionStage() {
		double x = getX();
		double y = getY();

		// if the user has specified an x/y location, use it
		if (!Double.isNaN(x) && !Double.isNaN(y) && Double.compare(x, prefX) != 0 && Double.compare(y, prefY) != 0) {
			// weird, but if I don't call setX/setY here, the stage
			// isn't where I expect it to be (in instances where a single
			// dialog is shown and closed multiple times). I expect the
			// second showing to be in the place the dialog was when it
			// was closed the first time, but on Windows it jumps to the
			// top-left of the screen.
			setX(x);
			setY(y);
			return;
		}

		// Firstly we need to force CSS and layout to happen, as the dialogPane
		// may not have been shown yet (so it has no dimensions)
		tip.applyCss();
		tip.layout();

		final Window owner = getOwner();
		final Scene ownerScene = owner.getScene();

		// scene.getY() seems to represent the y-offset from the top of the titlebar to the
		// start point of the scene, so it is the titlebar height
		final double titleBarHeight = ownerScene.getY();

		// because Stage does not seem to centre itself over its owner, we
		// do it here.

		// then we can get the dimensions and position the dialog appropriately.
		final double dialogWidth = tip.prefWidth(-1);
		final double dialogHeight = tip.prefHeight(dialogWidth);

//       stage.sizeToScene();

		x = owner.getX() + (ownerScene.getWidth() - dialogWidth);
		y = owner.getY() - titleBarHeight + (ownerScene.getHeight() - dialogHeight);

		prefX = x;
		prefY = y;

		setX(x);
		setY(y);
	}

	// this method ensures the internal dialog stage is bound to the owner window
	// properties as appropriate
	private void updateStageBindings(Window oldOwner, Window newOwner) {
		final Scene dialogScene = stage.getScene();

		if (oldOwner != null && oldOwner instanceof Stage) {
			Stage oldStage = (Stage) oldOwner;
			Bindings.unbindContent(stage.getIcons(), oldStage.getIcons());

			Scene oldScene = oldStage.getScene();
			if (scene != null && dialogScene != null) {
				Bindings.unbindContent(dialogScene.getStylesheets(), oldScene.getStylesheets());
			}
		}

		// put the icons and stylesheets of the owner window into the dialog
		if (newOwner instanceof Stage) {
			Stage newStage = (Stage) newOwner;
			Bindings.bindContent(stage.getIcons(), newStage.getIcons());

			Scene newScene = newStage.getScene();
			if (scene != null && dialogScene != null) {
				Bindings.bindContent(dialogScene.getStylesheets(), newScene.getStylesheets());
			}
		}
	}

	public void setPrefSize(int width, int height) {
		tip.setPrefSize(width, height);
	}
}
