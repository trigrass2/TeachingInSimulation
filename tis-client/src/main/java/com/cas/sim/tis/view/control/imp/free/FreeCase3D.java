package com.cas.sim.tis.view.control.imp.free;

import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.free.FreeCaseState;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;

public class FreeCase3D extends ElecCase3D<ArchiveCase> implements IContent {

	public FreeCase3D(ElecCaseState<ArchiveCase> state, ElecCaseBtnController btnController) {
		super(state, btnController);
	}

	public void setupCase(ArchiveCase archiveCase, CaseMode mode) {
		state.setupCase(archiveCase, mode);
		btnController.clean();
		btnController.setMode(mode);
	}

	public ArchiveCase getArchiveCase() {
		return ((FreeCaseState) state).getElecCase();
	}
}
