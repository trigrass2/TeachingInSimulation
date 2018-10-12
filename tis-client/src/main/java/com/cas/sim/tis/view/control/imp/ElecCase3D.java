package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.util.ResourceBundle;

import com.cas.sim.tis.anno.FxThread;
import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.entity.ElecComp;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.jme3x.jfx.injfx.input.JFXMouseInput;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ElecCase3D<T> {

	protected JmeApplication jmeApp;
	protected Canvas canvas;
	protected Region btns;

	protected Label nameLabel;
	protected AnchorPane pane;
	
	protected ElecCaseState<T> state;
	protected ElecCaseBtnController btnController;
	
	public ElecCase3D(ElecCaseState<T> state) {
//		创建一个Canvas层，用于显示JME
		canvas = new Canvas();
		canvas.setFocusTraversable(true);
		canvas.setOnMouseClicked(event -> canvas.requestFocus());
		canvas.getProperties().put(JFXMouseInput.PROP_USE_LOCAL_COORDS, true);
		canvas.parentProperty().addListener((ChangeListener<Parent>) (s, o, n) -> {
			if (o == null && n != null) {
				Pane parent = (Pane) n;
				log.info("给父容器添加尺寸监听");
				canvas.widthProperty().bind(parent.widthProperty());
				canvas.heightProperty().bind(parent.heightProperty());
			}
		});
//		创建一个JME应用程序，并且和Canvas集成
		jmeApp = new JmeApplication();

		state.setUI(this);
		jmeApp.getStateManager().attach(state);
		JmeToJFXIntegrator.startAndBind(jmeApp, canvas, Thread::new);
		
//		2、创建一些界面控件
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			btns = loader.load(ElecCase3D.class.getResourceAsStream("/view/jme/ElecCase.fxml"));
			btnController = loader.getController();
			btnController.setState(state);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		3、名称显示
		nameLabel = new Label();
//		TODO 调样式
		nameLabel.setId("RecognizeNameLabel");
		nameLabel.setStyle("-fx-background-color:rgb(200,0,0);");
		nameLabel.setTextFill(Color.WHITE);
		nameLabel.setEffect(new DropShadow(3, Color.BLACK));

		pane = new AnchorPane(nameLabel);
		pane.setPickOnBounds(false);
		pane.setMouseTransparent(true);

//		3、弹出菜单
		createWirePopupMenu();

		createCompPopupMenu();
	}
	
	public void selectedElecComp(ElecComp elecComp) {
//		找到典型案例的状态机
		jmeApp.enqueue(() -> {
			state.hold(elecComp);
			// 需要再focuse一下，否则按键无法监听
			Platform.runLater(() -> {
				canvas.requestFocus();
			});
		});
	}

	@FxThread
	public void showName(String text, float x, float y) {
		nameLabel.setText(text);
		nameLabel.setLayoutX(x);
//	解释：由于JME与Javafx坐标系上下颠倒，为了能正常获取鼠标坐标，将Javafx的坐标系向JME靠拢。
//	详见JFXMouseInput:
//	    private boolean useLocalCoords;
//		private boolean inverseYCoord;
		nameLabel.setLayoutY(canvas.getHeight() - y + 20);
	}

	public void setTitle(String title) {
		btnController.setTitle(title);
	}
	
	protected abstract void createCompPopupMenu();

	protected abstract void createWirePopupMenu();
	
	protected abstract void switchTo2D();
	
	protected abstract void switchTo3D();
}
