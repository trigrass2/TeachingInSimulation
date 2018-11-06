package com.cas.sim.tis.view.control.imp.jme;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.TypeUtils;
import com.cas.sim.tis.action.ArchiveCaseAction;
import com.cas.sim.tis.consts.ArchiveType;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Row;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.control.imp.table.ToggelBtnCell;
import com.cas.util.DateUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class TypicalCaseSelectDialog extends DialogPane<Integer> {

	private Table table;
	private ToggleGroup group = new ToggleGroup();
	private ToggleButton sys;
	private Column<Boolean> visible;
	private Column<String> delete;

	public TypicalCaseSelectDialog(boolean editable, int role) {
		VBox box = new VBox(20);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		HBox toggleBox = new HBox(10);
		if (role == RoleConst.TEACHER) {
			ToggleButton mine = new ToggleButton(MsgUtil.getMessage("elec.case.min.case"));
			mine.setMinSize(100, 40);
			mine.setStyle("-fx-font-size:14px");
			mine.setUserData(Session.get(Session.KEY_LOGIN_ID));
			group.getToggles().add(mine);
			toggleBox.getChildren().add(mine);
		} else if (role == RoleConst.STUDENT) {
			ToggleButton tech = new ToggleButton(MsgUtil.getMessage("elec.case.tech.case"));
			tech.setMinSize(100, 40);
			tech.setStyle("-fx-font-size:14px");

			User user = Session.get(Session.KEY_OBJECT);
			tech.setUserData(user.getTeacherId());
			group.getToggles().add(tech);
			toggleBox.getChildren().add(tech);
		}
		sys = new ToggleButton(MsgUtil.getMessage("elec.case.sys.case"));
		sys.setMinSize(100, 40);
		sys.setStyle("-fx-font-size:14px");
		sys.setUserData(1);
		group.getToggles().add(sys);
		toggleBox.getChildren().add(sys);
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				group.selectToggle(o);
			} else {
				reload(role);
			}
		});

		table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setRowsSpacing(1);
		table.setSeparatorable(false);
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 案例
		Column<Integer> name = new Column<>();
		name.setKey("name");
		name.setText(MsgUtil.getMessage("preparation.typical.case"));
		// 日期
		Column<Date> date = new Column<>();
		date.setAlignment(Pos.CENTER);
		date.setKey("createDate");
		date.setText(MsgUtil.getMessage("preparation.create.date"));
		date.setMaxWidth(160);
		date.setCellFactory(Cell.forTableColumn(new StringConverter<Date>() {

			@Override
			public String toString(Date date) {
				return DateUtil.date2Str(date, DateUtil.DATE_TIME_PAT_SHOW_);
			}

			@Override
			public Date fromString(String string) {
				return null;
			}
		}));
		table.getColumns().addAll(id, name, date);
		if (editable) {
			if (role != RoleConst.ADMIN) {
				// 设置学生可见按钮
				visible = new Column<Boolean>();
				visible.setKey("publish");
				visible.setCellFactory(ToggelBtnCell.forTableColumn(MsgUtil.getMessage("button.published"), MsgUtil.getMessage("button.unpublished"), "toggle-button", json -> {
					Integer rid = json.getInteger("id");
					boolean published = json.getBooleanValue("selected");
					// 修改典型案例状态
					SpringUtil.getBean(ArchiveCaseAction.class).published(rid, published);
					reload(role);
				}, new StringConverter<Boolean>() {

					@Override
					public String toString(Boolean published) {
						if (published) {
							return MsgUtil.getMessage("button.published");
						} else {
							return MsgUtil.getMessage("button.unpublished");
						}
					}

					@Override
					public Boolean fromString(String string) {
						return null;
					}
				}));
				visible.setAlignment(Pos.CENTER_RIGHT);
				table.getColumns().add(visible);
			}
			// 删除按钮
			delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "red-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
					if (response == ButtonType.NO) {
						return;
					}
					SpringUtil.getBean(ArchiveCaseAction.class).deleteByLogic((Integer) rid);
					reload(role);
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			table.getColumns().add(delete);
		}

		ScrollPane scroll = new ScrollPane(table);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		VBox.setVgrow(scroll, Priority.ALWAYS);

		Label error = new Label();
		error.getStyleClass().add("red");

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setMinSize(390, 40);
		ok.setPrefSize(390, 40);
		ok.setOnAction(e -> {
			Row row = table.getSelectedRow();
			if (row == null) {
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("preparation.typical.case")));
				return;
			}
			this.setResult(row.getItems().getInteger("id"));
		});

		box.getChildren().addAll(toggleBox, scroll, error, ok);
		getChildren().add(box);

		group.selectToggle(group.getToggles().get(0));
	}

	private void reload(int role) {
		Toggle toggle = group.getSelectedToggle();
		Integer creator = TypeUtils.castToInt(toggle.getUserData());
		boolean onlyPublished = role == RoleConst.STUDENT;
		if (delete == null) {
			if (onlyPublished && sys.isSelected()) {
				onlyPublished = false;
			}
		} else if (sys.isSelected()) {
			if (Session.get(Session.KEY_LOGIN_ROLE, 0) != RoleConst.ADMIN) {
				table.getColumns().remove(visible);
				table.getColumns().remove(delete);
			}
		} else if (!table.getColumns().contains(delete)) {
			table.getColumns().add(visible);
			table.getColumns().add(delete);
		}
		List<ArchiveCase> cases = SpringUtil.getBean(ArchiveCaseAction.class).getArchiveCasesByCreator(creator, onlyPublished, ArchiveType.TYPICAL.getIndex());
		JSONArray array = new JSONArray();
		array.addAll(cases);
		table.setItems(array);
		table.build();
	}
}
