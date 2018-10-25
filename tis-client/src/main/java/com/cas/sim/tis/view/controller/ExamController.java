package com.cas.sim.tis.view.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.LibraryRecordAction;
import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.entity.ExamLibraryRecord;
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
import com.cas.sim.tis.vo.ExamLibraryPublish;

import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

@FXMLController
public class ExamController {
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
	private Label minute;
	@FXML
	private Label second;
	@FXML
	private FlowPane flow;
	@FXML
	private Button prev;
	@FXML
	private Button next;
	@FXML
	private Button back;
	@FXML
	private Button submit;

	private ToggleGroup group = new ToggleGroup();

	private ExamLibraryPublish publish;

	private Library library;

	private IOption current;

	private int currIndex;

	/**
	 * 回答结果<br>
	 * Key:题号<br>
	 * Value:答题结果<br>
	 */
	private Map<Integer, ExamLibraryAnswer> answers = new HashMap<>();

	private Timeline timeline;

	private int cost;

	private boolean submited;
	private ChangeListener<Toggle> groupListener;

	public void initialize(ExamLibraryPublish publish) {
		clear();

		this.publish = publish;

		this.library = publish.getLibrary();
		this.libraryName.setTitle(library.getName());

		// 创建答题卡项
		List<Question> questions = this.library.getQuestions();
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
			ExamLibraryAnswer libraryAnswer = new ExamLibraryAnswer();
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

		// 启动计时器
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), (ActionEvent event1) -> {
			cost++;
			this.minute.setText(String.valueOf(cost / 60));
			this.second.setText(String.valueOf(cost % 60));
		}));
		timeline.play();
	}

	/**
	 * @param o
	 */
	private void checkAnswer(ToggleButton button) {
		if (current == null) {
			return;
		}
		ExamLibraryAnswer answer = current.getAnswer();
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
		ExamLibraryAnswer libraryAnswer = answers.get(currIndex);
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
		timeline.stop();
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
		this.cost = 0;

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
		// 计时暂停
		timeline.stop();
		// 计算得分
		float score = 0;
		for (int i = 0; i < answers.size(); i++) {
			ExamLibraryAnswer libraryAnswer = answers.get(i);
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

		ExamLibraryRecord record = new ExamLibraryRecord();
		record.setCost(cost);
		record.setScore(score);
		record.setPublishId(pid);
		record.setCreator(Session.get(Session.KEY_LOGIN_ID));
		record.setType(LibraryRecordType.LIBRARY.getType());

		List<ExamLibraryAnswer> libraryAnswers = new ArrayList<>(this.answers.values());

		SpringUtil.getBean(LibraryRecordAction.class).addRecord(record, libraryAnswers);

		back.setVisible(true);
		submited = true;
		group.selectToggle(group.getToggles().get(0));
		
		Session.set(Session.KEY_LIBRARY_PUBLISH_ID, null);
	}

	public ExamLibraryPublish getLibraryPublish() {
		return publish;
	}

	public void refresh() {
		if (decoration != null) {
			decoration.maximize();
		}
	}
}
