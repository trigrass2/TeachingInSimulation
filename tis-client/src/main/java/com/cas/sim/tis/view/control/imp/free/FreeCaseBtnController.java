package com.cas.sim.tis.view.control.imp.free;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;
import com.cas.sim.tis.view.controller.PageController;

public class FreeCaseBtnController extends ElecCaseBtnController {

	private FreeFlowItem flow;

	public FreeCaseBtnController(CaseMode... enableModes) {
		super(enableModes);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		scroll.setContent(flow = new FreeFlowItem());
	}
	
	@Override
	protected void switchCaseMode(CaseMode mode) {
//		FreeCase3D freeCase3D = (FreeCase3D) elecCase3D;
		SpringUtil.getBean(PageController.class).showLoading();
		if (CaseMode.BROKEN_TRAIN_MODE == mode) {
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
//		FreeCase freeCase = ((FreeCaseState) elecCaseState).getElecCase();
//		if (freeCase == null) {
		return null;
//		} else {
//			return freeCase.getDrawings();
//		}
	}

	@Override
	public void setDrawings(String drawings) {
//		FreeCase freeCase = ((FreeCaseState) elecCaseState).getElecCase();
//		if (freeCase != null) {
//			freeCase.setDrawings(drawings);
//		}
	}

	public void addFreeItem(FreeItem item) {
		flow.getChildren().add(item);
	}

	public void removeFreeItem(FreeItem item) {
		flow.getChildren().remove(item);
	}

	@Override
	public void prev() {
		
	}

	@Override
	public void next() {
		
	}
}