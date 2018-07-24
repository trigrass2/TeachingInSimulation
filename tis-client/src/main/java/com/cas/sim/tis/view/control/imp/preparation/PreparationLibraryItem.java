package com.cas.sim.tis.view.control.imp.preparation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.action.ClassAction;
import com.cas.sim.tis.action.PreparationLibraryAction;
import com.cas.sim.tis.action.PreparationPublishAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.PreparationLibrary;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.IPublish;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.classes.ClassSelectDialog;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.controller.PageController;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreparationLibraryItem extends VBox {

	@FXML
	private Title name;
	@FXML
	private VBox questions;
	private PreparationLibrary library;
	private PreparationDetail preparationDetail;

	private final ReadOnlyObjectWrapper<PreparationQuestionItem> selected = new ReadOnlyObjectWrapper<PreparationQuestionItem>() {
		@Override
		public void set(final PreparationQuestionItem newSelected) {
			final PreparationQuestionItem old = get();
			if (newSelected == null) {
				if (old != null) {
					old.getStyleClass().remove("question-selected");
				}
				super.set(null);
				return;
			}
			if (old == newSelected && newSelected.getStyleClass().contains("question-selected")) {
				newSelected.getStyleClass().remove("question-selected");
				super.set(null);
				return;
			}
			if (old != null) {
				old.getStyleClass().remove("question-selected");
			}
			newSelected.getStyleClass().add("question-selected");
			super.set(newSelected);
		}
	};

	public PreparationLibraryItem(PreparationLibrary library, PreparationDetail preparationDetail) {
		loadFXML();
		initialize(library, preparationDetail);
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/preparation/LibraryItem.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			loader.load();
			log.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
	}

	public void initialize(PreparationLibrary library, PreparationDetail preparationDetail) {
		this.library = library;
		this.preparationDetail = preparationDetail;
		this.name.setTitle(library.getName());
		loadQuestions();
	}

	@FXML
	private void add() {
		Dialog<List<Integer>> dialog = new Dialog<>();
		dialog.setDialogPane(new LibrarySelectDialog());
		dialog.setTitle(MsgUtil.getMessage("preparation.question.library"));
		dialog.setPrefSize(1000, 600);
		dialog.showAndWait().ifPresent(questionIds -> {
			if (questionIds == null) {
				return;
			}
			List<Integer> ids = JSON.parseArray(library.getQuestionIds(), Integer.class);
			if (ids != null) {
				ids.addAll(questionIds);
			} else {
				ids = questionIds;
			}
			library.setQuestionIds(JSON.toJSONString(ids));
			updateQuestions();
		});
	}

	@FXML
	private void publish() {
		// 判断当前是否有考核正在进行
		if (Session.get(Session.KEY_LIBRARY_PUBLISH_ID) != null || Session.get(Session.KEY_PREPARATION_PUBLISH_ID) != null) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.examing"));
			return;
		}
		if (questions.getChildren().size() == 0) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.no.preparation.questions"));
			return;
		}
		// 创建考核记录
		List<Class> classes = SpringUtil.getBean(ClassAction.class).findClassesByTeacherId(Session.get(Session.KEY_LOGIN_ID));

		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new ClassSelectDialog(classes));
		dialog.setTitle(MsgUtil.getMessage("class.dialog.select"));
		dialog.setPrefSize(652, 420);
		dialog.showAndWait().ifPresent(classId -> {
			try {
				Integer publishId = SpringUtil.getBean(PreparationPublishAction.class).publishPreparationLibrary(library.getId(), classId);
				// 记录当前考核发布编号
				Session.set(Session.KEY_PREPARATION_PUBLISH_ID, publishId);
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
	}

	@FXML
	private void delete() {
		AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), e -> {
			if (e == ButtonType.YES) {
				SpringUtil.getBean(PreparationLibraryAction.class).deletePreparationLibrary(library);
				preparationDetail.loadQuizs();
			}
		});
	}

	private void updateQuestions() {
		try {
			SpringUtil.getBean(PreparationLibraryAction.class).updatePreparationLibrary(library);
			loadQuestions();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	private void loadQuestions() {
		this.questions.getChildren().clear();
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByQuestionIds(library.getQuestionIds());
		for (int i = 0; i < questions.size(); i++) {
			int index = i + 1;
			Question question = questions.get(i);
			PreparationQuestionItem item = new PreparationQuestionItem(index, QuestionType.getQuestionType(question.getType()), question, id -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), e -> {
					if (e == ButtonType.YES) {
						List<Integer> ids = JSON.parseArray(library.getQuestionIds(), Integer.class);
						ids.remove(id);
						library.setQuestionIds(JSON.toJSONString(ids));
						updateQuestions();
					}
				});
			});
			item.setOnMouseClicked(e -> {
				selected.set(item);
			});
			this.questions.getChildren().add(item);
		}
	}

	public final PreparationQuestionItem getSelectedPreparationQuestionItem() {
		return selected.get();
	}

	public final ReadOnlyObjectProperty<PreparationQuestionItem> selectedPreparationQuestionItemProperty() {
		return selected.getReadOnlyProperty();
	}

	public final void selectPreparationQuestionItem(PreparationQuestionItem value) {
		selected.set(value);
	}

	public final void clearSelectedPreparationQuestionItem() {
		if (selected.get() == null) {
			return;
		}
		selected.get().getStyleClass().remove("question-selected");
		selected.set(null);
	}
}
