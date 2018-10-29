package com.cas.sim.tis.view.control.imp;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.Terminal;
import com.cas.circuit.component.Wire;
import com.cas.sim.tis.anno.FxThread;
import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.jme3x.jfx.injfx.input.JFXMouseInput;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ElecCase3D<T> implements IDistory {

	protected static final String REGEX_CHINESE = "[\u4e00-\u9fa5·！￥……（）【】｛｝：；“”‘’？。，]";// 中文正则

	protected JmeApplication jmeApp;
	protected Canvas canvas;
	protected Region btns;

	protected Label nameLabel;
	protected AnchorPane pane;

	protected ElecCaseState<T> state;
	protected ElecCaseBtnController btnController;

	protected ContextMenu wireMenu = new ContextMenu();
	protected ContextMenu compMenu = new ContextMenu();

	protected ElecCompDef compDef;
	protected Wire wire;
	protected Terminal terminal;

	protected MenuItem reset;
	protected Menu terms;

	public ElecCase3D(ElecCaseState<T> state, ElecCaseBtnController btnController) {
		this.state = state;
		this.btnController = btnController;

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
			loader.setController(btnController);
			btns = loader.load(ElecCase3D.class.getResourceAsStream("/view/jme/ElecCase.fxml"));
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

		btnController.setElecCase3D(this);
	}

	public Node[] getContent() {
		return new Node[] { canvas, pane, btns };
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

	protected void createCompPopupMenu() {
		createCompTagMenu();
		createCompDelMenu();
		createCompResetMenu();
	}

	protected void createWirePopupMenu() {
		createWireTagMenu();
		createWireDelMenu();
	}
	
	protected void createCompTagMenu() {
		TextInputDialog steamIdDialog = new TextInputDialog();
		steamIdDialog.setTitle(MsgUtil.getMessage("button.tag"));
		steamIdDialog.setHeaderText(null);
		steamIdDialog.setContentText(MsgUtil.getMessage("elec.case.prompt.input.comp.tag"));
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

		MenuItem tag = new MenuItem(MsgUtil.getMessage("button.tag"));
		tag.setOnAction(e -> {
			steamIdDialog.getEditor().setText(compDef.getProxy().getTagName());
			steamIdDialog.showAndWait().ifPresent(tagName -> {
				compDef.getProxy().setTagName(tagName);
			});
		});
		compMenu.getItems().add(tag);
	}
	
	protected void createCompDelMenu() {
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		del.setOnAction(e -> {
			CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
			if (state == null) {
				return;
			}
			boolean enable = state.detachFromCircuit(compDef);
			if (!enable) {
				AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.wiring"));
			}
		});
		compMenu.getItems().add(del);
	}
	
	protected void createCompResetMenu() {
		reset = new MenuItem(MsgUtil.getMessage("button.reset"));
		reset.setOnAction(e -> {
			compDef.reset();
		});
	}
	
	protected void createWireTagMenu() {
		TextInputDialog steamIdDialog = new TextInputDialog();
		steamIdDialog.setTitle(MsgUtil.getMessage("elec.case.wires.num"));
		steamIdDialog.setHeaderText(null);
		steamIdDialog.setContentText(MsgUtil.getMessage("elec.case.prompt.input.wire.num"));
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
		
		MenuItem tag = new MenuItem(MsgUtil.getMessage("elec.case.wires.num"));
		tag.setOnAction(e -> {
			steamIdDialog.getEditor().setText(wire.getProxy().getNumber());
			steamIdDialog.showAndWait().ifPresent(number -> {
				wire.getProxy().setNumber(number);
			});
		});
		wireMenu.getItems().add(tag);
	}
	
	protected void createWireDelMenu() {
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		del.setOnAction(e -> {
			CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
			boolean enable = state.detachFromCircuit(wire);
			if (!enable) {
				AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.power.on"));
			}
		});
		wireMenu.getItems().add(del);
	}
	
	/**
	 * 界面上点击保存按钮
	 */
	public void save() {
		state.save();
	}

	/**
	 * 判断当前接线板上是否存在元器件、导线
	 * @return
	 */
	public boolean isClean() {
		return state.isClean();
	}

	public void switchTo2D() {
		state.switchTo2D();
	}

	public void switchTo3D() {
		state.switchTo3D();
	}

	/**
	 * 显示元器件弹出菜单
	 * @param compDef 当前要操作的元器件对象
	 */
	public void showPopupMenu(ElecCompDef compDef) {
		this.compDef = compDef;

		String reset = compDef.getParam("reset", "0");
		Optional.ofNullable(reset).ifPresent(t -> {
			if ("1".equals(t)) {
				compMenu.getItems().add(0, this.reset);
			} else {
				compMenu.getItems().remove(this.reset);
			}
		});

		Point anchor = MouseInfo.getPointerInfo().getLocation();
		compMenu.show(GUIState.getStage(), anchor.x, anchor.y);
	}

	/**
	 * 显示导线弹出菜单
	 * @param compDef 当前要操作的导线对象
	 */
	public void showPopupMenu(Wire wire) {
		if (state.getMode().isHideCircuit()) {
			return;
		} 
		this.wire = wire;
		Point anchor = MouseInfo.getPointerInfo().getLocation();
		wireMenu.show(GUIState.getStage(), anchor.x, anchor.y);
	}


	@Override
	public void distroy() {
		jmeApp.getStateManager().detach(state);
		jmeApp.stop(true);

		btnController.distroy();
	}
}
