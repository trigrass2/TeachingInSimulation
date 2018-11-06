package com.cas.sim.tis.app.state.free;

import java.util.Optional;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ArchiveCaseAction;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.typical.TrainState;
import com.cas.sim.tis.consts.ArchiveType;
import com.cas.sim.tis.consts.CaseMode;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PageController;

import javafx.application.Platform;

public class FreeCaseState extends ElecCaseState<ArchiveCase> {

	@Override
	public void setupCase0() {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 查看模式时，不可拿去元器件
		HoldStatePro.ins.setEnabled(CaseMode.VIEW_MODE != mode);
		// 创建新的circuitState
		circuitState = new CircuitState(this, ui, getRoot());
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(getElecCase().getArchivePath());
			circuitState.read(archive, mode);
			Optional.ofNullable(archive).ifPresent(a -> {
				if (CaseMode.EDIT_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
				}
			});
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
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
		SpringUtil.getBean(ArchiveCaseAction.class).save(getElecCase(), archive, ArchiveType.FREE);
	}

	@Override
	public void newCase() {
		ArchiveCase archiveCase = new ArchiveCase();
		archiveCase.setName("新建案例 *");
		archiveCase.setType(ArchiveType.FREE.getIndex());
		setupCase(archiveCase, CaseMode.EDIT_MODE);
	}

	@Override
	public ArchiveCase getElecCase() {
		return elecCase;
	}

}
