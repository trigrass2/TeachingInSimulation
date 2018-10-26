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

	private Map<String, BrokenItem> itemMap = new HashMap<>();

	private int correctedNum;// 正确纠正次数
	private int brokenNum;// 练习、考核故障次数随机3个故障，故障案例不满3个故障则按全部故障进行测试
	private int chanceNum;// 错误机会次数

	private Integer publishId;

	public BrokenCaseBtnController(CaseMode... enableModes) {
		super(enableModes);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		scroll.setContent(flow = new BrokenFlowItem());
		submit.setOnAction(e -> {
			AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.broken.nonzero"), c -> {
				if (ButtonType.YES == c) {
					submit(publishId);
					AlertUtil.showAlert(AlertType.INFORMATION, "提交完成！");
				}
			});
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
		removeBrokenItem(broken);
		BrokenItem item = new BrokenItem(broken.getDesc());
		itemMap.put(broken.getKey(), item);
		flow.getChildren().add(item);
	}

	public void removeBrokenItem(IBroken broken) {
		BrokenItem item = itemMap.get(broken.getKey());
		if (item != null) {
			flow.getChildren().remove(item);
		}
	}

	public void checkSubmit(boolean forced) {
		if (publishId == null) {
			if (chanceNum == 0) {
				((BrokenCase3D) elecCase3D).setFinish(true);
				AlertUtil.showConfirm("您的机会已经用完，是否重新开始？", e -> {
					if (ButtonType.YES == e) {
						((BrokenCase3D) elecCase3D).resetTrainCase();
					}
				});
			} else if (brokenNum == 0) {
				AlertUtil.showAlert(AlertType.INFORMATION, "大吉大利，今晚吃鸡！");
				((BrokenCase3D) elecCase3D).setFinish(true);
			}
		} else if (chanceNum == 0) {
			submit(publishId);
			AlertUtil.showAlert(AlertType.ERROR, "您的机会已经用完，排故失败！");
		} else if (brokenNum == 0) {
			submit(publishId);
			AlertUtil.showAlert(AlertType.INFORMATION, "大吉大利，今晚吃鸡！");
		} else if (forced) {
			submit(publishId);
			AlertUtil.showAlert(AlertType.INFORMATION, "考试结束！");
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
		checkSubmit(false);
	}

	public void decreaseChanceNume() {
		chanceNum--;
		chance.setText(String.valueOf(chanceNum));
		checkSubmit(false);
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

	public void submit(Integer publishId) {
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
