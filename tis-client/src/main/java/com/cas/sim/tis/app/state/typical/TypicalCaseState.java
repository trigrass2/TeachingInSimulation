package com.cas.sim.tis.app.state.typical;

import java.util.Optional;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;

import javafx.application.Platform;

/**
 * 典型案例模块
 * @author Administrator
 */
public class TypicalCaseState extends ElecCaseState<TypicalCase> {

	private TypicalCase typicalCase;

	public void setMode(CaseMode mode) {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 查看模式时，不可拿去元器件
		holdState.setEnabled(CaseMode.VIEW_MODE != mode);
		// 创建新的circuitState
		circuitState = new CircuitState(this, holdState, ui, root);
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());
			Optional.ofNullable(archive).ifPresent(a -> {
				circuitState.read(a, mode);
				if (CaseMode.TRAIN_MODE == mode) {
					TrainState trainState = new TrainState((TypicalCase3D) ui, circuitState.getSteps(), circuitState.getRootCompNode());
					stateManager.attach(trainState);
					Platform.runLater(() -> ((TypicalCase3D) ui).loadSteps(circuitState.getSteps()));
				} else if (CaseMode.VIEW_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
					Platform.runLater(() -> ((TypicalCase3D) ui).loadSteps(circuitState.getSteps()));
				} else if (CaseMode.EDIT_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
				}
			});
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		});
		stateManager.attach(circuitState);
		this.mode = mode;
	}

	public void save() {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(typicalCase.getName());
		SpringUtil.getBean(TypicalCaseAction.class).save(typicalCase, archive);
	}

	public void newCase() {
		TypicalCase typicalCase = new TypicalCase();
		typicalCase.setName("新建案例 *");
		setupCase(typicalCase, CaseMode.EDIT_MODE);
	}

//	打开一个案例（管理员、教师、学生）
	public void setupCase(TypicalCase typicalCase, CaseMode mode) {
		this.typicalCase = typicalCase;
		setMode(mode);
		Platform.runLater(() -> {
			ui.setTitle(typicalCase.getName());
			// 结束加载界面
			SpringUtil.getBean(PageController.class).hideLoading();
		});
	}

	public void autoComps(boolean layout) {
		if (circuitState == null) {
			return;
		}
		circuitState.autoComps(layout);
	}

	public void autoWires(boolean routing) {
		if (circuitState == null) {
			return;
		}
		circuitState.autoWires(routing);
	}

	@Override
	public TypicalCase getElecCase() {
		return typicalCase;
	}
}
