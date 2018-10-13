package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;

import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.typical.TypicalCaseState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;

import javafx.application.Platform;
import javafx.scene.Node;

public class TypicalCase3D extends ElecCase3D<TypicalCase> implements IContent {

	public TypicalCase3D(ElecCaseState<TypicalCase> state, ElecCaseBtnController btnController) {
		super(state, btnController);
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

	public void setupCase(TypicalCase typicalCase, CaseMode mode) {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
//		修改元器件模型
		appState.setupCase(typicalCase, mode);
		btnController.clean();
		btnController.setMode(mode);
	}

	public void selectedElecComp(ElecComp elecComp) {
//		找到典型案例的状态机
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		
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
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		appState.save();
	}

	public TypicalCase getTypicalCase() {
		return ((TypicalCaseState) state).getElecCase();
	}

	public void switchTo2D() {
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		appState.switchTo2D();
	}

	public void switchTo3D() {
		TypicalCaseState appState = jmeApp.getStateManager().getState(TypicalCaseState.class);
		appState.switchTo3D();
	}

	public void autoComps(boolean layout) {
		((TypicalCaseState) state).autoComps(layout);
	}

	public void autoWires(boolean routing) {
		((TypicalCaseState) state).autoWires(routing);
	}

	public void loadSteps(List<Step> steps) {
		btnController.loadSteps(steps);
	}

	public void flowNext() {
		btnController.next();
	}
}
