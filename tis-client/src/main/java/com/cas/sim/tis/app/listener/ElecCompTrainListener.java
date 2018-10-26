package com.cas.sim.tis.app.listener;

import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.state.typical.TrainState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElecCompTrainListener extends MouseEventAdapter {

	private TypicalCase3D ui;

	private Step step;
	private TrainState train;

	public ElecCompTrainListener(TypicalCase3D ui, TrainState train) {
		this.ui = ui;
		this.train = train;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (step == null) {
			log.info("ElecCompTrainListener未指定步骤");
			return;
		}
		ElecComp comp = HoldStatePro.ins.getData();
		if (comp == null) {
			return;
		}
		Integer compId = comp.getId();
		if (compId.equals(step.getCompProxy().getId())) {
			train.next();
			Platform.runLater(() -> {
				ui.flowNext();
			});
		} else {
			Platform.runLater(() -> {
				AlertUtil.showTip(TipType.ERROR, MsgUtil.getMessage("alert.error.comp.unmatch"));
			});
		}
	}
}
