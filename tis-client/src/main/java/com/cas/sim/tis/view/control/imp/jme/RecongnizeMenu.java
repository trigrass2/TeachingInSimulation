package com.cas.sim.tis.view.control.imp.jme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.ElecCompAction;
import com.cas.sim.tis.view.control.imp.Tree;
import com.cas.sim.tis.view.control.imp.TreeLeaf;
import com.cas.sim.tis.view.control.imp.TreeLeaf.Level;

public class RecongnizeMenu extends Tree {

	@Override
	protected List<TreeLeaf> loadTreeData() {
//		查询元器件列表
		ElecCompAction elecCompAction = SpringUtil.getBean(ElecCompAction.class);
		Map<String, List<ElecComp>> map = elecCompAction.getElecCompMap();

		List<TreeLeaf> branches = new ArrayList<>();
		map.entrySet().forEach(entry -> {
			TreeLeaf branch = new TreeLeaf(entry.getKey(), Level.Level1, false);
			branches.add(branch);
			entry.getValue().forEach(e -> {
				TreeLeaf leaf = new TreeLeaf(e.getName() + "(" + e.getModel() + ")", Level.Level2, false);
				branch.getChildren().add(leaf);
			});
		});

		return branches;
	}
}
