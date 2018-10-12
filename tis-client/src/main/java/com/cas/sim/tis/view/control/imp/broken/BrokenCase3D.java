package com.cas.sim.tis.view.control.imp.broken;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.Wire;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class BrokenCase3D extends ElecCase3D<BrokenCase> implements IContent {

	private static final String REGEX_CHINESE = "[\u4e00-\u9fa5·！￥……（）【】｛｝：；“”‘’？。，]";// 中文正则

	private ContextMenu menuWire;
	private ContextMenu menuComp;

	private Wire wire;
	private MenuItem reset;
	private ElecCompDef compDef;

	public BrokenCase3D() {
		super(new BrokenCaseState());
	}

	@Override
	public Node[] getContent() {
		return new Node[] { canvas, pane, btns };
	}

	@Override
	public void distroy() {
		BrokenCaseState compState = jmeApp.getStateManager().getState(BrokenCaseState.class);
		jmeApp.getStateManager().detach(compState);
		jmeApp.stop(true);

		btnController.distroy();
	}

	/**
	 * 判断当前接线板上是否存在元器件、导线
	 * @return
	 */
	public boolean isClean() {
		BrokenCaseState appState = jmeApp.getStateManager().getState(BrokenCaseState.class);
		return appState.isClean();
	}

	public void setupCase(BrokenCase brokenCase, CaseMode mode) {
//		找到典型案例的状态机
		BrokenCaseState appState = jmeApp.getStateManager().getState(BrokenCaseState.class);
//		修改元器件模型
		appState.setupCase(brokenCase, mode);
		btnController.clean();
		btnController.setMode(mode);
	}

	public void selectedElecComp(ElecComp elecComp) {
//		找到典型案例的状态机
		BrokenCaseState appState = jmeApp.getStateManager().getState(BrokenCaseState.class);
//		
		jmeApp.enqueue(() -> {
			appState.hold(elecComp);
			// 需要再focuse一下，否则按键无法监听
			Platform.runLater(() -> {
				canvas.requestFocus();
			});
		});
	}

	/**
	 * 界面上点击保存按钮
	 */
	public void save() {
//		找到典型案例的状态机
		BrokenCaseState appState = jmeApp.getStateManager().getState(BrokenCaseState.class);
//		
		appState.save();
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
				menuComp.getItems().add(0, this.reset);
			} else {
				menuComp.getItems().remove(this.reset);
			}
		});

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
	
	public BrokenCase getBrokenCase() {
		return ((BrokenCaseState) state).getElecCase();
	}

	public void switchTo2D() {
		BrokenCaseState appState = jmeApp.getStateManager().getState(BrokenCaseState.class);
		appState.switchTo2D();
	}

	public void switchTo3D() {
		BrokenCaseState appState = jmeApp.getStateManager().getState(BrokenCaseState.class);
		appState.switchTo3D();
	}

	public void setTitle(String title) {
		btnController.setTitle(title);
	}

	public void loadSteps(List<Step> steps) {
		btnController.loadSteps(steps);
	}

	public void flowNext() {
		btnController.next();
	}

	@Override
	protected void createCompPopupMenu() {
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

		reset = new MenuItem(MsgUtil.getMessage("button.reset"));
		reset.setOnAction(e -> {
			compDef.reset();
		});
	}

	@Override
	protected void createWirePopupMenu() {
		MenuItem tag = new MenuItem(MsgUtil.getMessage("typical.case.wire.num"));
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		menuWire = new ContextMenu(tag, del);

		TextInputDialog steamIdDialog = new TextInputDialog();
		steamIdDialog.setTitle(MsgUtil.getMessage("typical.case.wire.num"));
		steamIdDialog.setHeaderText(null);
		steamIdDialog.setContentText(MsgUtil.getMessage("typical.case.prompt.input.wire.num"));
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
}
