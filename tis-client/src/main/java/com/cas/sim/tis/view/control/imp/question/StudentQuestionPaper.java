package com.cas.sim.tis.view.control.imp.question;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.action.LibraryAnswerAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.consts.PublishType;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.vo.ExamLibraryPublish;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
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

	private static final Logger LOG = LoggerFactory.getLogger(StudentQuestionPaper.class);

	@FXML
	private Title title;
	@FXML
	private Label libName;
	@FXML
	private VBox paper;
	@FXML
	private Text analysis;
	@FXML
	private ToggleGroup filter;

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
			LOG.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
	}

	/**
	 * 界面初始化
	 */
	private void initialize() {
		this.title.setTitle(type.getDetailTitle());

		ExamLibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(pid);
		this.libName.setText(publish.getLibrary().getName());

		filter.selectedToggleProperty().addListener((b, o, n) -> {
			loadQuestions();
		});
		loadQuestions();
	}

	private void loadQuestions() {
		this.paper.getChildren().clear();
		boolean onlyWrong = filter.getSelectedToggle() != null;
		List<ExamLibraryAnswer> answers = SpringUtil.getBean(LibraryAnswerAction.class).findAnswersByPublish(pid, LibraryRecordType.LIBRARY.getType(), onlyWrong);
		for (int i = 0; i < answers.size(); i++) {
			int index = i + 1;
			ExamLibraryAnswer answer = answers.get(i);
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

	@Override
	public void onContentAttached(PageController pageController) {
//		FIXME
//		pageController.setModuleName("main.menu.preparation");
	}

}
