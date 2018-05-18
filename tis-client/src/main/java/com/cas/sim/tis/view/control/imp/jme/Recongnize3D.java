package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.anno.FxThread;
import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.ElecCompState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.view.control.IContent;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.jme3x.jfx.injfx.input.JFXMouseInput;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class Recongnize3D implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(Recongnize3D.class);
	private JmeApplication jmeApp;
	private Pane pane;
	private Region btns;
	private Canvas canvas;
	private Label nameLabel;

	public Recongnize3D() {
//		创建一个Canvas层，用于显示JME
		canvas = new Canvas();
//		=====================================================================
//		IMPORTANT
//		=====================================================================
		canvas.setFocusTraversable(true);
		canvas.setOnMouseClicked(event -> canvas.requestFocus());
		canvas.getProperties().put(JFXMouseInput.PROP_USE_LOCAL_COORDS, true);
		LOG.info("给父容器添加尺寸监听");
		canvas.parentProperty().addListener((ChangeListener<Parent>) (s, o, n) -> {
			if (o == null && n != null) {
				Pane parent = (Pane) n;
				canvas.widthProperty().bind(parent.widthProperty());
				canvas.heightProperty().bind(parent.heightProperty());
			}
		});

//		创建一个JME应用程序，并且和Canvas集成
		jmeApp = new JmeApplication();

		ElecCompState compState = new ElecCompState();
		compState.setUI(this);
		jmeApp.getStateManager().attach(compState);
		JmeToJFXIntegrator.startAndBind(jmeApp, canvas, Thread::new);

//		3、名称显示
		nameLabel = new Label();
//		TODO 调样式
		nameLabel.setId("RecognizeNameLabel");

		pane = new AnchorPane(nameLabel);
		pane.setPickOnBounds(false);
		pane.setMouseTransparent(true);

//		2、创建一些界面控件
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			btns = loader.load(Recongnize3D.class.getResourceAsStream("/view/jme/Recognize.fxml"));
			RecongnizeBtnController btnController = loader.getController();
			btnController.setState(compState);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Node[] getContent() {
		return new Node[] { canvas, pane, btns };
	}

	@Override
	public void distroy() {
		ElecCompState compState = jmeApp.getStateManager().getState(ElecCompState.class);
		jmeApp.getStateManager().detach(compState);
		jmeApp.stop(true);
	}

	public void setElecComp(ElecComp eComp) {
		nameLabel.setText("");
//		加载元器件模型
//		找到元器件认知的状态机
		ElecCompState appState = jmeApp.getStateManager().getState(ElecCompState.class);
//		保证线程安全
		jmeApp.enqueue(() -> {
//			修改元器件模型
			appState.setElecComp(eComp);
		});
	}

	@FxThread
	public void showName(String text, float x, float y) {
		nameLabel.setText(text);
		nameLabel.setLayoutX(x);
////	解释：由于JME与Javafx坐标系上下颠倒，为了能正常获取鼠标坐标，将Javafx的坐标系向JME靠拢。
////	详见JFXMouseInput:
////	    private boolean useLocalCoords;
////		private boolean inverseYCoord;
		nameLabel.setLayoutY(canvas.getHeight() - y + 20);
	}
}
