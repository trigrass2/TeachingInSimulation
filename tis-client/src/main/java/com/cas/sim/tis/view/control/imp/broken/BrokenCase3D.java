package com.cas.sim.tis.view.control.imp.broken;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.List;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.Wire;
import com.cas.circuit.vo.Pair;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class BrokenCase3D extends ElecCase3D<BrokenCase> implements IContent {

	private Menu compSet;
	private Menu compUnset;

	private ContextMenu compTrain;
	private ContextMenu wireTrain;

	public BrokenCase3D(ElecCaseState<BrokenCase> state, ElecCaseBtnController btnController) {
		super(state, btnController);
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
		appState.save();
	}

	@Override
	protected void createCompPopupMenu() {
		super.createCompPopupMenu();
		// 编辑模式下的元器件菜单
		compSet = new Menu("设置故障");
		menuComp.getItems().add(0, compSet);
		// 练习模式下的元器件菜单
		compTrain = new ContextMenu();
		compUnset = new Menu("排除故障");
		compTrain.getItems().add(0, compUnset);
	}

	@Override
	protected void createWirePopupMenu() {
		// 编辑模式导线菜单
		super.createWirePopupMenu();
		MenuItem set = new MenuItem("设置故障");
		set.setOnAction(e -> {
			state.getCircuitState().setWireBroken(wire, true);
		});
		MenuItem unset = new MenuItem("排除故障");
		unset.setOnAction(e -> {
			state.getCircuitState().setWireBroken(wire, false);
		});
		menuWire.getItems().add(0, set);
		menuWire.getItems().add(1, unset);
		// 练习模式导线菜单
		wireTrain = new ContextMenu();
		MenuItem unsetTrain = new MenuItem("排除故障");
		unset.setOnAction(e -> {
			state.getCircuitState().setWireBroken(wire, false);
		});
		wireTrain.getItems().add(0, unsetTrain);
	}

	@Override
	public void showPopupMenu(ElecCompDef compDef) {
		List<Pair> contactors = compDef.getContactorList();
		List<Pair> coils = compDef.getCoilList();
		if (CaseMode.EDIT_MODE == state.getMode()) {
			this.compSet.getItems().clear();
			// TODO 根据元器件配置添加菜单
			for (Pair contactor : contactors) {
				Menu menu = new Menu(contactor.getDesc());
				ToggleGroup group = new ToggleGroup();
				RadioMenuItem normal = new RadioMenuItem("状态正常");
				normal.setSelected(true);
				normal.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(contactor);
				});
				normal.setToggleGroup(group);
				RadioMenuItem disconnect = new RadioMenuItem("始终断开");
				disconnect.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(contactor);
				});
				disconnect.setToggleGroup(group);
				RadioMenuItem close = new RadioMenuItem("始终闭合");
				close.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(contactor);
				});
				close.setToggleGroup(group);
				menu.getItems().addAll(normal, disconnect, close);
				this.compSet.getItems().add(menu);
			}
			for (Pair coil : coils) {
				Menu menu = new Menu(coil.getDesc());
				ToggleGroup group = new ToggleGroup();
				RadioMenuItem normal = new RadioMenuItem("状态正常");
				normal.setSelected(true);
				normal.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(coil);
				});
				normal.setToggleGroup(group);
				RadioMenuItem disconnect = new RadioMenuItem("始终断开");
				disconnect.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(coil);
				});
				disconnect.setToggleGroup(group);
				menu.getItems().addAll(normal, disconnect);
				this.compSet.getItems().add(menu);
			}
			super.showPopupMenu(compDef);
		} else {
			this.compUnset.getItems().clear();
			for (Pair contactor : contactors) {
				Menu menu = new Menu(contactor.getDesc());
				ToggleGroup group = new ToggleGroup();
				RadioMenuItem disconnect = new RadioMenuItem("始终断开");
				disconnect.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(contactor);
				});
				disconnect.setToggleGroup(group);
				RadioMenuItem close = new RadioMenuItem("始终闭合");
				close.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(contactor);
				});
				close.setToggleGroup(group);
				menu.getItems().addAll(disconnect, close);
				this.compUnset.getItems().add(menu);
			}
			for (Pair coil : coils) {
				Menu menu = new Menu(coil.getDesc());
				ToggleGroup group = new ToggleGroup();
				RadioMenuItem disconnect = new RadioMenuItem("始终断开");
				disconnect.setOnAction(e -> {
					// TODO 设置状态
					compDef.setBroken(coil);
				});
				disconnect.setToggleGroup(group);
				menu.getItems().addAll(disconnect);
				this.compUnset.getItems().add(menu);
			}
			this.compDef = compDef;
			Point anchor = MouseInfo.getPointerInfo().getLocation();
			compTrain.show(GUIState.getStage(), anchor.x, anchor.y);
		}
	}

	@Override
	public void showPopupMenu(Wire wire) {
		if (CaseMode.EDIT_MODE == state.getMode()) {
			super.showPopupMenu(wire);
		} else {
			this.wire = wire;
			Point anchor = MouseInfo.getPointerInfo().getLocation();
			wireTrain.show(GUIState.getStage(), anchor.x, anchor.y);
		}
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

	public void addBrokenItem(BrokenItem item) {
		((BrokenCaseBtnController) btnController).addBrokenItem(item);
	}

	public void removeBrokenItem(BrokenItem item) {
		((BrokenCaseBtnController) btnController).removeBrokenItem(item);
	}
}
