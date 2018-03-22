package com.cas.sim.tis.view.control.imp.jme;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.TypicalCaseState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.AlertUtil;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class TypicalCase3D implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(TypicalCase3D.class);
	private static final String REGEX_CHINESE = "[\u4e00-\u9fa5·！￥……（）【】｛｝：；“”‘’？。，]";// 中文正则
	// 当前案例对象
	private TypicalCase typicalCase;

	private JmeApplication jmeApp;

	private Canvas canvas;
	private Region btns;

	private TypicalCaseBtnController btnController;

	// KEY:UUID
	private Map<String, ContextMenu> menus = new HashMap<>();

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

		btnController.distroy();
	}

	public void setupCase(TypicalCase typicalCase) {
		this.typicalCase = typicalCase;
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

	public void save() {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		
		appState.save();

	}

	/**
	 * 显示元器件弹出菜单
	 * @param def
	 */
	public void showPopupMenu(ElecCompDef def) {
		ContextMenu menu = null;
		String key = def.getProxy().getUuid();
		if (menus.containsKey(key)) {
			menu = menus.get(key);
		} else {
			MenuItem tag = new MenuItem(MsgUtil.getMessage("button.tag"));
			tag.setOnAction(e -> {
				TextInputDialog steamIdDialog = new TextInputDialog(def.getProxy().getTagName());
				steamIdDialog.setTitle(MsgUtil.getMessage("button.tag"));
				steamIdDialog.setHeaderText(null);
				steamIdDialog.getEditor().textProperty().addListener((b, o, n) -> {
					Pattern pat = Pattern.compile(REGEX_CHINESE);
					Matcher mat = pat.matcher(n);
					if (mat.find()) {
						steamIdDialog.getEditor().setText(o);
					}
				});
				steamIdDialog.setContentText(MsgUtil.getMessage("typical.case.prompt.input.tag"));
				steamIdDialog.showAndWait().ifPresent(tagName -> {
					def.getProxy().setTagName(tagName);
					CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
					if (state == null) {
						return;
					}
					state.setTagNameChanged(true);
				});
			});
			MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
			del.setOnAction(e -> {
				CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
				if (state == null) {
					return;
				}
				boolean enable = state.detachFromCircuit(def);
				if (!enable) {
					AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.wiring"));
				}
			});
			menu = new ContextMenu(tag, del);
		}
		Point anchor = MouseInfo.getPointerInfo().getLocation();
		menu.show(GUIState.getStage(), anchor.x, anchor.y);
	}

	public TypicalCase getTypicalCase() {
		return typicalCase;
	}
}
