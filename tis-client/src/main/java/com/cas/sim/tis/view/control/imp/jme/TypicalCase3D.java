package com.cas.sim.tis.view.control.imp.jme;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Wire;
import com.cas.sim.tis.anno.FxThread;
import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.TypicalCaseState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IContent;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.jme3x.jfx.injfx.input.JFXMouseInput;

import de.felixroske.jfxsupport.GUIState;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class TypicalCase3D implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(TypicalCase3D.class);
	private static final String REGEX_CHINESE = "[\u4e00-\u9fa5·！￥……（）【】｛｝：；“”‘’？。，]";// 中文正则

	private JmeApplication jmeApp;

	private Canvas canvas;
	private Region btns;

	private ContextMenu menuWire;
	private ContextMenu menuComp;

	private TypicalCaseBtnController btnController;
	private Wire wire;
	private ElecCompDef compDef;
	private Label nameLabel;
	private AnchorPane pane;

	public TypicalCase3D() {
//		创建一个Canvas层，用于显示JME
		canvas = new Canvas();
		canvas.setFocusTraversable(true);
		canvas.setOnMouseClicked(event -> canvas.requestFocus());
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
		compState.setUI(this);
		jmeApp.getStateManager().attach(compState);
		JmeToJFXIntegrator.startAndBind(jmeApp, canvas, Thread::new);

//		2、创建一些界面控件
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			btns = loader.load(TypicalCase3D.class.getResourceAsStream("/view/jme/TypicalCase.fxml"));
			btnController = loader.getController();
			btnController.setState(compState);
			btnController.setUI(this);
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

	private void createCompPopupMenu() {
		MenuItem tag = new MenuItem(MsgUtil.getMessage("button.tag"));
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		this.menuComp = new ContextMenu(tag, del);

		TextInputDialog steamIdDialog = new TextInputDialog();
		steamIdDialog.setTitle(MsgUtil.getMessage("button.tag"));
		steamIdDialog.setHeaderText(null);
		steamIdDialog.setContentText(MsgUtil.getMessage("typical.case.prompt.input.comp.tag"));
		steamIdDialog.getEditor().textProperty().addListener((b, o, n) -> {
			if (n == null) {
				return;
			}
			Pattern pat = Pattern.compile(REGEX_CHINESE);
			Matcher mat = pat.matcher(n);
			if (mat.find()) {
				steamIdDialog.getEditor().setText(o);
			}
		});

		tag.setOnAction(e -> {
			steamIdDialog.getEditor().setText(compDef.getProxy().getTagName());
			steamIdDialog.showAndWait().ifPresent(tagName -> {
				compDef.getProxy().setTagName(tagName);
			});
		});

		del.setOnAction(e -> {
			CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
			if (state == null) {
				return;
			}
			state.detachFromCircuit(compDef);
//			if (!enable) {
//				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.wiring"));
//			}
		});
	}

	private void createWirePopupMenu() {
		MenuItem tag = new MenuItem(MsgUtil.getMessage("typical.case.wire.num"));
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		menuWire = new ContextMenu(tag, del);

		TextInputDialog steamIdDialog = new TextInputDialog();
		steamIdDialog.setTitle(MsgUtil.getMessage("typical.case.wire.num"));
		steamIdDialog.setHeaderText(null);
		steamIdDialog.setContentText(MsgUtil.getMessage("typical.case.prompt.input.wire.num"));
		steamIdDialog.getEditor().textProperty().addListener((b, o, n) -> {
			Pattern pat = Pattern.compile(REGEX_CHINESE);
			Matcher mat = pat.matcher(n);
			if (mat.find()) {
				steamIdDialog.getEditor().setText(o);
			}
		});

		tag.setOnAction(e -> {
			steamIdDialog.getEditor().setText(wire.getProxy().getNumber());
			steamIdDialog.showAndWait().ifPresent(number -> {
				wire.getProxy().setNumber(number);
			});
		});
		del.setOnAction(e -> {
			CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
			state.detachFromCircuit(wire);
//			if (!enable) {
//				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.power.on"));
//			}
		});
	}

	@Override
	public Node[] getContent() {
		return new Node[] { canvas, pane, btns };
	}

	@Override
	public void distroy() {
		TypicalCaseState compState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		jmeApp.getStateManager().detach(compState);
		jmeApp.stop(true);

		btnController.distroy();
	}
	
	/**
	 * 判断当前接线板上是否存在元器件、导线
	 * @return
	 */
	public boolean isClean() {
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		return appState.isClean();
	}

	public void setupCase(TypicalCase typicalCase) {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		修改元器件模型
		appState.setupCase(typicalCase);

		btnController.clean();
	}

	public void selectedElecComp(ElecComp elecComp) {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		
		jmeApp.enqueue(() -> appState.hold(elecComp));
	}

	/**
	 * 界面上点击保存按钮
	 */
	public void save() {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		
		appState.save();
	}

	/**
	 * 显示元器件弹出菜单
	 * @param compDef 当前要操作的元器件对象
	 */
	public void showPopupMenu(ElecCompDef compDef) {
		this.compDef = compDef;

		Point anchor = MouseInfo.getPointerInfo().getLocation();
		menuComp.show(GUIState.getStage(), anchor.x, anchor.y);
	}

	/**
	 * 显示导线弹出菜单
	 * @param compDef 当前要操作的导线对象
	 */
	public void showPopupMenu(Wire wire) {
		this.wire = wire;
		Point anchor = MouseInfo.getPointerInfo().getLocation();
		menuWire.show(GUIState.getStage(), anchor.x, anchor.y);
	}

	public TypicalCase getTypicalCase() {
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		if (appState != null) {
			return appState.getTypicalCase();
		}
		return null;
	}

	public void switchTo2D() {
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		appState.switchTo2D();
	}

	public void switchTo3D() {
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		appState.switchTo3D();
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
