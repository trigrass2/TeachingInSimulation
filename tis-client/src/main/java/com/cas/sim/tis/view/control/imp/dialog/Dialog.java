package com.cas.sim.tis.view.control.imp.dialog;

import java.util.Optional;

import com.sun.javafx.tk.Toolkit;

import de.felixroske.jfxsupport.GUIState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

@SuppressWarnings("restriction")
public class Dialog<R> {

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

	private Scene scene;

	private final Parent DUMMY_ROOT = new Region();
	private DialogPane<R> dialogPane;

	private double prefX = Double.NaN;
	private double prefY = Double.NaN;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	public Dialog() {
		initModality(Modality.APPLICATION_MODAL);
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

	public void setTitle(String title) {
		dialogPane.setTitle(title);
	}

	public void setDialogPane(DialogPane<R> dialogPane) {
		this.dialogPane = dialogPane;
		this.dialogPane.setDialog(this);

		if (scene == null) {
			scene = new Scene(dialogPane);
			stage.setScene(scene);
		} else {
			scene.setRoot(dialogPane);
		}

		dialogPane.autosize();
		stage.sizeToScene();
	}

	public void show() {
		Toolkit.getToolkit().checkFxUserThread();

		if (Double.isNaN(dialogPane.getWidth()) && Double.isNaN(dialogPane.getHeight())) {
			sizeToScene();
		}

		scene.setRoot(dialogPane);
		stage.centerOnScreen();
		stage.show();
	}

	public Optional<R> showAndWait() {
		Toolkit.getToolkit().checkFxUserThread();

		if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
			throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
		}

		if (Double.isNaN(dialogPane.getWidth()) && Double.isNaN(dialogPane.getHeight())) {
			sizeToScene();
		}

		// this is slightly odd - we fire the SHOWN event before the show()
		// call, so that users get the event before the dialog blocks
		scene.setRoot(dialogPane);
		stage.centerOnScreen();
		stage.showAndWait();

		return Optional.ofNullable(getResult());

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

	// --- result
	private final ObjectProperty<R> resultProperty = new SimpleObjectProperty<R>() {
		protected void invalidated() {
			close();
		}
	};

	/**
	 * A property representing what has been returned from the dialog. A result is generated through the {@link #resultConverterProperty() result converter}, which is intended to convert from the {@link ButtonType} that the user clicked on into a value of type R. Refer to the {@link Dialog} class JavaDoc for more details.
	 */
	public final ObjectProperty<R> resultProperty() {
		return resultProperty;
	}

	public final R getResult() {
		return resultProperty().get();
	}

	public final void setResult(R value) {
		this.resultProperty().set(value);
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

	BooleanProperty resizableProperty() {
		return stage.resizableProperty();
	}

	StringProperty titleProperty() {
		return stage.titleProperty();
	}

	ReadOnlyBooleanProperty focusedProperty() {
		return stage.focusedProperty();
	}

	public void sizeToScene() {
		stage.sizeToScene();
	}

	/**************************************************************************
	 * Private implementation
	 **************************************************************************/

	private void positionStage() {
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
		dialogPane.applyCss();
		dialogPane.layout();

		final Window owner = getOwner();
		final Scene ownerScene = owner.getScene();

		// scene.getY() seems to represent the y-offset from the top of the titlebar to the
		// start point of the scene, so it is the titlebar height
		final double titleBarHeight = ownerScene.getY();

		// because Stage does not seem to centre itself over its owner, we
		// do it here.

		// then we can get the dimensions and position the dialog appropriately.
		final double dialogWidth = dialogPane.prefWidth(-1);
		final double dialogHeight = dialogPane.prefHeight(dialogWidth);

//       stage.sizeToScene();

		x = owner.getX() + (ownerScene.getWidth() / 2.0) - (dialogWidth / 2.0);
		y = owner.getY() + titleBarHeight / 2.0 + (ownerScene.getHeight() / 2.0) - (dialogHeight / 2.0);

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
		dialogPane.setPrefSize(width, height);
	}
}
