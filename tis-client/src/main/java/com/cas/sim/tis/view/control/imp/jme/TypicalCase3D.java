package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.TypicalCaseState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.view.control.IContent;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.jme3x.jfx.injfx.input.JFXMouseInput;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class TypicalCase3D implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(TypicalCase3D.class);
	private JmeApplication jmeApp;
	private Region btns;
	private Canvas canvas;

	public TypicalCase3D() {
//		创建一个Canvas层，用于显示JME
		canvas = new Canvas();
//		canvas.setStyle("-fx-background-size: cover");
		canvas.getProperties().put(JFXMouseInput.PROP_USE_LOCAL_COORDS, true);
		canvas.parentProperty().addListener((ChangeListener<Parent>) (s, o, n) -> {
			if (o == null && n != null) {
				Pane parent = (Pane) n;
				LOG.info("给父容器添加尺寸监听");
				canvas.widthProperty().bind(parent.widthProperty());
				canvas.heightProperty().bind(parent.heightProperty());
			}
		});
//		创建一个JME应用程序，并且和Canvas集成
		jmeApp = new JmeApplication();

		TypicalCaseState compState = new TypicalCaseState();
		jmeApp.getStateManager().attach(compState);
		JmeToJFXIntegrator.startAndBindMainViewPort(jmeApp, canvas, Thread::new);

//		2、创建一些界面控件
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			btns = loader.load(TypicalCase3D.class.getResourceAsStream("/view/jme/TypicalCase.fxml"));
			TypicalCaseBtnController btnController = loader.getController();
			btnController.setState(compState);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Node[] getContent() {
		return new Node[] { canvas, btns };
	}

	@Override
	public void distroy() {
		TypicalCaseState compState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		jmeApp.getStateManager().detach(compState);
		jmeApp.stop(true);
	}

	public void setupCase(TypicalCase typicalCase) {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		修改元器件模型
		appState.setupCase(typicalCase);
	}

	public void selectedElecComp(ElecComp elecComp) {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		
		jmeApp.enqueue(() -> appState.hold(elecComp));
	}

	public void save() {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		
		appState.save();

	}
}
