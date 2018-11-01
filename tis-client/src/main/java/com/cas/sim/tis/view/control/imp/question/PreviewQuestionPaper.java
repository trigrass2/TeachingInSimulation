package com.cas.sim.tis.view.control.imp.question;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.ClassAction;
import com.cas.sim.tis.action.LibraryAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.consts.TemplateConsts;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.ExamView;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.IPublish;
import com.cas.sim.tis.view.control.imp.classes.ClassSelectDialog;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.controller.ExamController;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.vo.ExamLibraryPublish;
import com.cas.util.FileUtil;
import com.cas.util.Util;

import de.felixroske.jfxsupport.GUIState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class PreviewQuestionPaper extends HBox implements IContent {

	private static final Logger LOG = LoggerFactory.getLogger(PreviewQuestionPaper.class);

	@FXML
	private HBox options;
	@FXML
	private HBox submits;
	@FXML
	private Button templateBtn;
	@FXML
	private Button importBtn;
	@FXML
	private Button exportBtn;
	@FXML
	private Button publishBtn;
	@FXML
	private Button practiceBtn;
//	@FXML
//	private Label libName;
	@FXML
	private VBox paper;
	@FXML
	private PieChart chart;
	@FXML
	private Label tip;

	private Integer rid;
	private boolean editable;
	private boolean readonly;
	private boolean showReference;
	private Library library;

	public PreviewQuestionPaper(Integer rid, boolean editable) {
		this(rid, editable, false);
	}

	public PreviewQuestionPaper(Integer rid, boolean editable, boolean readonly) {
		loadFXML();
		this.rid = rid;
		this.editable = editable;
		this.readonly = readonly;
		initialize();
	}

	/**
	 * 加载界面布局文件
	 */
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/question/Paper.fxml");
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
		this.library = SpringUtil.getBean(LibraryAction.class).findLibraryById(rid);
//		this.libName.setText(library.getName());

		if (readonly) {
			this.options.getChildren().removeAll(templateBtn, importBtn, exportBtn);
			this.submits.getChildren().removeAll(practiceBtn, publishBtn);
		} else {
			if (!editable) {
				this.options.getChildren().removeAll(templateBtn, importBtn, exportBtn);
			}
			int role = Session.get(Session.KEY_LOGIN_ROLE);
			if (RoleConst.ADMIN == role) {
				this.submits.getChildren().removeAll(practiceBtn, publishBtn);
				showReference = true;
			} else if (RoleConst.TEACHER == role) {
				this.submits.getChildren().removeAll(practiceBtn);
				showReference = true;
			} else if (RoleConst.STUDENT == role) {
				this.submits.getChildren().removeAll(publishBtn);
			}
		}

		chart.setOnMouseMoved(e -> {
			tip.setTranslateX(e.getX());
			tip.setTranslateY(e.getY());
		});

		checkImportOrExport();

		loadQuestions();
	}

	private void checkImportOrExport() {
		boolean export = SpringUtil.getBean(QuestionAction.class).checkImportOrExport(rid);
		if (export) {
			this.options.getChildren().remove(importBtn);
		} else {
			this.options.getChildren().remove(exportBtn);
		}
	}

	private void loadQuestions() {
		this.paper.getChildren().clear();
		List<Question> choices = loadQuestionsByType(QuestionType.CHOICE);
		List<Question> judgments = loadQuestionsByType(QuestionType.JUDGMENT);
		List<Question> blanks = loadQuestionsByType(QuestionType.BLANK);
		List<Question> subjectives = loadQuestionsByType(QuestionType.SUBJECTIVE);
		List<Question> questions = new ArrayList<>();
		questions.addAll(choices);
		questions.addAll(judgments);
		questions.addAll(blanks);
		questions.addAll(subjectives);
		for (int i = 0; i < questions.size(); i++) {
			int index = i + 1;
			Question question = questions.get(i);
			PreviewQuestionItem item = new PreviewQuestionItem(index, QuestionType.getQuestionType(question.getType()), question, showReference);
			paper.getChildren().add(item);
		}
		ObservableList<Data> datas = FXCollections.observableArrayList(//
				new PieChart.Data(MsgUtil.getMessage("question.choice"), choices.size()), //
				new PieChart.Data(MsgUtil.getMessage("question.judgment"), judgments.size()), //
				new PieChart.Data(MsgUtil.getMessage("question.blank"), blanks.size()), //
				new PieChart.Data(MsgUtil.getMessage("question.subjective"), subjectives.size())); //
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
	}

	private List<Question> loadQuestionsByType(QuestionType type) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, type.getType());
		if (questions.size() == 0) {
			return new ArrayList<>();
		}
		return questions;
	}

	@FXML
	private void template() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setInitialFileName("题库试题导入模版.xls");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls"));
		File target = chooser.showSaveDialog(GUIState.getStage());
		if (target == null) {
			return;
		}
		FileUtil.copyFile(TemplateConsts.QUESTION_TEMPLATE, target.getAbsolutePath(), true);
	}

	@FXML
	private void importExcel() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls"));
		File source = chooser.showOpenDialog(GUIState.getStage());
		if (source == null) {
			return;
		}
		List<Question> questions = new ArrayList<>();
		if (!loadChoice(source, questions) || !loadJudgment(source, questions) || !loadBlank(source, questions) || !loadSubjective(source, questions)) {
			return;
		}
		try {
			SpringUtil.getBean(QuestionAction.class).addQuestions(rid, questions);
			loadQuestions();
			this.options.getChildren().remove(importBtn);
			this.options.getChildren().add(exportBtn);
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.import.success"));
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	@FXML
	private void exportExcel() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setInitialFileName(library.getName() + ".xls");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(MsgUtil.getMessage("resource.excel"), "*.xls"));
		File target = chooser.showSaveDialog(GUIState.getStage());
		if (target == null) {
			return;
		}
		FileUtil.createFile(target.getAbsolutePath());
		Map<String, Object[][]> datas = new HashMap<>();
		exportChoice(datas);
		exportJudgment(datas);
		exportBlank(datas);
		exportSubjective(datas);
		try {
			ExcelUtil.writeExcelByTemplate(TemplateConsts.QUESTION_TEMPLATE, target.getAbsolutePath(), ExcelUtil.EXCEL_TYPE_2003, datas, 1);
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.export.success"));
		} catch (Exception e) {
			AlertUtil.showAlert(AlertType.ERROR, e.getMessage());
			LOG.error("Excel导出失败：{}", e.getMessage());
		}
	}

	@FXML
	private void publish() {
		// 判断当前是否有考核正在进行
		if (Session.get(Session.KEY_LIBRARY_PUBLISH_ID) != null || Session.get(Session.KEY_PREPARATION_PUBLISH_ID) != null || Session.get(Session.KEY_BROKEN_CASE_PUBLISH_ID) != null) {
			AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.examing"));
			return;
		}
		// 创建考核记录
		List<Class> classes = SpringUtil.getBean(ClassAction.class).findClassesByTeacherId(Session.get(Session.KEY_LOGIN_ID));

		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new ClassSelectDialog(classes));
		dialog.setTitle(MsgUtil.getMessage("class.dialog.select"));
		dialog.setPrefSize(652, 420);
		dialog.showAndWait().ifPresent(cid -> {
			if (cid == null) {
				return;
			}
			try {
				Integer publishId = SpringUtil.getBean(LibraryPublishAction.class).publishLibraryToClass(rid, cid);
				// 记录当前考核发布编号
				Session.set(Session.KEY_LIBRARY_PUBLISH_ID, publishId);
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
	private void practice() {
		int id = SpringUtil.getBean(LibraryPublishAction.class).practiceLibraryByStudent(rid);
		ExamLibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(id);

		Application.showView(ExamView.class);

		ExamController controller = SpringUtil.getBean(ExamController.class);
		controller.initialize(publish);
	}

	private void exportChoice(Map<String, Object[][]> datas) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, QuestionType.CHOICE.getType());
		Object[][] data = new Object[questions.size() + 1][4];
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			data[i + 1][0] = question.getTitle();
			data[i + 1][1] = question.getOptions().replaceAll("\\|", "\r\n");
			data[i + 1][2] = question.getReference();
			data[i + 1][3] = question.getAnalysis();
		}
		datas.put(QuestionType.CHOICE.getSheetName(), data);
	}

	private void exportJudgment(Map<String, Object[][]> datas) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, QuestionType.JUDGMENT.getType());
		Object[][] data = new Object[questions.size() + 1][3];
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			data[i + 1][0] = question.getTitle();
			data[i + 1][1] = question.getReference();
			data[i + 1][2] = question.getAnalysis();
		}
		datas.put(QuestionType.JUDGMENT.getSheetName(), data);
	}

	private void exportBlank(Map<String, Object[][]> datas) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, QuestionType.BLANK.getType());
		Object[][] data = new Object[questions.size() + 1][3];
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			data[i + 1][0] = question.getTitle().replaceAll("\\|", "");
			data[i + 1][1] = question.getReference().replaceAll("\\|", "\r\n");
			data[i + 1][2] = question.getAnalysis();
		}
		datas.put(QuestionType.BLANK.getSheetName(), data);
	}

	private void exportSubjective(Map<String, Object[][]> datas) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, QuestionType.SUBJECTIVE.getType());
		Object[][] data = new Object[questions.size() + 1][2];
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			data[i + 1][0] = question.getTitle();
			data[i + 1][1] = question.getAnalysis();
		}
		datas.put(QuestionType.SUBJECTIVE.getSheetName(), data);
	}

	private boolean loadChoice(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.CHOICE.getSheetName(), 5);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.stem"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object optionsObj = result[i][1];
			if (Util.isEmpty(optionsObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.option"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String options = String.valueOf(optionsObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (options.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.option"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object referenceObj = result[i][2];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.reference"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			String reference = String.valueOf(referenceObj).trim();
			if (reference.length() > 10) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.reference"), 10);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			Object pointObj = result[i][3];
			if (Util.isEmpty(pointObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.point"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			String point = String.valueOf(pointObj).trim();
			if (!Util.isNumeric(point)) {
				String reason = MsgUtil.getMessage("alert.warning.not.number", MsgUtil.getMessage("question.point"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			Object analysisObj = result[i][4];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 5, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setOptions(options);
			question.setAnalysis(analysis);
			question.setPoint(Float.parseFloat(point));
			question.setReference(reference);
			question.setType(QuestionType.CHOICE.getType());
			question.setCreator(Session.get(Session.KEY_LOGIN_ID));
			questions.add(question);
		}
		return true;
	}

	private boolean loadJudgment(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.JUDGMENT.getSheetName(), 4);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.stem"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object referenceObj = result[i][1];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.reference"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String reference = String.valueOf(referenceObj).trim();
			if (reference.length() > 10) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.reference"), 10);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object pointObj = result[i][2];
			if (Util.isEmpty(pointObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.point"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			String point = String.valueOf(pointObj).trim();
			if (!Util.isNumeric(point)) {
				String reason = MsgUtil.getMessage("alert.warning.not.number", MsgUtil.getMessage("question.point"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			Object analysisObj = result[i][3];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setPoint(Float.parseFloat(point));
			question.setReference(reference);
			question.setType(QuestionType.JUDGMENT.getType());
			question.setCreator(Session.get(Session.KEY_LOGIN_ID));
			questions.add(question);
		}
		return true;
	}

	private boolean loadBlank(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.BLANK.getSheetName(), 4);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.stem"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").replaceAll("（）", "（|）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object referenceObj = result[i][1];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.reference"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String reference = String.valueOf(referenceObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (reference.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.reference"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object pointObj = result[i][2];
			if (Util.isEmpty(pointObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.point"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String point = String.valueOf(pointObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (!Util.isNumeric(point)) {
				String reason = MsgUtil.getMessage("alert.warning.not.number", MsgUtil.getMessage("question.point"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object analysisObj = result[i][3];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setPoint(Float.parseFloat(point));
			question.setReference(reference);
			question.setType(QuestionType.BLANK.getType());
			question.setCreator(Session.get(Session.KEY_LOGIN_ID));
			questions.add(question);
		}
		return true;
	}

	private boolean loadSubjective(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.SUBJECTIVE.getSheetName(), 2);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.stem"));
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object analysisObj = result[i][1];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setType(QuestionType.SUBJECTIVE.getType());
			question.setCreator(Session.get(Session.KEY_LOGIN_ID));
			questions.add(question);
		}
		return true;
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
		pageController.setTitleName(library.getName());
	}

}
