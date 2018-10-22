package com.cas.sim.tis.view.control.imp.broken;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.cas.circuit.IBroken;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;
import com.cas.sim.tis.view.controller.PageController;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class BrokenCaseBtnController extends ElecCaseBtnController {

	@FXML
	private HBox broken;
	
	private BrokenFlowItem flow;
	
	private Map<IBroken, BrokenItem> itemMap = new HashMap<>();
	
	private int brokenNum = 3;
	private int chanceNum = 5;

	public BrokenCaseBtnController(CaseMode... enableModes) {
		super(enableModes);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		scroll.setContent(flow = new BrokenFlowItem());
	}
	
	@Override
	protected void switchCaseMode(CaseMode mode) {
//		BrokenCase3D brokenCase3D = (BrokenCase3D) elecCase3D;
		SpringUtil.getBean(PageController.class).showLoading();
		if (CaseMode.BROKEN_TRAIN_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(false);
			broken.setVisible(true);
			view.setVisible(false);
			steps.setVisible(false);
			btns.setVisible(false);
		}else if(CaseMode.BROKEN_EXAM_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(false);
			broken.setVisible(true);
			view.setVisible(false);
			steps.setVisible(false);
			btns.setVisible(false);
			// TODO 提交按钮？？
		} else if (CaseMode.EDIT_MODE == mode) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(true);
			broken.setVisible(false);
			view.setVisible(false);
			steps.setVisible(true);
			btns.setVisible(false);
		}
		if (elecCaseState != null) {
			elecCaseState.setMode(mode);
		}
	}

	@Override
	public String getDrawings() {
//		BrokenCase brokenCase = ((BrokenCaseState) elecCaseState).getElecCase();
//		if (brokenCase == null) {
		return null;
//		} else {
//			return brokenCase.getDrawings();
//		}
	}

	@Override
	public void setDrawings(String drawings) {
//		BrokenCase brokenCase = ((BrokenCaseState) elecCaseState).getElecCase();
//		if (brokenCase != null) {
//			brokenCase.setDrawings(drawings);
//		}
	}

	public void addBrokenItem(IBroken broken) {
		BrokenItem item = new BrokenItem(broken.getDesc());
		itemMap.put(broken, item);
		flow.getChildren().add(item);
	}

	public void removeBrokenItem(IBroken broken) {
		BrokenItem item = itemMap.get(broken);
		flow.getChildren().remove(item);
	}

	@Override
	public void prev() {
		
	}

	@Override
	public void next() {
		
	}
	
	@Override
	protected void submit() {
		if (brokenNum > 0) {
			AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.broken.nonzero"), consumer -> {
				
			});
		}
	}
}
