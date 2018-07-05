package com.cas.sim.tis.view.control.imp.preparation;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.LibraryAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.consts.LibraryType;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.SearchBox;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.question.PreviewQuestionItem;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LibrarySelectDialog extends DialogPane<List<Integer>> {

	private ToggleGroup group = new ToggleGroup();
	private SearchBox search;
	private Table table;

	private VBox libraryPane;
	private VBox questionPane;
	private Title title;
	private VBox quetionList;

	private List<Integer> questionIds = new ArrayList<>();

	public LibrarySelectDialog() {
		createLibraryPane();
		createQuestionPane();
		getChildren().add(libraryPane);
	}

	private void createLibraryPane() {
		libraryPane = new VBox(10);
		VBox.setVgrow(libraryPane, Priority.ALWAYS);
		libraryPane.setAlignment(Pos.TOP_CENTER);
		libraryPane.setPadding(new Insets(20));

		HBox toggleBox = new HBox(10);
		for (LibraryType libraryType : LibraryType.values()) {
			ToggleButton toggle = new ToggleButton(libraryType.getKey());
			toggle.setMinSize(100, 40);
			toggle.setStyle("-fx-font-size:14px");
			toggle.setUserData(libraryType.getType());
			toggleBox.getChildren().add(toggle);
			group.getToggles().add(toggle);
		}
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				group.selectToggle(o);
			} else {
				reload();
			}
		});
		search = new SearchBox();
		search.setOnSearch(event -> {
			reload();
		});
		HBox searchBox = new HBox();
		searchBox.getChildren().add(search);
		searchBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(searchBox, Priority.ALWAYS);

		toggleBox.getChildren().add(searchBox);

		table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setSeparatorable(false);
		table.setRowsSpacing(1);
		// 数据库唯一表示
		Column<Integer> primary = new Column<>();
		primary.setPrimary(true);
		primary.setVisible(false);
		primary.setKey("id");
		// 题库名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("library.name"));
		name.setPrefWidth(250);
		// 查看按钮
		Column<String> view = new Column<String>();
		view.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.view"), Priority.ALWAYS, "blue-btn", id -> {
			getChildren().remove(libraryPane);
			getChildren().add(questionPane);
			loadQuestions((Integer) id);
		}));
		view.setAlignment(Pos.CENTER_RIGHT);
		table.getColumns().addAll(primary, name, view);

		ScrollPane scroll = new ScrollPane(table);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		VBox.setVgrow(scroll, Priority.ALWAYS);

		libraryPane.getChildren().addAll(toggleBox, scroll);

		group.selectToggle(group.getToggles().get(0));
	}

	private void createQuestionPane() {
		questionPane = new VBox(10);
		VBox.setVgrow(questionPane, Priority.ALWAYS);
		questionPane.setAlignment(Pos.TOP_CENTER);
		questionPane.setPadding(new Insets(20, 20, 20, 0));

		HBox box = new HBox(10);
		title = new Title();

		HBox btn = new HBox();
		btn.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(btn, Priority.ALWAYS);
		
		Button button = new Button(MsgUtil.getMessage("button.back"));
		button.getStyleClass().add("blue-btn");
		button.setOnAction(e -> {
			getChildren().add(libraryPane);
			getChildren().remove(questionPane);
			questionIds.clear();
		});
		btn.getChildren().add(button);
		
		box.getChildren().addAll(title, btn);

		quetionList = new VBox(10);
		quetionList.setPadding(new Insets(10));

		ScrollPane scroll = new ScrollPane(quetionList);
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
			if (questionIds.size() == 0) {
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("question.stem")));
				return;
			}
			dialog.setResult(questionIds);
		});

		questionPane.getChildren().addAll(box, scroll, error, ok);
	}

	private void loadQuestions(Integer rid) {
		Library lib = SpringUtil.getBean(LibraryAction.class).findLibraryById(rid);
		title.setTitle(lib.getName());

		this.quetionList.getChildren().clear();

		List<Question> choices = loadQuestionsByType(rid, QuestionType.CHOICE);
		List<Question> judgments = loadQuestionsByType(rid, QuestionType.JUDGMENT);
		List<Question> blanks = loadQuestionsByType(rid, QuestionType.BLANK);
		List<Question> subjectives = loadQuestionsByType(rid, QuestionType.SUBJECTIVE);
		List<Question> questions = new ArrayList<>();
		questions.addAll(choices);
		questions.addAll(judgments);
		questions.addAll(blanks);
		questions.addAll(subjectives);
		for (int i = 0; i < questions.size(); i++) {
			int index = i + 1;
			Question question = questions.get(i);
			PreviewQuestionItem item = new PreviewQuestionItem(index, QuestionType.getQuestionType(question.getType()), question, true);
			item.setOnMouseClicked(e -> {
				Integer id = question.getId();
				if (questionIds.contains(id)) {
					questionIds.remove(id);
					item.getStyleClass().remove("question-selected");
				} else {
					questionIds.add(id);
					item.getStyleClass().add("question-selected");
				}
			});
			quetionList.getChildren().add(item);
		}
	}

	private List<Question> loadQuestionsByType(Integer rid, QuestionType type) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, type.getType());
		if (questions.size() == 0) {
			return new ArrayList<>();
		}
		return questions;
	}

	private void reload() {
		List<Library> libraries = SpringUtil.getBean(LibraryAction.class).findLibraryByType((int) group.getSelectedToggle().getUserData(), search.getText());
		JSONArray array = new JSONArray();
		array.addAll(libraries);
		table.setItems(array);
		table.build();
	}
}
