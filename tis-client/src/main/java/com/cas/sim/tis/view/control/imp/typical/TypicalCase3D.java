package com.cas.sim.tis.view.control.imp.typical;

import java.util.List;

import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.typical.TypicalCaseState;
import com.cas.sim.tis.consts.CaseMode;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;

public class TypicalCase3D extends ElecCase3D<ArchiveCase>{

	public TypicalCase3D(ElecCaseState<ArchiveCase> state, ElecCaseBtnController<ArchiveCase> btnController) {
		super(state, btnController);
	}

	public void setupCase(ArchiveCase typicalCase, CaseMode mode) {
//		修改元器件模型
		state.setupCase(typicalCase, mode);
		btnController.clean();
		btnController.setMode(mode);
		pageController.setTitleName(typicalCase.getName());
	}

	public ArchiveCase getArchiveCase() {
		return ((TypicalCaseState) state).getElecCase();
	}

	public void autoComps(boolean layout) {
		((TypicalCaseState) state).autoComps(layout);
	}

	public void autoWires(boolean routing) {
		((TypicalCaseState) state).autoWires(routing);
	}

	public void loadSteps(List<Step> steps) {
		((TypicalCaseBtnController) btnController).loadSteps(steps);
	}

	public void flowNext() {
		((TypicalCaseBtnController) btnController).next();
	}
}
