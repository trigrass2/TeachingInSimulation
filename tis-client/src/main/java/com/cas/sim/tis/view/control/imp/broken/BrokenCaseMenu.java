package com.cas.sim.tis.view.control.imp.broken;

import java.util.Optional;

import com.cas.sim.tis.action.ArchiveCaseAction;
import com.cas.sim.tis.action.BrokenCaseAction;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.control.imp.ElecCaseMenu;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.cas.sim.tis.view.control.imp.jme.TypicalCaseSelectDialog;
import com.cas.sim.tis.view.controller.PageController;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class BrokenCaseMenu extends ElecCaseMenu {

	public BrokenCaseMenu(ElecCase3D<?> elecCase3D) {
		super(elecCase3D);
	}

	@Override
	protected void showCaseDialog() {
		Dialog<Integer> dialog = new Dialog<>();
		int role = Session.get(Session.KEY_LOGIN_ROLE, RoleConst.STUDENT);
		dialog.setDialogPane(new BrokenCaseSelectDialog(role != RoleConst.STUDENT, role));
		dialog.setTitle(MsgUtil.getMessage("broken.case.title.list"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			BrokenCaseAction action = SpringUtil.getBean(BrokenCaseAction.class);
			BrokenCase brokenCase = action.findBrokenCaseById(id);
			((BrokenCase3D) elecCase3D).setupCase(brokenCase, CaseMode.BROKEN_TRAIN_MODE);
		});
	}

	@Override
	protected void newCase() {
		Dialog<Integer> dialog = new Dialog<>();
		int role = Session.get(Session.KEY_LOGIN_ROLE, RoleConst.STUDENT);
		dialog.setDialogPane(new TypicalCaseSelectDialog(false, role));
		dialog.setTitle(MsgUtil.getMessage("typical.case.title.list"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			ArchiveCaseAction action = SpringUtil.getBean(ArchiveCaseAction.class);
			ArchiveCase typicalCase = action.findArchiveCaseById(id);
			
			SpringUtil.getBean(PageController.class).showLoading();
			// 0、判断当前是否有接线存在
			if (((BrokenCase3D) elecCase3D).isClean()) {
				BrokenCase brokenCase = new BrokenCase();
				brokenCase.setTypicalId(id);
				brokenCase.setName("新建故障 *");
				brokenCase.setArchivePath(typicalCase.getArchivePath());
				((BrokenCase3D) elecCase3D).setupCase(brokenCase, CaseMode.EDIT_MODE);
			} else {
				AlertUtil.showConfirm(MsgUtil.getMessage("elec.case.not.be.clean"), resp -> {
					if (resp == ButtonType.YES) {
						BrokenCase brokenCase = new BrokenCase();
						brokenCase.setTypicalId(id);
						brokenCase.setName("新建故障 *");
						brokenCase.setArchivePath(typicalCase.getArchivePath());
						((BrokenCase3D) elecCase3D).setupCase(brokenCase, CaseMode.EDIT_MODE);
					}
				});
			}
		});
	}

	@Override
	protected void saveCase() {
//		显示等待界面
		SpringUtil.getBean(PageController.class).showLoading();
		try {
			BrokenCase brokenCase = ((BrokenCase3D) elecCase3D).getBrokenCase();
//			如果该案例没有ID，则表明是新增的案例，此时需要用户提供一个案例名称
			if (brokenCase.getId() == null) {
//				创建一个输入对话框，让用户填写案例名称
				TextInputDialog steamIdDialog = new TextInputDialog();
				steamIdDialog.setTitle(MsgUtil.getMessage("menu.button.save"));
				steamIdDialog.setHeaderText(null);
				steamIdDialog.setContentText(MsgUtil.getMessage("broken.case.prompt.input.case"));
				Optional<String> steamID = steamIdDialog.showAndWait();
//				FIXME 用户输入了一个已经存在的案例名称当如何处理
				if (!steamID.isPresent()) {
					return;
				}
				brokenCase.setName(steamID.get());
			}
			((BrokenCase3D) elecCase3D).save();

			AlertUtil.showTip(TipType.INFO, MsgUtil.getMessage("alert.information.data.save.success"));
		} finally {
//			保存完成，关闭等待界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		}
	}
}
