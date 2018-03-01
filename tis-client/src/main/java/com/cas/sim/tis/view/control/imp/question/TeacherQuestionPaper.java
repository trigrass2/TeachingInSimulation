package com.cas.sim.tis.view.control.imp.question;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.cas.sim.tis.action.LibraryAnswerAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.util.MathUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 教师查看考核试卷详情
 * @功能 RecordQuestionPaper.java
 * @作者 caowj
 * @创建日期 2018年2月27日
 * @修改人 caowj
 */
public class TeacherQuestionPaper extends HBox implements IContent {

	@FXML
	private Title title;
	@FXML
	private Label libName;
	@FXML
	private VBox paper;
	@FXML
	private PieChart chart;
	@FXML
	private Label tip;
	@FXML
	private Label rate;
	@FXML
	private Text analysis;
	@FXML
	private ToggleGroup order;

	private int pid;
	private PreviewQuestionItem currentItem;

	/**
	 * @param pid 考核、练习发起编号
	 */
	public TeacherQuestionPaper(int pid) {
		this.pid = pid;
		loadFXML();
		initialize();
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/record/PaperForTeacher.fxml");
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
		LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(pid);
		this.libName.setText(publish.getLibrary().getName());

		chart.setOnMouseMoved(e -> {
			tip.setTranslateX(e.getX());
			tip.setTranslateY(e.getY());
		});
		
		order.selectedToggleProperty().addListener((observe, oldVal, newVal) -> {
			if (newVal == null) {
				order.selectToggle(oldVal);
				return;
			}
			loadQuestions();
		});
		order.selectToggle(order.getToggles().get(0));
	}

	private void loadQuestions() {
		this.paper.getChildren().clear();

		boolean mostWrong = false;
		Toggle toggle = order.getSelectedToggle();
		if (toggle != null) {
			mostWrong = Boolean.valueOf((String) order.getSelectedToggle().getUserData());
		}
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByPublish(pid, mostWrong);
		for (int i = 0; i < questions.size(); i++) {
			int index = i + 1;
			Question question = questions.get(i);
			PreviewQuestionItem item = new PreviewQuestionItem(index, QuestionType.getQuestionType(question.getType()), question, true);
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
	private void selectQuestion(Question question, PreviewQuestionItem item) {
		if (currentItem != null) {
			currentItem.getStyleClass().remove("question-selected");
		}
		analysis.setText(question.getAnalysis());
		loadChart(question.getId());
		currentItem = item;
		currentItem.getStyleClass().add("question-selected");
	}

	private void loadChart(int qid) {
		Map<AnswerState, Integer> statistics = SpringUtil.getBean(LibraryAnswerAction.class).statisticsByQuestionId(pid, qid);

		Integer undo = statistics.get(AnswerState.ANSWER_STATE_UNDO);
		Integer wrong = statistics.get(AnswerState.ANSWER_STATE_WRONG);
		Integer corrected = statistics.get(AnswerState.ANSWER_STATE_CORRECTED);
		Integer right = statistics.get(AnswerState.ANSWER_STATE_RIGHT);
		int total = undo + wrong + corrected + right;

		ObservableList<Data> datas = FXCollections.observableArrayList(//
				new PieChart.Data(MsgUtil.getMessage("answer.undo"), undo), //
				new PieChart.Data(MsgUtil.getMessage("answer.wrong"), wrong + corrected), //
				new PieChart.Data(MsgUtil.getMessage("answer.right"), right)//
		);
		chart.setData(datas);
		for (final PieChart.Data data : chart.getData()) {
			Node node = data.getNode();
			node.setOnMouseEntered(e -> {
				tip.setText(data.getName() + ":" + (int) data.getPieValue());
				tip.setVisible(true);
			});
			node.setOnMouseExited(e -> {
				tip.setVisible(false);

			});
		}
		if (total == 0) {
			rate.setText(MsgUtil.getMessage("answer.rate", 0));
		} else {
			rate.setText(MsgUtil.getMessage("answer.rate", MathUtil.round(2, right * 100f / total)));
		}
	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		return new Region[] { this };
	}

}
