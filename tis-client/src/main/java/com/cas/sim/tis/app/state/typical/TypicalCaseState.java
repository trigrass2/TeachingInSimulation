package com.cas.sim.tis.app.state.typical;

import java.util.Optional;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ArchiveCaseAction;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.consts.ArchiveType;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3x.jfx.util.JFXPlatform;

/**
 * 典型案例模块
 * @author Administrator
 */
public class TypicalCaseState extends ElecCaseState<ArchiveCase> {

	private ArchiveCase archiveCase;

	public void setMode(CaseMode mode) {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 查看模式时，不可拿去元器件
		holdState.setEnabled(CaseMode.VIEW_MODE != mode);
		holdState.discard();
		
		// 创建新的circuitState
		circuitState = new CircuitState(this, ui, root);
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(archiveCase.getArchivePath());
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
		this.mode = mode;
	}

	public void save() {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(archiveCase.getName());
		SpringUtil.getBean(ArchiveCaseAction.class).save(archiveCase, archive, ArchiveType.TYPICAL);
	}

	public void newCase() {
		ArchiveCase archiveCase = new ArchiveCase();
		archiveCase.setName("新建案例 *");
		archiveCase.setType(ArchiveType.TYPICAL.getIndex());
		setupCase(archiveCase, CaseMode.EDIT_MODE);
	}

//	打开一个案例（管理员、教师、学生）
	public void setupCase(ArchiveCase archiveCase, CaseMode mode) {
		this.archiveCase = archiveCase;
		setMode(mode);
		JFXPlatform.runInFXThread(() -> {
//			ui.setTitle(archiveCase.getName());
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
	public ArchiveCase getElecCase() {
		return archiveCase;
	}
}
