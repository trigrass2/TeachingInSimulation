package com.cas.sim.tis.view.control.imp.broken;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.cas.circuit.IBroken;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ExamBrokenRecord;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCaseBtnController;
import com.cas.sim.tis.view.controller.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BrokenCaseBtnController extends ElecCaseBtnController {

	@FXML
	private HBox broken;
	@FXML
	private Label number;
	@FXML
	private Label chance;

	private BrokenFlowItem flow;

	private Map<IBroken, BrokenItem> itemMap = new HashMap<>();

	private int correctedNum;
	private int brokenNum;
	private int chanceNum;

	private Integer publishId;

	public BrokenCaseBtnController(CaseMode... enableModes) {
		super(enableModes);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		scroll.setContent(flow = new BrokenFlowItem());
		submit.setOnAction(e -> {
			submit(false);
		});
	}

	@Override
	protected void switchCaseMode(CaseMode mode) {
		SpringUtil.getBean(PageController.class).showLoading();
		trainOrEdit.toFront();
		view.setVisible(false);
		btns.setVisible(false);
		if (CaseMode.BROKEN_TRAIN_MODE == mode) {
			trainOrEdit.setVisible(false);
			broken.setVisible(true);
			steps.setVisible(false);
		} else if (CaseMode.BROKEN_EXAM_MODE == mode) {
			trainOrEdit.setVisible(false);
			broken.setVisible(true);
			steps.setVisible(false);
		} else if (CaseMode.EDIT_MODE == mode) {
			trainOrEdit.setVisible(true);
			broken.setVisible(false);
			steps.setVisible(true);
		}
		if (flow != null) {
			flow.getChildren().clear();
		}
		if (elecCaseState != null) {
			elecCaseState.setMode(mode);
		}
		if (elecCase3D != null) {
			((BrokenCase3D) elecCase3D).setFinish(false);
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

	public void submit(boolean forced) {
		if (publishId == null) {
			if (brokenNum == 0) {
				AlertUtil.showAlert(AlertType.INFORMATION, "大吉大利，今晚吃鸡！");
			}
		} else if (brokenNum == 0) {
			finish(publishId);
			AlertUtil.showAlert(AlertType.INFORMATION, "大吉大利，今晚吃鸡！");
		} else if (forced) {
			finish(publishId);
		} else {
			AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.broken.nonzero"), c -> {
				if (ButtonType.YES == c) {
					finish(publishId);
					AlertUtil.showAlert(AlertType.INFORMATION, "提交完成！");
				}
			});
		}
	}

	@Override
	public void clean() {
		super.clean();
		flow.getChildren().clear();
	}

	public void decreaseBrokenNume() {
		brokenNum--;
		correctedNum++;
		number.setText(String.valueOf(brokenNum));
		if (brokenNum == 0) {
			submit(false);
		}
	}

	public void decreaseChanceNume() {
		chanceNum--;
		chance.setText(String.valueOf(chanceNum));
		if (chanceNum == 0) {
			AlertUtil.showAlert(AlertType.ERROR, "您的机会已经用完，排故失败！");
			if (publishId != null) {
				finish(publishId);
			}
		}
	}

	public void init(int num) {
		submit.setDisable(false);

		correctedNum = 0;
		brokenNum = num;
		number.setText(String.valueOf(brokenNum));
		chanceNum = 5;
		chance.setText(String.valueOf(chanceNum));

		publishId = Session.get(Session.KEY_BROKEN_CASE_PUBLISH_ID);
		submit.setVisible(publishId != null);
	}

	public void finish(Integer publishId) {
		submit.setDisable(true);
		ExamBrokenRecord record = new ExamBrokenRecord();
		record.setCorrectedNum(correctedNum);
		record.setMistakeNum(5 - chanceNum);
		record.setBrokenNum(brokenNum);
		record.setPublishId(publishId);
		((BrokenCaseState) elecCaseState).submit(record);

		PageController controller = SpringUtil.getBean(PageController.class);
		controller.setBackEnable(true);

		((BrokenCase3D) elecCase3D).setFinish(true);

		Session.set(Session.KEY_BROKEN_CASE_PUBLISH_ID, null);
	}
}
