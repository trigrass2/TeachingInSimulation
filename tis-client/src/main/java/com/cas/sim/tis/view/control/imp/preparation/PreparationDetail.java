package com.cas.sim.tis.view.control.imp.preparation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.CatalogAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.action.PreparationAction;
import com.cas.sim.tis.action.PreparationQuizAction;
import com.cas.sim.tis.action.PreparationResourceAction;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.action.UserAction;
import com.cas.sim.tis.consts.PreparationResourceType;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.entity.Question;
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
import com.cas.sim.tis.view.control.imp.question.PreviewQuestionItem;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.SVGIconCell;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
import com.cas.sim.tis.vo.PreparationInfo;
import com.cas.sim.tis.vo.PreparationQuizInfo;

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

	@FXML
	private Title title;
	@FXML
	private FlowPane points;
	@FXML
	private Table resces;
	@FXML
	private Table tests;
	@FXML
	private VBox quizs;
	@FXML
	private VBox asks;
	@FXML
	private VBox a;
	@FXML
	private VBox s;
	@FXML
	private VBox k;

	private Catalog task;
	private Preparation preparation;

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		this.title.setTitle(task.getName());

		createResourceTable();
		createTestTable();

		loadPreparation();

		loadPoints();
		loadResources();
		loadQuizs();
		loadTests();
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
				if (ResourceType.LINK == resourceType) {
					return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22);
				} else {
					return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22, 25);
				}
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
				// TODO
			} else if (PreparationResourceType.TYPICAL.getType() == type) {
				// TODO
			}
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		resces.getColumns().add(view);
		if (RoleConst.TEACHER == Session.get(Session.KEY_LOGIN_ROLE, 0)) {
			// 删除按钮
			Column<String> delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("table.delete"), response -> {
					if (response == ButtonType.YES) {
						SpringUtil.getBean(PreparationResourceAction.class).detele((Integer) rid);
						loadResources();
					}
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			resces.getColumns().add(delete);
		}
	}

	private void createTestTable() {
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
				return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22, 25);
			}
		};
		icon.setCellFactory(SVGIconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("resource.name"));
		name.setMaxWidth(250);
		tests.getColumns().addAll(id, icon, name);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", rid -> {
			// TODO 打开故障练习、自由接线
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		tests.getColumns().add(view);
		if (RoleConst.TEACHER == Session.get(Session.KEY_LOGIN_ROLE, -1)) {
			// 删除按钮
			Column<String> delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("table.delete"), response -> {
					if (response == ButtonType.YES) {
						SpringUtil.getBean(PreparationResourceAction.class).detele((Integer) rid);
						loadResources();
					}
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			tests.getColumns().add(delete);
		}
	}

	private void loadPreparation() {
		int cid = task.getId();
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		int creator = Session.get(Session.KEY_LOGIN_ID);
		if (RoleConst.STUDENT == role) {
			User user = SpringUtil.getBean(UserAction.class).findUserByID(creator);
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

	private void loadPoints() {
		List<Catalog> points = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(task.getId());
		for (Catalog point : points) {
			createPointItem(point);
		}
	}

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
		int index = 1;
		List<PreparationQuizInfo> quizs = SpringUtil.getBean(PreparationQuizAction.class).findQuizsByPreparationId(preparation.getId());
		for (PreparationQuizInfo quiz : quizs) {
			Question question = quiz.getQuestion();
			PreviewQuestionItem item = new PreviewQuestionItem(index++, QuestionType.getQuestionType(question.getType()), question, RoleConst.TEACHER == Session.get(Session.KEY_LOGIN_ROLE, 0));
			item.setUserData(quiz.getId());
			this.quizs.getChildren().add(item);
		}
	}

	private void loadTests() {
		if (preparation == null) {
			return;
		}
		List<PreparationInfo> tests = SpringUtil.getBean(PreparationQuizAction.class).findTestsByPreparationId(preparation.getId());
		JSONArray array = new JSONArray();
		array.addAll(tests);
		this.tests.setItems(array);
		this.tests.build();
	}

	private void createPointItem(Catalog point) {
		CheckBox pointItem = new CheckBox(point.getName());
		pointItem.getStyleClass().add("point-check-box");
		pointItem.selectedProperty().addListener((b, o, n) -> {
			refreshASK();
		});
		pointItem.setSelected(true);
		points.getChildren().add(pointItem);
	}

	private void refreshASK() {
		// TODO 根据选择的知识点设置刷新学习目标中ASK的选中情况
	}

	@FXML
	private void cognition() {
		Map<String, List<ElecComp>> elecCompMap = SpringUtil.getBean(ElecCompAction.class).getElecCompMap();

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
		List<TypicalCase> cases = SpringUtil.getBean(TypicalCaseAction.class).getTypicalCaseList();

		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new TypicalCaseSelectDialog(cases));
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
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new ResourceUploadDialog());
		dialog.setTitle(MsgUtil.getMessage("preparation.local.resource"));
		dialog.setPrefSize(640, 330);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			// 记录到数据库
			addResource(id, PreparationResourceType.RESOURCE.getType());
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

	}

	private void addResource(Integer id, int type) {
		PreparationResource resource = new PreparationResource();
		resource.setRelationId(id);
		resource.setPreparationId(preparation.getId());
		resource.setType(type);
		try {
			SpringUtil.getBean(PreparationResourceAction.class).addResource(resource);
			loadResources();
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("data.add.success"));
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	private void openResource(Integer id) {
		ResourceAction action = SpringUtil.getBean(ResourceAction.class);
		action.browsed(id);
		Resource resource = action.findResourceByID(id);
		// 跳转到查看页面
		PageController controller = SpringUtil.getBean(PageController.class);
		controller.loadContent(new ResourceViewer(resource), PageLevel.Level2);
	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		return new Region[] { this };
	}

}
