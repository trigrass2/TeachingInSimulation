package com.cas.sim.tis.view.control.imp.broken;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.List;

import com.cas.circuit.IBroken;
import com.cas.circuit.IBroken.BrokenState;
import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.Wire;
import com.cas.circuit.vo.Pair;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;

import de.felixroske.jfxsupport.GUIState;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class BrokenCase3D extends ElecCase3D<BrokenCase> implements IContent {

	private Menu compSet;
	private Menu compUnset;

	private MenuItem wireSet;
	private MenuItem wireUnset;

	private ContextMenu compTrain;
	private ContextMenu wireTrain;

	private boolean finish;

	public BrokenCase3D(ElecCaseState<BrokenCase> state, ElecCaseBtnController btnController) {
		super(state, btnController);
	}

	public void setupCase(BrokenCase brokenCase, CaseMode mode) {
//		修改元器件模型
		state.setupCase(brokenCase, mode);
		btnController.clean();
		btnController.setMode(mode);
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	@Override
	protected void createCompPopupMenu() {
		super.createCompPopupMenu();
		// 编辑模式下的元器件菜单
		compSet = new Menu(MsgUtil.getMessage("broken.case.setup"));
		compMenu.getItems().add(0, compSet);
		// 练习模式下的元器件菜单
		compUnset = new Menu(MsgUtil.getMessage("broken.case.correct"));
		compTrain = new ContextMenu();
		compTrain.getItems().add(0, compUnset);
	}

	@Override
	protected void createWirePopupMenu() {
		// 编辑模式导线菜单
		super.createWirePopupMenu();
		// 练习模式导线菜单
		MenuItem unsetTrain = new MenuItem(MsgUtil.getMessage("broken.case.correct"));
		unsetTrain.setOnAction(e -> {
			if (wire.isBroken()) {
				wire.setCorrected(true);
				decreaseBrokenNume();
				state.getCircuitState().analyze();
				AlertUtil.showTip(TipType.INFO, MsgUtil.getMessage("broken.case.right.pair"));
			} else {
				decreaseChanceNume();
				AlertUtil.showTip(TipType.ERROR, MsgUtil.getMessage("broken.case.wrong.pair"));
			}
		});
		wireTrain = new ContextMenu();
		wireTrain.getItems().add(0, unsetTrain);
	}

	@Override
	protected void createCompDelMenu() {
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		del.setOnAction(e -> {
			// TODO 验证元器件上是否有
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

	@Override
	protected void createWireDelMenu() {
		MenuItem del = new MenuItem(MsgUtil.getMessage("button.delete"));
		del.setOnAction(e -> {
			if (wire.isBroken()) {
				AlertUtil.showTip(TipType.ERROR, MsgUtil.getMessage("alert.error.wire.brokend.cant.del"));
				return;
			}
			CircuitState state = jmeApp.getStateManager().getState(CircuitState.class);
			boolean enable = state.detachFromCircuit(wire);
			if (!enable) {
				AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.power.on"));
			}
		});
		wireMenu.getItems().add(del);
	}

	@Override
	public void showPopupMenu(ElecCompDef compDef) {
		if (finish) {
			return;
		}
		List<Pair> contactors = compDef.getContactorList();
		List<Pair> coils = compDef.getCoilList();
		if (CaseMode.EDIT_MODE == state.getMode()) {
			this.compSet.getItems().clear();
			// TODO 根据元器件配置添加菜单
			for (Pair contactor : contactors) {
				Menu menu = new Menu(contactor.getName());
				ToggleGroup group = new ToggleGroup();
				BrokenState state = contactor.getState();
				RadioMenuItem normal = new RadioMenuItem(MsgUtil.getMessage("broken.case.normal"));
				normal.setSelected(state == BrokenState.NORMAL);
				normal.setOnAction(e -> {
					// TODO 设置状态
					contactor.setBroken(BrokenState.NORMAL);
					compDef.setBroken(contactor);
					removeBrokenItem(contactor);
				});
				normal.setToggleGroup(group);
				RadioMenuItem disconnect = new RadioMenuItem(MsgUtil.getMessage("broken.case.open"));
				disconnect.setSelected(state == BrokenState.OPEN);
				disconnect.setOnAction(e -> {
					// TODO 设置状态
					contactor.setBroken(BrokenState.OPEN);
					compDef.setBroken(contactor);
					addBrokenItem(contactor);
				});
				disconnect.setToggleGroup(group);
				RadioMenuItem close = new RadioMenuItem(MsgUtil.getMessage("broken.case.close"));
				close.setSelected(state == BrokenState.CLOSE);
				close.setOnAction(e -> {
					// TODO 设置状态
					contactor.setBroken(BrokenState.CLOSE);
					compDef.setBroken(contactor);
					addBrokenItem(contactor);
				});
				close.setToggleGroup(group);
				menu.getItems().addAll(normal, disconnect, close);
				this.compSet.getItems().add(menu);
			}
			for (Pair coil : coils) {
				Menu menu = new Menu(coil.getName());
				ToggleGroup group = new ToggleGroup();
				RadioMenuItem normal = new RadioMenuItem(MsgUtil.getMessage("broken.case.normal"));
				normal.setSelected(true);
				normal.setOnAction(e -> {
					// TODO 设置状态
					coil.setBroken(BrokenState.NORMAL);
					compDef.setBroken(coil);
					removeBrokenItem(coil);
				});
				normal.setToggleGroup(group);
				RadioMenuItem disconnect = new RadioMenuItem(MsgUtil.getMessage("broken.case.open"));
				disconnect.setOnAction(e -> {
					// TODO 设置状态
					coil.setBroken(BrokenState.OPEN);
					compDef.setBroken(coil);
					addBrokenItem(coil);
				});
				disconnect.setToggleGroup(group);
				menu.getItems().addAll(normal, disconnect);
				this.compSet.getItems().add(menu);
			}
			super.showPopupMenu(compDef);
		} else {
			this.compUnset.getItems().clear();
			for (Pair contactor : contactors) {
				Menu menu = new Menu(contactor.getName());
				MenuItem disconnect = new MenuItem(MsgUtil.getMessage("broken.case.open"));
				disconnect.setOnAction(e -> {
					// TODO 判断线圈状态是否与用户选择结果相同，相同则纠正，不同则提示错误
					if (contactor.getState() == BrokenState.OPEN) {
						contactor.setBroken(BrokenState.NORMAL);
						compDef.setCorrected(contactor);
						decreaseBrokenNume();
						state.getCircuitState().analyze();
						AlertUtil.showTip(TipType.INFO, MsgUtil.getMessage("broken.case.right.pair"));
					} else {
						decreaseChanceNume();
						AlertUtil.showTip(TipType.ERROR, MsgUtil.getMessage("broken.case.wrong.pair"));
					}
				});
				MenuItem close = new MenuItem(MsgUtil.getMessage("broken.case.close"));
				close.setOnAction(e -> {
					// TODO 判断线圈状态是否与用户选择结果相同，相同则纠正，不同则提示错误
					if (contactor.getState() == BrokenState.CLOSE) {
						contactor.setBroken(BrokenState.NORMAL);
						compDef.setCorrected(contactor);
						decreaseBrokenNume();
						state.getCircuitState().analyze();
						AlertUtil.showTip(TipType.INFO, MsgUtil.getMessage("broken.case.right.pair"));
					} else {
						decreaseChanceNume();
						AlertUtil.showTip(TipType.ERROR, MsgUtil.getMessage("broken.case.wrong.pair"));
					}
				});
				menu.getItems().addAll(disconnect, close);
				this.compUnset.getItems().add(menu);
			}
			for (Pair coil : coils) {
				Menu menu = new Menu(coil.getName());
				MenuItem disconnect = new MenuItem(MsgUtil.getMessage("broken.case.open"));
				disconnect.setOnAction(e -> {
					// TODO 判断线圈状态是否与用户选择结果相同，相同则纠正，不同则提示错误
					if (coil.getState() == BrokenState.OPEN) {
						coil.setBroken(BrokenState.NORMAL);
						compDef.setCorrected(coil);
						decreaseBrokenNume();
						state.getCircuitState().analyze();
						AlertUtil.showTip(TipType.INFO, MsgUtil.getMessage("broken.case.right.pair"));
					} else {
						decreaseChanceNume();
						AlertUtil.showTip(TipType.ERROR, MsgUtil.getMessage("broken.case.wrong.pair"));
					}
				});
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
		if (finish) {
			return;
		}
		if (CaseMode.EDIT_MODE == state.getMode()) {
			if (wire.isBroken()) {
				wireUnset = new MenuItem(MsgUtil.getMessage("broken.case.reset"));
				wireUnset.setOnAction(e -> {
					wire.setBroken(BrokenState.NORMAL);
					removeBrokenItem(wire);
					state.getCircuitState().analyze();
				});
				wireMenu.getItems().remove(0);
				wireMenu.getItems().add(0, wireUnset);
			} else {
				wireSet = new MenuItem(MsgUtil.getMessage("broken.case.setup"));
				wireSet.setOnAction(e -> {
					wire.setBroken(BrokenState.OPEN);
					addBrokenItem(wire);
					state.getCircuitState().analyze();
				});
				wireMenu.getItems().remove(0);
				wireMenu.getItems().add(0, wireSet);
			}
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

	public void addBrokenItem(IBroken broken) {
		((BrokenCaseBtnController) btnController).addBrokenItem(broken);
	}

	public void removeBrokenItem(IBroken broken) {
		((BrokenCaseBtnController) btnController).removeBrokenItem(broken);
	}

	public void initBrokenNum(int num) {
		((BrokenCaseBtnController) btnController).init(num);
	}

	public void decreaseBrokenNume() {
		((BrokenCaseBtnController) btnController).decreaseBrokenNume();
	}

	public void decreaseChanceNume() {
		((BrokenCaseBtnController) btnController).decreaseChanceNume();
	}

	public void submit() {
		((BrokenCaseBtnController) btnController).submit(true);
	}
}
