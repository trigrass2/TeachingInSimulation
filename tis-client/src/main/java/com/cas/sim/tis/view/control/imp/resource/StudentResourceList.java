package com.cas.sim.tis.view.control.imp.resource;

import org.json.JSONArray;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.table.BtnsCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.github.pagehelper.PageInfo;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;

public class StudentResourceList extends ResourceList {
	@Override
	protected void createTable() {
		// 编辑按钮
		Column<String> btns = new Column<String>();
		btns.setCellFactory(BtnsCell.forTableColumn());
		btns.setAlignment(Pos.CENTER_RIGHT);
		btns.setPrefWidth(145);
		table.getColumns().add(btns);
	}

	@Override
	protected void loadResources() {
		int userId = Session.get(Session.KEY_LOGIN_USER_ID);

		int curr = pagination.getCurrentPageIndex();
		int pageSize = pagination.getPageCount();

		// FIXME
		String keyword = null;

		String orderByClause = order.getSelectedToggle().getUserData().toString();

		PageInfo<Resource> page = null;
		page = service.findStudentResources(userId, curr, pageSize, resourceTypes, keyword, orderByClause);
		pagination.setMaxPageIndicatorCount((int) page.getTotal());
		table.setItems(new JSONArray(page.getList()));
		table.build();
	}

	@Override
	protected void loadPieChart() {
		int userId = Session.get(Session.KEY_LOGIN_USER_ID);
		// FIXME
		String keyword = null;
		int picNum = service.countStudentResourceByType(userId, 0, resourceTypes, keyword);
		int swfNum = service.countStudentResourceByType(userId, 1, resourceTypes, keyword);
		int videoNum = service.countStudentResourceByType(userId, 2, resourceTypes, keyword);
		int txtNum = service.countStudentResourceByType(userId, 3, resourceTypes, keyword);
		int wordNum = service.countStudentResourceByType(userId, 4, resourceTypes, keyword);
		int pptNum = service.countStudentResourceByType(userId, 5, resourceTypes, keyword);
		int excelNum = service.countStudentResourceByType(userId, 6, resourceTypes, keyword);
		int pdfNum = service.countStudentResourceByType(userId, 7, resourceTypes, keyword);

		chart.setData(FXCollections.observableArrayList(new PieChart.Data(MsgUtil.getMessage("resource.pic"), picNum), new PieChart.Data(MsgUtil.getMessage("resource.swf"), swfNum), new PieChart.Data(MsgUtil.getMessage("resource.video"), videoNum), new PieChart.Data(MsgUtil.getMessage("resource.txt"), txtNum), new PieChart.Data(MsgUtil.getMessage("resource.word"), wordNum), new PieChart.Data(MsgUtil.getMessage("resource.ppt"), pptNum), new PieChart.Data(MsgUtil.getMessage("resource.excel"), excelNum), new PieChart.Data(MsgUtil.getMessage("resource.pdf"), pdfNum)));
	}
}
