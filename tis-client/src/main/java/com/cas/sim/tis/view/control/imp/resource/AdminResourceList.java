package com.cas.sim.tis.view.control.imp.resource;

import org.json.JSONArray;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.table.BtnsCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.github.pagehelper.PageInfo;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;

public class AdminResourceList extends ResourceList {

	private boolean editable;

	public AdminResourceList(boolean editable) {
		this.editable = editable;
	}

	@Override
	protected void createTable() {
		super.createTable();
		if (editable) {
			// 编辑按钮
			Column<String> btns = new Column<String>();
			btns.setCellFactory(BtnsCell.forTableColumn());
			btns.setAlignment(Pos.CENTER_RIGHT);
			btns.setPrefWidth(145);
			table.getColumns().add(btns);
		}
	}

	@Override
	protected void loadResources() {
		int curr = pagination.getCurrentPageIndex();
		int pageSize = pagination.getPageCount();

		// FIXME
		String keyword = null;

		String orderByClause = order.getSelectedToggle().getUserData().toString();

		PageInfo<Resource> page = null;
		page = service.findAdminResources(curr, pageSize, resourceTypes, keyword, orderByClause);
		pagination.setMaxPageIndicatorCount((int) page.getTotal());
		table.setItems(new JSONArray(page.getList()));
		table.build();
	}

	@Override
	protected void loadPieChart() {
		// FIXME
		String keyword = null;
		int picNum = service.countAdminResourceByType(0, resourceTypes, keyword);
		int swfNum = service.countAdminResourceByType(1, resourceTypes, keyword);
		int videoNum = service.countAdminResourceByType(2, resourceTypes, keyword);
		int txtNum = service.countAdminResourceByType(3, resourceTypes, keyword);
		int wordNum = service.countAdminResourceByType(4, resourceTypes, keyword);
		int pptNum = service.countAdminResourceByType(5, resourceTypes, keyword);
		int excelNum = service.countAdminResourceByType(6, resourceTypes, keyword);
		int pdfNum = service.countAdminResourceByType(7, resourceTypes, keyword);

		chart.setData(FXCollections.observableArrayList(new PieChart.Data(MsgUtil.getMessage("resource.pic"), picNum), new PieChart.Data(MsgUtil.getMessage("resource.swf"), swfNum), new PieChart.Data(MsgUtil.getMessage("resource.video"), videoNum), new PieChart.Data(MsgUtil.getMessage("resource.txt"), txtNum), new PieChart.Data(MsgUtil.getMessage("resource.word"), wordNum), new PieChart.Data(MsgUtil.getMessage("resource.ppt"), pptNum), new PieChart.Data(MsgUtil.getMessage("resource.excel"), excelNum), new PieChart.Data(MsgUtil.getMessage("resource.pdf"), pdfNum)));
	}
}
