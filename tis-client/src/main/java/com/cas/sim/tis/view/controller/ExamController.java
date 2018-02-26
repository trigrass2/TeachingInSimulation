package com.cas.sim.tis.view.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.HomeView;
import com.cas.sim.tis.view.action.LibraryRecordAction;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.exam.BlankOption;
import com.cas.sim.tis.view.control.imp.exam.ChoiceOption;
import com.cas.sim.tis.view.control.imp.exam.IOption;
import com.cas.sim.tis.view.control.imp.exam.JudgmentOption;
import com.cas.sim.tis.view.control.imp.exam.SubjectiveOption;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

@FXMLController
public class ExamController {

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

	private ToggleGroup group = new ToggleGroup();

	private LibraryPublish publish;

	private Library library;

	private IOption current;

	private int currIndex;

	/**
	 * 回答结果<br>
	 * Key:题号<br>
	 * Value:答题结果<br>
	 */
	private Map<Integer, LibraryAnswer> answers = new HashMap<>();

	private Timeline timeline;
	private int cost;

	private boolean submited;

	public void initialize(LibraryPublish publish) {
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
			LibraryAnswer libraryAnswer = new LibraryAnswer();
			libraryAnswer.setIndex(i);
			libraryAnswer.setQuestion(question);
			libraryAnswer.setQuestionId(question.getId());
			this.answers.put(i, libraryAnswer);

			total += question.getPoint();
		}
		this.total.setText(String.valueOf(total));

		group.selectToggle(group.getToggles().get(0));
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (o == null || n == null || o == n) {
				return;
			}
			// 验证上一个试题是否作答完成
			LibraryAnswer answer = current.getAnswer();
			if (answer != null && !StringUtils.isEmpty(answer.getAnswer())) {
				answers.put((Integer) o.getUserData(), answer);
				((ToggleButton) o).getStyleClass().remove("undo");
				((ToggleButton) o).getStyleClass().add("done");
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
		});
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

	@FXML
	private void submit(ActionEvent event) {
		((Button) event.getSource()).setDisable(true);
		Alert alert = new Alert(AlertType.CONFIRMATION, MsgUtil.getMessage("alert.confirmation.submit"), ButtonType.YES, ButtonType.NO);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(resp -> {
			if (resp == ButtonType.NO) {
				((Button)event.getSource()).setDisable(false);
				return;
			}
			// 计时暂停
			timeline.stop();
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
				if (libraryAnswer.getCorrected()) {
					libraryAnswer.setCorrected(true);
					button.getStyleClass().add("right");
				} else {
					button.getStyleClass().add("wrong");
				}
			}
			this.score.setText(String.valueOf(score));

			int pid = publish.getId();

			LibraryRecord record = new LibraryRecord();
			record.setCost(cost);
			record.setScore(score);
			record.setPublishId(pid);
			record.setCreator(Session.get(Session.KEY_LOGIN_ID));

			List<LibraryAnswer> libraryAnswers = new ArrayList<>(this.answers.values());

			SpringUtil.getBean(LibraryRecordAction.class).addRecord(record, libraryAnswers);

			back.setVisible(true);
			submited = true;
		});
	}
}
