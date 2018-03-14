package com.jme3x.jfx;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cas.sim.tis.util.JmeUtil;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.injfx.JmeToJFXApplication;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.jme3x.jfx.injfx.input.JFXMouseInput;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The test to show how to integrate jME to Canvas.
 *
 * @author JavaSaBr
 */
public class TestJmeToJFXCanvas extends Application {

    public static void main(@NotNull final String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull final Stage stage) {

        final Canvas canvas = new Canvas();
        
        canvas.getProperties().put(JFXMouseInput.PROP_USE_LOCAL_COORDS, true);
        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(event -> canvas.requestFocus());

        final Button button = new Button("BUTTON");
        final StackPane stackPane = new StackPane(canvas, button);
        final Scene scene = new Scene(stackPane, 600, 600);

        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        stage.setTitle("Test");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> System.exit(0));

        // creates jME application
        final JmeToJFXApplication application = makeJmeApplication();

        // integrate jME application with Canvas
        JmeToJFXIntegrator.startAndBindMainViewPort(application, canvas, Thread::new);
    }

    private static @NotNull JmeToJFXApplication makeJmeApplication() {

        final AppSettings settings = JmeToJFXIntegrator.prepareSettings(new AppSettings(true), 60);
        final JmeToJFXApplication application = new JmeToJFXApplication() {

            protected Geometry player;
            Boolean isRunning = true;

            @Override
            public void simpleInitApp() {
                super.simpleInitApp();
                flyCam.setDragToRotate(true);
                Box b = new Box(1, 1, 1);
                player = new Geometry("Player", b);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Blue);
                player.setMaterial(mat);
                rootNode.attachChild(player);
                
//        		添加鼠标监听
                inputManager.addMapping("pick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        		inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
        			if (isPressed) {
        				System.out.println(inputManager.getCursorPosition());
        				System.out.println(settings.getWidth());
        				System.out.println(settings.getHeight());
        				
//        				@Nullable
//        				Geometry picked = JmeUtil.getGeometryFromCursor(root, cam, inputManager);
//        				if (picked != null) {
//        					System.err.println(picked);
//        				}
        				@Nullable
        				Vector3f point = JmeUtil.getContactPointFromCursor(rootNode, cam, inputManager);
        				if(point != null) {
        					Geometry ball = JmeUtil.getSphere(assetManager, 32, 0.1f, ColorRGBA.Red);
        					rootNode.attachChild(ball);
        					ball.setLocalTranslation(point);
        				}
        			}
        		}, "pick");

//                initKeys(); // load my custom keybinding
            }

//            /** Custom Keybinding: Map named actions to inputs. */
//            private void initKeys() {
//                /** You can map one or several inputs to one named mapping. */
//                inputManager.addMapping("Pause", new KeyTrigger(keyInput.KEY_P));
//                inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
//                inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
//                inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), // spacebar!
//                        new MouseButtonTrigger(MouseInput.BUTTON_LEFT));        // left click!
//                /** Add the named mappings to the action listeners. */
//                inputManager.addListener(actionListener, "Pause");
//                inputManager.addListener(analogListener, "Left", "Right", "Rotate");
//            }
//
//            /** Use this listener for KeyDown/KeyUp events */
//            private ActionListener actionListener = (name, keyPressed, tpf) -> {
//                if (name.equals("Pause") && !keyPressed) {
//                    isRunning = !isRunning;
//                }
//            };
//
//            /** Use this listener for continuous events */
//            private AnalogListener analogListener = new AnalogListener() {
//                public void onAnalog(String name, float value, float tpf) {
//                    if (isRunning) {
//                        if (name.equals("Rotate")) {
//                            player.rotate(0, value, 0);
//                        }
//                        if (name.equals("Right")) {
//                            player.move((new Vector3f(value, 0, 0)));
//                        }
//                        if (name.equals("Left")) {
//                            player.move(new Vector3f(-value, 0, 0));
//                        }
//                    } else {
//                        System.out.println("Press P to unpause.");
//                    }
//                }
//            };
        };

        application.setSettings(settings);
        application.setShowSettings(false);

        return application;
    }
}