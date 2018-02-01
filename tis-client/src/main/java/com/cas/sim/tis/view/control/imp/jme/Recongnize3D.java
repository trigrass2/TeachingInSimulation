package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.ElecCompState;
import com.cas.sim.tis.view.control.IContent;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class Recongnize3D implements IContent {

	private StackPane stackPane;
	private JmeApplication jmeApp;

	public Recongnize3D() {
//		创建一个堆栈容器
		stackPane = new StackPane();
//		创建一个Canva层，用于显示JME
		Canvas canvas = new Canvas(1113, 718);
		stackPane.getChildren().add(canvas);
//		创建一个JME应用程序，并且和Canvas集成
		jmeApp = new JmeApplication();

		ElecCompState compState = new ElecCompState();
		jmeApp.getStateManager().attach(compState);
		JmeToJFXIntegrator.startAndBindMainViewPort(jmeApp, canvas, Thread::new);

//		2、创建一些界面控件
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			Region btns = loader.load(Recongnize3D.class.getResourceAsStream("/view/jme/Recognize.fxml"));
			RecongnizeBtnController btnController = loader.getController();
			btnController.setState(compState);
			stackPane.getChildren().add(btns);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Region getContent() {
		return stackPane;
	}

	@Override
	public void removed() {
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
