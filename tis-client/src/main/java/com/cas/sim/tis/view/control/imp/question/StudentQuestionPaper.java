package com.cas.sim.tis.view.control.imp.question;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.cas.sim.tis.action.LibraryAnswerAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.consts.PublishType;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 考核、练习试卷详情
 * @功能 RecordQuestionPaper.java
 * @作者 caowj
 * @创建日期 2018年2月27日
 * @修改人 caowj
 */
public class StudentQuestionPaper extends HBox implements IContent {

	@FXML
	private Title title;
	@FXML
	private Label libName;
	@FXML
	private VBox paper;
	@FXML
	private Text analysis;

	private int pid;
	private PublishType type;
	private RecordQuestionItem currentItem;

	/**
	 * @param pid 考核、练习发起编号
	 */
	public StudentQuestionPaper(int pid, PublishType type) {
		this.pid = pid;
		this.type = type;
		loadFXML();
		initialize();
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/record/PaperForStudent.fxml");
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

	/**
	 * 界面初始化
	 */
	private void initialize() {
		this.title.setTitle(type.getDetailTitle());

		LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(pid);
		this.libName.setText(publish.getLibrary().getName());

		loadQuestions();
	}

	private void loadQuestions() {
		this.paper.getChildren().clear();
		List<LibraryAnswer> answers = SpringUtil.getBean(LibraryAnswerAction.class).findAnswersByPublish(pid);
		for (int i = 0; i < answers.size(); i++) {
			int index = i + 1;
			LibraryAnswer answer = answers.get(i);
			Question question = answer.getQuestion();
			RecordQuestionItem item = new RecordQuestionItem(index, answer);
			item.setOnMouseClicked(e -> {
				selectQuestion(question, item);
			});
			paper.getChildren().add(item);
			if (i == 0) {
				selectQuestion(question, item);
			}
		}
	}

	/**
	 * @param question
	 * @param item
	 */
	private void selectQuestion(Question question, RecordQuestionItem item) {
		if (currentItem != null) {
			currentItem.getStyleClass().remove("question-selected");
		}
		analysis.setText(question.getAnalysis());
		currentItem = item;
		currentItem.getStyleClass().add("question-selected");
	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		return new Region[] { this };
	}

}
