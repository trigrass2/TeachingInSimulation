package com.cas.sim.tis.app.state.typical;

import java.util.Optional;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ArchiveCaseAction;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.consts.ArchiveType;
import com.cas.sim.tis.consts.CaseMode;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.typical.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3x.jfx.util.JFXPlatform;

/**
 * 典型案例模块
 * @author Administrator
 */
public class TypicalCaseState extends ElecCaseState<ArchiveCase> {

	@Override
	public void setupCase0() {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 查看模式时，不可拿去元器件
		HoldStatePro.ins.setEnabled(CaseMode.VIEW_MODE != mode);
		HoldStatePro.ins.discard();

		// 创建新的circuitState
		circuitState = new CircuitState(this, ui, getRoot());
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(getElecCase().getArchivePath());
			circuitState.read(archive, mode);
			Optional.ofNullable(archive).ifPresent(a -> {
				if (CaseMode.TYPICAL_TRAIN_MODE == mode) {
					stateManager.attach(new TrainState((TypicalCase3D) ui, circuitState.getSteps(), circuitState.getRootCompNode()));
					JFXPlatform.runInFXThread(() -> ((TypicalCase3D) ui).loadSteps(circuitState.getSteps()));
				} else if (CaseMode.VIEW_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
					JFXPlatform.runInFXThread(() -> ((TypicalCase3D) ui).loadSteps(circuitState.getSteps()));
				} else if (CaseMode.EDIT_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
				}
			});
			// 结束加载界面
			JFXPlatform.runInFXThread(() -> SpringUtil.getBean(PageController.class).hideLoading());
		});
		stateManager.attach(circuitState);
	}

	@Override
	public void save() {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(getElecCase().getName());
		SpringUtil.getBean(ArchiveCaseAction.class).save(getElecCase(), archive, ArchiveType.TYPICAL);
	}

	@Override
	public void newCase() {
		ArchiveCase archiveCase = new ArchiveCase();
		archiveCase.setName("新建案例 *");
		archiveCase.setType(ArchiveType.TYPICAL.getIndex());
		setupCase(archiveCase, CaseMode.EDIT_MODE);
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
	public ArchiveCase getElecCase() {
		return elecCase;
	}
}
