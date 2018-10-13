package com.cas.sim.tis.view.control.imp.broken;

import java.util.List;

import com.cas.circuit.component.ElecCompDef;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class BrokenCase3D extends ElecCase3D<BrokenCase> implements IContent {

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
	protected void createWirePopupMenu() {
		super.createWirePopupMenu();
		MenuItem set = new MenuItem("设置故障");
		set.setOnAction(e -> {

		});
		MenuItem unset = new MenuItem("排除故障");
		unset.setOnAction(e -> {

		});
		menuWire.getItems().add(0, set);
		menuWire.getItems().add(1, unset);
	}
	
	@Override
	public void showPopupMenu(ElecCompDef compDef) {
		
		super.showPopupMenu(compDef);
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
}
