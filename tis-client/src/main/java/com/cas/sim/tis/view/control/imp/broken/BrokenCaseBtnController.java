package com.cas.sim.tis.view.control.imp.broken;

import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;
import com.cas.sim.tis.view.controller.PageController;

public class BrokenCaseBtnController extends ElecCaseBtnController {

	public BrokenCaseBtnController(CaseMode... enableModes) {
		super(enableModes);
	}

	@Override
	protected void switchCaseMode(CaseMode mode) {
//		BrokenCase3D brokenCase3D = (BrokenCase3D) elecCase3D;
		SpringUtil.getBean(PageController.class).showLoading();
		if (CaseMode.TRAIN_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(false);
			view.setVisible(false);
			steps.setVisible(false);
			btns.setVisible(false);
		} else if (CaseMode.EDIT_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(true);
			view.setVisible(false);
			steps.setVisible(false);
			btns.setVisible(false);
		}
		if (elecCaseState != null) {
			elecCaseState.setMode(mode);
		}
	}

	@Override
	public String getDrawings() {
		BrokenCase brokenCase = ((BrokenCaseState) elecCaseState).getElecCase();
		if (brokenCase == null) {
			return null;
		} else {
			return brokenCase.getDrawings();
		}
	}

	@Override
	public void setDrawings(String drawings) {
		BrokenCase brokenCase = ((BrokenCaseState) elecCaseState).getElecCase();
		if (brokenCase != null) {
			brokenCase.setDrawings(drawings);
		}
	}
}
