package com.cas.sim.tis.view.control.imp.preparation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.BrokenCaseAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.action.GoalAction;
import com.cas.sim.tis.action.GoalCoverageAction;
import com.cas.sim.tis.action.PreparationAction;
import com.cas.sim.tis.action.PreparationQuizAction;
import com.cas.sim.tis.action.PreparationResourceAction;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.action.UserAction;
import com.cas.sim.tis.consts.GoalRelationshipType;
import com.cas.sim.tis.consts.GoalType;
import com.cas.sim.tis.consts.PreparationQuizType;
import com.cas.sim.tis.consts.PreparationResourceType;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ResourceViewer;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.jme.Recongnize3D;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.control.imp.question.PreviewQuestionPaper;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.SVGIconCell;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
import com.cas.sim.tis.vo.PreparationInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PreparationDetail extends HBox implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(PreparationDetail.class);

	@FXML
	private Title title;
	@FXML
	private FlowPane objectives;
	@FXML
	private Table resces;
	@FXML
	private Table quizs;
	@FXML
	private VBox a;
	@FXML
	private VBox s;
	@FXML
	private VBox k;

	private Catalog task;
	private Preparation preparation;

	private Map<Integer, CheckBox> askChecks = new HashMap<Integer, CheckBox>();
	private Map<Integer, CheckBox> oChecks = new HashMap<Integer, CheckBox>();

	public PreparationDetail(Catalog task) {
		this.task = task;

		loadFXML();
		initialize();
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/preparation/Detail.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			loader.load();
			LOG.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
	}

	private void initialize() {
		this.title.setTitle(task.getName());

		createResourceTable();
		createQuizTable();

		loadPreparation();

		refreshASK();
		loadASK(task.getId(), GoalRelationshipType.TASK.getType());

//		loadPoints();
		loadResources();
		loadQuizs();
	}

	private void createResourceTable() {
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 资源图标
		Column<Integer> icon = new Column<>();
		icon.setAlignment(Pos.CENTER_RIGHT);
		icon.setKey("icon");
		icon.setText("");
		icon.setMaxWidth(22);
		Function<Integer, SVGGlyph> converter = new Function<Integer, SVGGlyph>() {

			@Override
			public SVGGlyph apply(Integer type) {
				if (type == null) {
					return null;
				}
				ResourceType resourceType = ResourceType.getResourceType(type);
				return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22);
			}
		};
		icon.setCellFactory(SVGIconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("resource.name"));
		name.setMaxWidth(250);
		resces.getColumns().addAll(id, icon, name);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", rid -> {
			PreparationResource preparationResource = SpringUtil.getBean(PreparationResourceAction.class).findResourceById((Integer) rid);
			int type = preparationResource.getType();
			if (PreparationResourceType.RESOURCE.getType() == type) {
				openResource(preparationResource.getRelationId());
			} else if (PreparationResourceType.COGNITION.getType() == type) {
				openCognition(preparationResource.getRelationId());
			} else if (PreparationResourceType.TYPICAL.getType() == type) {
				openTypicalCase(preparationResource.getRelationId());
			}
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		resces.getColumns().add(view);
		if (RoleConst.TEACHER == Session.get(Session.KEY_LOGIN_ROLE, 0)) {
			// 删除按钮
			Column<String> delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
					if (response == ButtonType.YES) {
						SpringUtil.getBean(PreparationResourceAction.class).deteleByLogic((Integer) rid);
						loadResources();
					}
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			resces.getColumns().add(delete);
		}
	}

	private void createQuizTable() {
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 资源图标
		Column<Integer> icon = new Column<>();
		icon.setAlignment(Pos.CENTER_RIGHT);
		icon.setKey("icon");
		icon.setText("");
		icon.setMaxWidth(25);
		Function<Integer, SVGGlyph> converter = new Function<Integer, SVGGlyph>() {

			@Override
			public SVGGlyph apply(Integer type) {
				if (type == null) {
					return null;
				}
				ResourceType resourceType = ResourceType.getResourceType(type);
				return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22);
			}
		};
		icon.setCellFactory(SVGIconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("resource.name"));
		name.setMaxWidth(250);
		quizs.getColumns().addAll(id, icon, name);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", rid -> {
			PreparationQuiz preparationQuiz = SpringUtil.getBean(PreparationQuizAction.class).findQuizById((Integer) rid);
			int type = preparationQuiz.getType();
			if (PreparationQuizType.LIBRARY.getType() == type) {
				openLibrary(preparationQuiz.getRelationId());
			} else if (PreparationQuizType.BROKEN_CASE.getType() == type) {
				openBrokenCase(preparationQuiz.getRelationId());
			} else if (PreparationQuizType.FREE.getType() == type) {
				openFreeMode();
			}
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		quizs.getColumns().add(view);
		if (RoleConst.TEACHER == Session.get(Session.KEY_LOGIN_ROLE, -1)) {
			// 删除按钮
			Column<String> delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
					if (response == ButtonType.YES) {
						SpringUtil.getBean(PreparationResourceAction.class).deteleByLogic((Integer) rid);
						loadResources();
					}
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			quizs.getColumns().add(delete);
		}
		quizs.selectedRowProperty().addListener((b, o, n) -> {
			if (n == null) {
				loadASK(task.getId(), GoalRelationshipType.TASK.getType());
			} else {
				Integer rid = n.getItems().getIntValue("id");
				loadASK(rid, GoalRelationshipType.QUIZ.getType());
			}
		});
	}

	private void loadASK(Integer rid, int type) {
		List<GoalCoverage> coverages = SpringUtil.getBean(GoalCoverageAction.class).findGoalIdsByRid(rid, type);
		for (CheckBox box : askChecks.values()) {
			box.setSelected(false);
		}
		for (GoalCoverage coverage : coverages) {
			askChecks.get(coverage.getGoalId()).setSelected(true);
		}
	}

	private void loadPreparation() {
		int cid = task.getId();
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		int creator = Session.get(Session.KEY_LOGIN_ID);
		if (RoleConst.STUDENT == role) {
			User user = SpringUtil.getBean(UserAction.class).findUserById(creator);
			creator = user.getTeacherId();
		}
		// 查询是否存在自定义备课内容
		preparation = SpringUtil.getBean(PreparationAction.class).findPreparationByTaskIdAndCreator(cid, creator);
		// 查询是否存在模版备课内容
		if (preparation == null) {
			// XXX 暂时规定默认备课模版创建人编号为0
			preparation = SpringUtil.getBean(PreparationAction.class).findPreparationByTaskIdAndCreator(cid, 0);
		}
		if (preparation == null && RoleConst.TEACHER == role) {
			preparation = new Preparation();
			preparation.setCatalogId(cid);
			preparation.setCreator(creator);
			preparation = SpringUtil.getBean(PreparationAction.class).addPreparation(preparation);
		}
	}

//	private void loadPoints() {
//		List<Catalog> points = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(task.getId());
//		for (Catalog point : points) {
//			createPointItem(point);
//		}
//	}

	private void loadResources() {
		if (preparation == null) {
			return;
		}
		List<PreparationInfo> resources = SpringUtil.getBean(PreparationResourceAction.class).findResourcesByPreparationId(preparation.getId());
		JSONArray array = new JSONArray();
		array.addAll(resources);
		this.resces.setItems(array);
		this.resces.build();
	}

	private void loadQuizs() {
		if (preparation == null) {
			return;
		}
		List<PreparationInfo> quizs = SpringUtil.getBean(PreparationQuizAction.class).findQuizsByPreparationId(preparation.getId());
		JSONArray array = new JSONArray();
		array.addAll(quizs);
		this.quizs.setItems(array);
		this.quizs.build();
	}

//	private void createPointItem(Catalog point) {
//		CheckBox pointItem = new CheckBox(point.getName());
//		pointItem.getStyleClass().add("point-check-box");
//		pointItem.selectedProperty().addListener((b, o, n) -> {
//			refreshASK();
//		});
//		pointItem.setSelected(true);
//		points.getChildren().add(pointItem);
//	}

	private void refreshASK() {
		// TODO 根据选择的知识点设置刷新学习目标中ASK的选中情况
		List<Goal> goals = SpringUtil.getBean(GoalAction.class).findGoalsByRid(task.getId(), GoalRelationshipType.TASK.getType());
		for (Goal goal : goals) {
			int type = goal.getType();
			Integer id = goal.getId();
			String name = goal.getName();
			if (GoalType.OBJECTIVE.getType() == type) {
				CheckBox checkBox = new CheckBox(name);
				checkBox.getStyleClass().add("point-check-box");
				checkBox.setSelected(false);
				checkBox.setDisable(true);
				objectives.getChildren().add(checkBox);
				oChecks.put(id, checkBox);
			} else {
				CheckBox checkBox = new CheckBox();
				checkBox.setUserData(id);
				checkBox.setText(goal.getName());
//				checkBox.setDisable(true);
				checkBox.getStyleClass().add("ask-check-box");
				checkBox.setOnAction(e -> {
					// 根据选择增删目标关系
					if (checkBox.isSelected()) {
						SpringUtil.getBean(GoalCoverageAction.class).insertRelationship(id, task.getId(), GoalRelationshipType.TASK.getType());
					} else {
						SpringUtil.getBean(GoalCoverageAction.class).deleteRelationship(id, task.getId(), GoalRelationshipType.TASK.getType());
					}
					// 获得当前选择的ASK满足的O
					for (Entry<Integer, CheckBox> entry : oChecks.entrySet()) {
						entry.getValue().setSelected(SpringUtil.getBean(GoalCoverageAction.class).checkObjectiveCoverage(entry.getKey(), task.getId()));
					}
				});
				if (GoalType.ATTITUDE.getType() == type) {
					a.getChildren().add(checkBox);
				} else if (GoalType.SKILL.getType() == type) {
					s.getChildren().add(checkBox);
				} else if (GoalType.KNOWLEDGE.getType() == type) {
					k.getChildren().add(checkBox);
				}
				askChecks.put(id, checkBox);
			}
		}
	}

	@FXML
	private void cognition() {
		Map<Integer, List<ElecComp>> elecCompMap = SpringUtil.getBean(ElecCompAction.class).getElecCompMap();

		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new ElecCompSelectDialog(elecCompMap));
		dialog.setTitle(MsgUtil.getMessage("preparation.elec.comp"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			addResource(id, PreparationResourceType.COGNITION.getType());
		});
	}

	@FXML
	private void typicalCase() {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new TypicalCaseSelectDialog(false));
		dialog.setTitle(MsgUtil.getMessage("preparation.typical.case"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			addResource(id, PreparationResourceType.TYPICAL.getType());
		});
	}

	@FXML
	private void loadResource() {
		Dialog<List<Integer>> dialog = new Dialog<>();
		dialog.setDialogPane(new ResourceUploadDialog());
		dialog.setTitle(MsgUtil.getMessage("preparation.local.resource"));
		dialog.setPrefSize(640, 330);
		dialog.showAndWait().ifPresent(ids -> {
			if (ids == null) {
				return;
			}
			addResources(ids, PreparationResourceType.RESOURCE.getType());
		});
	}

	@FXML
	private void resource() {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new ResourceSelectedDialog());
		dialog.setTitle(MsgUtil.getMessage("preparation.library.resource"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			addResource(id, PreparationResourceType.RESOURCE.getType());
		});
	}

	@FXML
	private void quiz() {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new LibrarySelectDialog());
		dialog.setTitle(MsgUtil.getMessage("preparation.question.library"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			addQuiz(id, PreparationQuizType.LIBRARY.getType());
		});
	}

	@FXML
	private void repair() {
		List<BrokenCase> cases = SpringUtil.getBean(BrokenCaseAction.class).getBrokenCaseList();

		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new BrokenCaseSelectDialog(cases));
		dialog.setTitle(MsgUtil.getMessage("preparation.broken.case"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			addQuiz(id, PreparationQuizType.BROKEN_CASE.getType());
		});
	}

	@FXML
	private void free() {
		if (SpringUtil.getBean(PreparationQuizAction.class).checkFreeQuiz(preparation.getId())) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.exsit", MsgUtil.getMessage("preparation.free.case")));
			return;
		}
		addQuiz(null, PreparationQuizType.FREE.getType());
	}

	private void addResource(Integer id, int type) {
		PreparationResource resource = new PreparationResource();
		resource.setRelationId(id);
		resource.setPreparationId(preparation.getId());
		resource.setType(type);
		try {
			SpringUtil.getBean(PreparationResourceAction.class).addResources(resource);
			loadResources();
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.data.add.success"));
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	private void addResources(List<Integer> ids, int type) {
		List<PreparationResource> resources = new ArrayList<PreparationResource>();
		int creator = Session.get(Session.KEY_LOGIN_ID);
		for (Integer id : ids) {
			PreparationResource resource = new PreparationResource();
			resource.setRelationId(id);
			resource.setPreparationId(preparation.getId());
			resource.setType(type);
			resource.setCreator(creator);
			resources.add(resource);
		}
		try {
			SpringUtil.getBean(PreparationResourceAction.class).addResources(resources);
			loadResources();
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.data.add.success"));
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	private void addQuiz(Integer id, int type) {
		PreparationQuiz quiz = new PreparationQuiz();
		quiz.setRelationId(id);
		quiz.setPreparationId(preparation.getId());
		quiz.setType(type);
		try {
			SpringUtil.getBean(PreparationQuizAction.class).addQuiz(quiz);
			loadQuizs();
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.data.add.success"));
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	private void openResource(Integer id) {
		ResourceAction action = SpringUtil.getBean(ResourceAction.class);
		action.browsed(id);
		Resource resource = action.findResourceById(id);
		// 跳转到查看页面
		PageController controller = SpringUtil.getBean(PageController.class);
		controller.loadContent(new ResourceViewer(resource), PageLevel.Level2);
	}

	private void openCognition(Integer id) {
		ElecComp comp = SpringUtil.getBean(ElecCompAction.class).findElecCompById(id);

		PageController controller = SpringUtil.getBean(PageController.class);
		Recongnize3D content = new Recongnize3D();

		controller.loadContent(content, PageLevel.Level2);
		controller.showLoading();
		controller.setEndHideLoading((v) -> {
			content.setElecComp(comp);
		});
	}

	private void openTypicalCase(Integer id) {
		TypicalCase typicalCase = SpringUtil.getBean(TypicalCaseAction.class).findTypicalCaseById(id);

		PageController controller = SpringUtil.getBean(PageController.class);
		TypicalCase3D content = new TypicalCase3D();

		controller.loadContent(content, PageLevel.Level2);
		controller.showLoading();
		controller.setEndHideLoading((v) -> {
			content.setupCase(typicalCase);
		});
	}

	private void openLibrary(Integer id) {
		PageController controller = SpringUtil.getBean(PageController.class);
		controller.loadContent(new PreviewQuestionPaper(id, false, true), PageLevel.Level2);
	}

	private void openBrokenCase(Integer id) {

	}

	private void openFreeMode() {

	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		return new Region[] { this };
	}

}
