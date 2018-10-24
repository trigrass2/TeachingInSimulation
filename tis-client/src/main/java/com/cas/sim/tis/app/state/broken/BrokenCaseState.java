package com.cas.sim.tis.app.state.broken;

import java.util.Optional;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.BrokenCaseAction;
import com.cas.sim.tis.action.BrokenRecordAction;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.BrokenRecord;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PageController;

import javafx.application.Platform;

public class BrokenCaseState extends ElecCaseState<BrokenCase> {

	private BrokenCase brokenCase;

	@Override
	public void setMode(CaseMode mode) {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 检测模式时，不可拿去元器件
		holdState.setEnabled(mode.isHoldEnable());
		// 创建新的circuitState
		circuitState = new CircuitState(this, holdState, ui, root);
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(brokenCase.getArchivePath());
			Optional.ofNullable(archive).ifPresent(a -> {
				circuitState.read(a, mode);
			});
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		});
		stateManager.attach(circuitState);
		this.mode = mode;
	}

	public void submit(BrokenRecord record) {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(brokenCase.getName());
		SpringUtil.getBean(BrokenRecordAction.class).save(record, archive);
	}

	@Override
	public void save() {
		if (circuitState == null) {
			return;
		}
		Archive archive = circuitState.getArchive();
		archive.setName(brokenCase.getName());
		SpringUtil.getBean(BrokenCaseAction.class).save(brokenCase, archive);
	}

	@Override
	public void newCase() {
		BrokenCase brokenCase = new BrokenCase();
		brokenCase.setName("新建案例 *");
		setupCase(brokenCase, CaseMode.EDIT_MODE);
	}

	@Override
	public void setupCase(BrokenCase brokenCase, CaseMode mode) {
		this.brokenCase = brokenCase;
		setMode(mode);
		Platform.runLater(() -> {
			ui.setTitle(brokenCase.getName());
			// 结束加载界面
			SpringUtil.getBean(PageController.class).hideLoading();
		});
	}

	@Override
	public BrokenCase getElecCase() {
		return brokenCase;
	}
}
