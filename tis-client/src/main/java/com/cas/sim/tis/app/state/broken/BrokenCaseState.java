package com.cas.sim.tis.app.state.broken;

import java.util.Optional;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.BrokenCaseAction;
import com.cas.sim.tis.action.BrokenRecordAction;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.consts.CaseMode;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.ExamBrokenRecord;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PageController;

import javafx.application.Platform;

public class BrokenCaseState extends ElecCaseState<BrokenCase> {

	@Override
	public void setupCase0() {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 检测模式时，不可拿取元器件
		HoldStatePro.ins.setEnabled(mode.isHoldEnable());
		HoldStatePro.ins.discard();

		// 创建新的circuitState
		circuitState = new CircuitState(this, ui, getRoot());
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(getElecCase().getArchivePath());
			Optional.ofNullable(archive).ifPresent(a -> {
				circuitState.read(a, mode);
			});
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		});
		stateManager.attach(circuitState);
	}

	public void submit(ExamBrokenRecord record) {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(getElecCase().getName());
		SpringUtil.getBean(BrokenRecordAction.class).save(record, archive);
	}

	@Override
	public void save() {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(getElecCase().getName());
		SpringUtil.getBean(BrokenCaseAction.class).save(getElecCase(), archive);
	}

	@Override
	public void newCase() {
		BrokenCase brokenCase = new BrokenCase();
		brokenCase.setName("新建案例 *");
		setupCase(brokenCase, CaseMode.EDIT_MODE);
	}

	@Override
	public BrokenCase getElecCase() {
		return elecCase;
	}
}
