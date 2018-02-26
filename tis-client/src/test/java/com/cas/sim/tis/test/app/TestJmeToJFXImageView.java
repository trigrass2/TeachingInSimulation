package com.cas.sim.tis.test.app;

import org.jetbrains.annotations.NotNull;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.injfx.JmeToJFXApplication;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The test to show how to integrate jME to ImageView.
 *
 * @author JavaSaBr
 */
public class TestJmeToJFXImageView extends Application {

    public static void main(@NotNull final String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull final Stage stage) {

        final ImageView imageView = new ImageView();
        imageView.setFocusTraversable(true);
        imageView.setOnMouseClicked(event -> imageView.requestFocus());

        final StackPane stackPane = new StackPane(imageView);
        final Scene scene = new Scene(stackPane, 600, 600);

        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        imageView.fitHeightProperty().bind(stackPane.heightProperty());

        stage.setTitle("Test");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> System.exit(0));

        // creates jME application
        final JmeToJFXApplication application = makeJmeApplication();

        // integrate jME application with ImageView
        JmeToJFXIntegrator.startAndBindMainViewPort(application, imageView, Thread::new);
    }

    private static @NotNull JmeToJFXApplication makeJmeApplication() {

        final AppSettings settings = JmeToJFXIntegrator.prepareSettings(new AppSettings(true), 60);
        final JmeToJFXApplication application = new JmeToJFXApplication() {

            protected Spatial player;
            @Override
            public void simpleInitApp() {
                super.simpleInitApp();
//        		stateManager.attach(new SceneCameraState());
                float aspect = (float) cam.getWidth() / cam.getHeight();
//              flyCam.setDragToRotate(true);
                cam.setFrustumPerspective(45f, aspect, 0.01f, 1000);
                assetManager.registerLocator("E:\\JME_SDKPROJ_HOME\\robot_assets\\assets\\", FileLocator.class);
        		assetManager.registerLocator("http://192.168.1.19:8082/assets/", UrlLocator.class);
        		player = assetManager.loadModel("Models\\elecComp\\DZ47LEC32.j3o");
//        		player = assetManager.loadModel("Model/RT28N-32X/RT28N-32X.j3o");
        		
//                Box b = new Box(1, 1, 1);
//                player = new Geometry("Player", b);
//                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//                mat.setColor("Color", ColorRGBA.Blue);
//                player.setMaterial(mat);
                rootNode.attachChild(player);
            }
        };
        application.setSettings(settings);
        application.setShowSettings(false);
        return application;
    }
}
