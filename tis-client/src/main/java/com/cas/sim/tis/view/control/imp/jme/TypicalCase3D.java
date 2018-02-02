package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.ElecCompState;
import com.cas.sim.tis.view.control.IContent;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

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

		ElecCompState compState = new ElecCompState();
		jmeApp.getStateManager().attach(compState);
		JmeToJFXIntegrator.startAndBindMainViewPort(jmeApp, canvas, Thread::new);

//		2、创建一些界面控件
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			btns = loader.load(TypicalCase3D.class.getResourceAsStream("/view/jme/Recognize.fxml"));
			RecongnizeBtnController btnController = loader.getController();
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
		ElecCompState compState = jmeApp.getStateManager().getState(ElecCompState.class);
		jmeApp.getStateManager().detach(compState);
		jmeApp.stop(true);
	}

	public void setModelPath(String mdlPath) {
//		加载元器件模型
//		找到元器件认知的状态机
		ElecCompState appState = jmeApp.getStateManager().getState(ElecCompState.class);
//		保证线程安全
		jmeApp.enqueue(() -> {
//			修改元器件模型
			appState.setModelPath(mdlPath);
		});
	}
}
