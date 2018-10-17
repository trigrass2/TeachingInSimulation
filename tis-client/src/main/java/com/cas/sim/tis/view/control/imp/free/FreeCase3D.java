package com.cas.sim.tis.view.control.imp.free;

import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;

public class FreeCase3D extends ElecCase3D<ArchiveCase> {

	public FreeCase3D(ElecCaseState<ArchiveCase> state, ElecCaseBtnController btnController) {
		super(state, btnController);
	}

	@Override
	protected void switchTo2D() {

	}

	@Override
	protected void switchTo3D() {

	}
	
	public void save() {
		
	}
	
	public void setupCase(ArchiveCase archiveCase, CaseMode editMode) {
		
	}
	
	public boolean isClean() {
		return false;
	}

	public ArchiveCase getArchiveCase() {
		return null;
	}

}
