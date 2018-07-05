package com.cas.sim.tis.view.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.GoalAction;
import com.cas.sim.tis.action.LibraryRecordAction;
import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.consts.GoalType;
import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.entity.PreparationLibrary;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.HomeView;
import com.cas.sim.tis.view.control.imp.Decoration;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.exam.BlankOption;
import com.cas.sim.tis.view.control.imp.exam.ChoiceOption;
import com.cas.sim.tis.view.control.imp.exam.IOption;
import com.cas.sim.tis.view.control.imp.exam.JudgmentOption;
import com.cas.sim.tis.view.control.imp.exam.SubjectiveOption;
import com.cas.util.MathUtil;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

@FXMLController
public class PreparationExamController {
	@FXML
	private Decoration decoration;
	@FXML
	private Title libraryName;
	@FXML
	private ScrollPane options;
	@FXML
	private Label total;
	@FXML
	private Label score;
	@FXML
	private Label rate;
	@FXML
	private FlowPane flow;
	@FXML
	private VBox a;
	@FXML
	private VBox s;
	@FXML
	private VBox k;
	@FXML
	private Button prev;
	@FXML
	private Button next;
	@FXML
	private Button back;
	@FXML
	private Button submit;
	@FXML
	private Button askResult;

	private Map<Integer, CheckBox> checkBoxs = new HashMap<>();

	private ToggleGroup group = new ToggleGroup();

	private PreparationPublish publish;

	private PreparationLibrary library;

	private IOption current;

	private int currIndex;

	/**
	 * 回答结果<br>
	 * Key:题号<br>
	 * Value:答题结果<br>
	 */
	private Map<Integer, LibraryAnswer> answers = new HashMap<>();

	private boolean submited;
	private ChangeListener<Toggle> groupListener;

	public void initialize(PreparationPublish publish, List<Question> questions) {
		clear();

		this.publish = publish;

		this.library = publish.getLibrary();
		this.libraryName.setTitle(library.getName());

//		// XXX 暂时不做：顺序打乱
//		Collections.shuffle(questions);
		float total = 0f;
		for (int i = 0; i < questions.size(); i++) {
			ToggleButton toggle = new ToggleButton(String.valueOf(i + 1));
			toggle.getStyleClass().add("undo");
			toggle.setUserData(i);
			toggle.setWrapText(false);
			flow.getChildren().add(toggle);
			group.getToggles().add(toggle);

			Question question = questions.get(i);
			LibraryAnswer libraryAnswer = new LibraryAnswer();
			libraryAnswer.setIndex(i);
			libraryAnswer.setQuestion(question);
			libraryAnswer.setQuestionId(question.getId());
			this.answers.put(i, libraryAnswer);

			total += question.getPoint();
		}
		this.total.setText(String.valueOf(total));

		groupListener = (b, o, n) -> {
			if (o == null) {
				return;
			} else if (n == null) {
				this.group.selectToggle(o);
				return;
			}
			if (!submited) {
				// 验证上一个试题是否作答完成
				checkAnswer((ToggleButton) o);
			}
			// 加载下一个试题
			currIndex = (int) n.getUserData();
			prev.setDisable(false);
			next.setDisable(false);
			if (currIndex <= 0) {
				prev.setDisable(true);
			}
			if (currIndex >= questions.size() - 1) {
				next.setDisable(true);
			}
			loadQuestion();
		};
		group.selectedToggleProperty().addListener(groupListener);
		group.selectToggle(group.getToggles().get(0));
		loadQuestion();
		loadASK();
	}

	/**
	 * @param o
	 */
	private void checkAnswer(ToggleButton button) {
		if (current == null) {
			return;
		}
		LibraryAnswer answer = current.getAnswer();
		if (answer == null) {
			return;
		}
		String answerStr = answer.getAnswer();
		if (StringUtils.isEmpty(answerStr)) {
			answer.setCorrected(AnswerState.ANSWER_STATE_UNDO.getType());
			if (button.getStyleClass().contains("undo")) {
				return;
			} else {
				button.getStyleClass().remove("done");
				button.getStyleClass().add("undo");
			}
		} else if (StringUtils.isEmpty(answerStr.replaceAll("\\|", ""))) {
			answer.setCorrected(AnswerState.ANSWER_STATE_UNDO.getType());
			if (button.getStyleClass().contains("undo")) {
				return;
			} else {
				button.getStyleClass().remove("done");
				button.getStyleClass().add("undo");
			}
		} else {
			answers.put((Integer) button.getUserData(), answer);
			if (button.getStyleClass().contains("done")) {
				return;
			} else {
				button.getStyleClass().add("done");
				button.getStyleClass().remove("undo");
			}
		}
	}

	/**
	 * 加载题目
	 * @param index 打乱以后的序号
	 * @param question
	 */
	private void loadQuestion() {
		// 获得答题结果对象
		LibraryAnswer libraryAnswer = answers.get(currIndex);
//		// 加载题目
//		this.question.setText(question.getTitle());
		// 根据题目类型处理
		int type = libraryAnswer.getQuestion().getType();
		switch (QuestionType.getQuestionType(type)) {
		case CHOICE:
			ChoiceOption choice = new ChoiceOption(currIndex + 1, libraryAnswer, submited);
			this.options.setContent(choice);
			this.current = choice;
			break;
		case JUDGMENT:
			JudgmentOption judgment = new JudgmentOption(currIndex + 1, libraryAnswer, submited);
			this.options.setContent(judgment);
			this.current = judgment;
			break;
		case BLANK:
			BlankOption blank = new BlankOption(currIndex + 1, libraryAnswer, submited);
			this.options.setContent(blank);
			this.current = blank;
			break;
		case SUBJECTIVE:
			SubjectiveOption subjective = new SubjectiveOption(currIndex + 1, libraryAnswer, submited);
			this.options.setContent(subjective);
			this.current = subjective;
			break;
		default:
			break;
		}
	}

	@FXML
	private void prev(ActionEvent event) {
		if (currIndex - 1 <= 0) {
			currIndex = 0;
		} else {
			currIndex--;
		}
		group.selectToggle(group.getToggles().get(currIndex));
	}

	@FXML
	private void next(ActionEvent event) {
		if (currIndex + 1 >= answers.size() - 1) {
			currIndex = answers.size() - 1;
		} else {
			currIndex++;
		}
		group.selectToggle(group.getToggles().get(currIndex));
	}

	@FXML
	private void back() {
		// 考试结束返回首页
		Application.showView(HomeView.class);
	}

	/**
	 * 
	 */
	private void clear() {
		this.submited = false;
		this.current = null;
		this.currIndex = 0;

		this.answers.clear();
		this.group.getToggles().clear();
		this.flow.getChildren().clear();
		if (groupListener != null) {
			this.group.selectedToggleProperty().removeListener(groupListener);
		}

		this.submit.setDisable(false);
		this.back.setVisible(false);
		this.prev.setDisable(true);
		this.next.setDisable(false);

		this.score.setText("--");
		this.rate.setText("--");

		this.a.getChildren().clear();
		this.s.getChildren().clear();
		this.k.getChildren().clear();
		this.checkBoxs.clear();
	}

	@FXML
	private void submit() {
		submit(false);
	}

	public void submit(boolean passive) {
		if (submited) {
			return;
		}
		submit.setDisable(true);
		if (passive) {
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("exam.over"));
			finish();
		} else {
			AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.submit"), resp -> {
				if (resp == ButtonType.NO) {
					submit.setDisable(false);
					return;
				}
				finish();
			});
		}
	}

	public void finish() {
		// 提交前更新一下当前显示试题的答案
		checkAnswer((ToggleButton) group.getSelectedToggle());
		// 计算得分
		float score = 0;
		for (int i = 0; i < answers.size(); i++) {
			LibraryAnswer libraryAnswer = answers.get(i);
			Question question = libraryAnswer.getQuestion();
			int type = question.getType();
			if (QuestionType.SUBJECTIVE.getType() == type) {
				continue;
			}
			ToggleButton button = (ToggleButton) group.getToggles().get(i);
			button.getStyleClass().remove("undo");
			button.getStyleClass().remove("done");
			score += libraryAnswer.getScore();
			if (AnswerState.ANSWER_STATE_RIGHT.getType() == libraryAnswer.getCorrected()) {
				button.getStyleClass().add("right");
			} else {
				button.getStyleClass().add("wrong");
			}
		}
		this.score.setText(String.valueOf(score));

		int pid = publish.getId();

		LibraryRecord record = new LibraryRecord();
		record.setScore(score);
		record.setPublishId(pid);
		record.setCreator(Session.get(Session.KEY_LOGIN_ID));
		record.setType(LibraryRecordType.PREPARATION.getType());

		List<LibraryAnswer> libraryAnswers = new ArrayList<>(this.answers.values());

		SpringUtil.getBean(LibraryRecordAction.class).addRecord(record, libraryAnswers);

		back.setVisible(true);
		submited = true;
		group.selectToggle(group.getToggles().get(0));

		showASKDetail();
	}

	public PreparationPublish getPreparationPublish() {
		return publish;
	}

	public void refresh() {
		if (decoration != null) {
			decoration.maximize();
		}
	}

	private void loadASK() {
		PreparationLibrary library = publish.getLibrary();
		List<Goal> goals = SpringUtil.getBean(GoalAction.class).findGoalByPreparationLibraryIdAndQuestionIds(library.getId(), library.getQuestionIds());
		for (Goal goal : goals) {
			CheckBox checkBox = new CheckBox(goal.getName());
			checkBox.getStyleClass().add("ask-check-box");
			checkBox.setDisable(true);
			int type = goal.getType();
			if (GoalType.ATTITUDE.getType() == type) {
				a.getChildren().add(checkBox);
			} else if (GoalType.SKILL.getType() == type) {
				s.getChildren().add(checkBox);
			} else if (GoalType.KNOWLEDGE.getType() == type) {
				k.getChildren().add(checkBox);
			}
			checkBoxs.put(goal.getId(), checkBox);
		}
	}

	private void showASKDetail() {
		List<Integer> uncorrectQuestionIds = new ArrayList<>();
		List<LibraryAnswer> libraryAnswers = new ArrayList<>(this.answers.values());
		for (LibraryAnswer libraryAnswer : libraryAnswers) {
			if (libraryAnswer.getCorrected() == AnswerState.ANSWER_STATE_RIGHT.getType()) {
				continue;
			}
			uncorrectQuestionIds.add(libraryAnswer.getQuestionId());
		}
		List<Goal> uncorrectGoals = new ArrayList<>();
		if (uncorrectQuestionIds.size() != 0) {
			uncorrectGoals = SpringUtil.getBean(GoalAction.class).findGoalByPreparationLibraryIdAndQuestionIds(publish.getLibrary().getId(), JSON.toJSONString(uncorrectQuestionIds));
		}
		for (CheckBox checkBox : checkBoxs.values()) {
			checkBox.setSelected(true);
		}
		for (Goal uncorrectGoal : uncorrectGoals) {
			if (checkBoxs.containsKey(uncorrectGoal.getId())) {
				CheckBox checkBox = checkBoxs.get(uncorrectGoal.getId());
				checkBox.setSelected(false);
			}
		}
		int total = checkBoxs.size();
		int correct = total - uncorrectGoals.size();
		rate.setText(MathUtil.round(2, correct * 100f / total) + "%");
	}
}
