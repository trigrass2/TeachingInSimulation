package com.cas.sim.tis.view.control.imp.broken;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.TypeUtils;
import com.cas.sim.tis.action.BrokenCaseAction;
import com.cas.sim.tis.action.BrokenPublishAction;
import com.cas.sim.tis.action.ClassAction;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.IPublish;
import com.cas.sim.tis.view.control.imp.classes.ClassSelectDialog;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Row;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.control.imp.table.ToggelBtnCell;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.util.DateUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class BrokenCaseSelectDialog extends DialogPane<Integer> {
	private Table table;
	private ToggleGroup group = new ToggleGroup();
	private ToggleButton sys;
	private Column<Boolean> exam;
	private Column<Boolean> visible;
	private Column<String> delete;

	public BrokenCaseSelectDialog(boolean editable, int role) {
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
		// 故障维修案例
		Column<Integer> name = new Column<>();
		name.setKey("name");
		name.setText(MsgUtil.getMessage("preparation.broken.case"));
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
					SpringUtil.getBean(BrokenCaseAction.class).published(rid, published);
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
//				// 发送考核
				exam = new Column<Boolean>();
				exam.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.exam"), "blue-btn", typicalId -> {
					// 获得当前教师负责的班级
					List<Class> classes = SpringUtil.getBean(ClassAction.class).findClassesByTeacherId(Session.get(Session.KEY_LOGIN_ID));

					// 弹出班级选择框
					Dialog<Integer> dialog = new Dialog<>();
					dialog.setDialogPane(new ClassSelectDialog(classes));
					dialog.setTitle(MsgUtil.getMessage("class.dialog.select"));
					dialog.setPrefSize(652, 420);
					dialog.showAndWait().ifPresent(classId -> {
						try {
							Integer publishId = SpringUtil.getBean(BrokenPublishAction.class).publishBroken((Integer) typicalId, classId);
							// 记录当前考核发布编号
							Session.set(Session.KEY_BROKEN_CASE_PUBLISH_ID, publishId);
							// 添加考核进行时菜单
							PageController controller = SpringUtil.getBean(PageController.class);
							ILeftContent content = controller.getLeftMenu();
							if (content instanceof IPublish) {
								((IPublish) content).publish(publishId);
							}
						} catch (Exception e) {
							e.printStackTrace();
							AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
						}
					});
				}));
				exam.setAlignment(Pos.CENTER_RIGHT);
				exam.setPrefWidth(90);
				table.getColumns().addAll(visible, exam);
			}
			// 删除按钮
			delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "red-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
					if (response == ButtonType.NO) {
						return;
					}
					SpringUtil.getBean(BrokenCaseAction.class).deleteByLogic((Integer) rid);
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
		List<BrokenCase> cases = SpringUtil.getBean(BrokenCaseAction.class).getBrokenCaseByCreator(creator, onlyPublished);
		JSONArray array = new JSONArray();
		array.addAll(cases);
		table.setItems(array);
		table.build();
	}
}
