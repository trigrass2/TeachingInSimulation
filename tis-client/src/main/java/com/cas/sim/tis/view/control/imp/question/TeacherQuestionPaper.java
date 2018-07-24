package com.cas.sim.tis.view.control.imp.question;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.action.GoalAction;
import com.cas.sim.tis.action.LibraryAnswerAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.action.PreparationPublishAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.consts.GoalType;
import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.PreparationPublish;
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
import javafx.scene.control.CheckBox;
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

	private static final Logger LOG = LoggerFactory.getLogger(TeacherQuestionPaper.class);

	@FXML
	private Title title;
	@FXML
	private Label libName;
	@FXML
	private VBox paper;
	@FXML
	private VBox ask;
	@FXML
	private VBox a;
	@FXML
	private VBox s;
	@FXML
	private VBox k;
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

	private int publishId;
	private Integer preparationLibraryId;
	private boolean showASK;
	private LibraryRecordType type;
	private PreviewQuestionItem currentItem;


	public TeacherQuestionPaper(Integer publishId) {
		this(publishId, null, LibraryRecordType.LIBRARY);
	}
	
	/**
	 * @param publishId 考核、练习发起编号
	 * @param showASK 是否显示试题对应ASK，在备课试题测试中用到
	 */
	public TeacherQuestionPaper(Integer publishId,Integer preparationLibraryId, LibraryRecordType type) {
		this.publishId = publishId;
		this.preparationLibraryId = preparationLibraryId;
		this.type = type;
		this.showASK = LibraryRecordType.PREPARATION == type;
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
		if (LibraryRecordType.LIBRARY == type) {
			LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(publishId);
			this.libName.setText(publish.getLibrary().getName());
		}else {
			PreparationPublish publish = SpringUtil.getBean(PreparationPublishAction.class).findPublishById(publishId);
			this.libName.setText(publish.getLibrary().getName());
		}

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
		ask.setVisible(showASK);
	}

	private void loadQuestions() {
		this.paper.getChildren().clear();

		boolean mostWrong = false;
		Toggle toggle = order.getSelectedToggle();
		if (toggle != null) {
			mostWrong = Boolean.valueOf((String) order.getSelectedToggle().getUserData());
		}
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByPublishId(publishId, type.getType(), mostWrong);
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
		int qid = question.getId();
		loadChart(qid);
		if (showASK) {
			loadASK(qid);
		}
		currentItem = item;
		currentItem.getStyleClass().add("question-selected");
	}

	private void loadChart(int qid) {
		Map<AnswerState, Integer> statistics = SpringUtil.getBean(LibraryAnswerAction.class).statisticsByQuestionId(publishId, type.getType(), qid);

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

	private void loadASK(int qid) {
		a.getChildren().clear();
		s.getChildren().clear();
		k.getChildren().clear();
		a.getChildren().add(new Label("职业素质目标（A）"));
		s.getChildren().add(new Label("技能目标（S）"));
		k.getChildren().add(new Label("知识目标（K）"));
		List<Goal> goals = SpringUtil.getBean(GoalAction.class).findGoalByPreparationLibraryIdAndQuestionIds(preparationLibraryId, "[" + qid + "]");
		for (Goal goal : goals) {
			CheckBox checkBox = new CheckBox(goal.getName());
			checkBox.getStyleClass().add("ask-check-box");
			checkBox.setSelected(true);
			checkBox.setDisable(true);
			int type = goal.getType();
			if (GoalType.ATTITUDE.getType() == type) {
				a.getChildren().add(checkBox);
			} else if (GoalType.SKILL.getType() == type) {
				s.getChildren().add(checkBox);
			} else if (GoalType.KNOWLEDGE.getType() == type) {
				k.getChildren().add(checkBox);
			}
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
