package com.cas.sim.tis.view.control.imp.jme;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.typical.TypicalCaseState;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;
import com.cas.sim.tis.view.controller.PageController;

public class TypicalCaseBtnController extends ElecCaseBtnController {

	public TypicalCaseBtnController(CaseMode ...enableModes) {
		super(enableModes);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		autoComps.selectedProperty().addListener((b, o, n) -> {
			if (n) {
				autoComps.setText(MsgUtil.getMessage("typical.case.clear.comp"));
			} else {
				autoComps.setText(MsgUtil.getMessage("typical.case.auto.comp"));
				autoWires.setSelected(false);
			}
			((TypicalCase3D) elecCase3D).autoComps(n);
		});
		autoWires.selectedProperty().addListener((b, o, n) -> {
			if (n) {
				autoWires.setText(MsgUtil.getMessage("typical.case.clear.wire"));
				autoComps.setSelected(true);
			} else {
				autoWires.setText(MsgUtil.getMessage("typical.case.auto.wire"));
			}
			((TypicalCase3D) elecCase3D).autoWires(n);
		});
	}

	protected void switchCaseMode(CaseMode mode) {
		TypicalCase3D typicalCase3D = (TypicalCase3D) elecCase3D;
		SpringUtil.getBean(PageController.class).showLoading();
		if (CaseMode.VIEW_MODE == mode) {
			view.toFront();
			view.setVisible(true);
			trainOrEdit.setVisible(false);
			steps.setVisible(true);
			btns.setVisible(true);
			autoComps.setSelected(false);
			autoWires.setSelected(false);
			if (typicalCase3D != null) {
				typicalCase3D.autoComps(false);
				typicalCase3D.autoWires(false);
			}
		} else if (CaseMode.TRAIN_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(true);
			view.setVisible(false);
			steps.setVisible(true);
			btns.setVisible(false);
			if (typicalCase3D != null) {
				typicalCase3D.autoWires(false);
				typicalCase3D.autoComps(false);
			}
		} else if (CaseMode.EDIT_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(true);
			view.setVisible(false);
			steps.setVisible(false);
			btns.setVisible(false);
			if (typicalCase3D != null) {
				typicalCase3D.autoComps(true);
				typicalCase3D.autoWires(true);
			}
		}
		if (elecCaseState != null) {
			elecCaseState.setMode(mode);
		}
	}

	@Override
	public String getDrawings() {
		TypicalCase typicalCase = ((TypicalCaseState)elecCaseState).getElecCase();
		if (typicalCase == null) {
			return null;
		} else {
			return typicalCase.getDrawings();
		}
	}

	@Override
	public void setDrawings(String drawings) {
		TypicalCase typicalCase = ((TypicalCaseState)elecCaseState).getElecCase();
		if (typicalCase != null) {
			typicalCase.setDrawings(drawings);
		}
	}
}
