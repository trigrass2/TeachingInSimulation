package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;

import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.controller.PageController;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RecongnizeMenu implements ILeftContent {

	private ToggleGroup group = new ToggleGroup();

	private Recongnize3D recongnize3D;

	public RecongnizeMenu(Recongnize3D recongnize3D) {
		this.recongnize3D = recongnize3D;
		this.group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				this.group.selectToggle(o);
			}
		});
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(10);
//		查询元器件列表
		ElecCompAction elecCompAction = SpringUtil.getBean(ElecCompAction.class);

		List<ElecComp> comps = elecCompAction.getElecCompsByRecongnize();

		comps.forEach(elecComp -> {
			ToggleButton lbl = new ToggleButton(elecComp.getName() + "(" + elecComp.getModel() + ")");
			lbl.setTooltip(new Tooltip(lbl.getText()));
			lbl.getStyleClass().add("titled-content-btn");
			
			lbl.setOnAction(event -> {
				recongnize3D.setElecComp(elecComp);
			});
			vb.getChildren().add(lbl);
			group.getToggles().add(lbl);
		});
		return vb;
	}

	@Override
	public void onMenuAttached(PageController pageController) {
		pageController.setModuleName("menu.item.cognition");
	}

}
