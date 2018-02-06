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

import com.cas.sim.tis.consts.QuestionConsts;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.LibraryAction;
import com.cas.sim.tis.view.action.QuestionAction;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.library.LibraryList.LibraryMenuType;
import com.cas.util.FileUtil;
import com.cas.util.Util;

import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class PreviewQuestionPaper extends HBox implements IContent {

	@FXML
	private HBox btns;
	@FXML
	private Button templateBtn;
	@FXML
	private Button importBtn;
	@FXML
	private Button exportBtn;
	@FXML
	private Button renameBtn;
	@FXML
	private Button publishBtn;
	@FXML
	private Button practiceBtn;
	@FXML
	private Text libName;
	@FXML
	private VBox paper;

	private int rid;

	private LibraryMenuType menuType;

	public PreviewQuestionPaper(LibraryMenuType menuType, int rid) {
		loadFXML();
		this.rid = rid;
		this.menuType = menuType;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 界面初始化
	 */
	private void initialize() {
		Library library = SpringUtil.getBean(LibraryAction.class).findLibraryByID(rid);
		this.libName.setText(library.getName());

		if (menuType.isEditable()) {
			this.btns.getChildren().removeAll(practiceBtn);
		} else {
			this.btns.getChildren().removeAll(templateBtn, importBtn, exportBtn, renameBtn, publishBtn);
		}

		checkImportOrExport();

		loadQuestions();
	}

	private void checkImportOrExport() {
		boolean export = SpringUtil.getBean(QuestionAction.class).checkImportOrExport(rid);
		if (export) {
			this.btns.getChildren().remove(importBtn);
		} else {
			this.btns.getChildren().remove(exportBtn);
		}
	}

	private void loadQuestions() {
		this.paper.getChildren().clear();
		loadQuestionsByType(QuestionType.CHOICE);
		loadQuestionsByType(QuestionType.JUDGMENT);
		loadQuestionsByType(QuestionType.BLANK);
		loadQuestionsByType(QuestionType.SUBJECTIVE);
	}

	private void loadQuestionsByType(QuestionType type) {
		List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByLibraryAndQuestionType(rid, type.getType());
		if (questions.size() == 0) {
			return;
		}
		VBox box = new VBox(20);
		this.paper.getChildren().add(box);

		Title title = new Title();
		title.setTitle(type.getTitle());
		box.getChildren().add(title);
		for (int i = 0; i < questions.size(); i++) {
			int index = i + 1;
			Question question = questions.get(i);
			PreviewQuestionItem item = new PreviewQuestionItem(index, type, question);
			box.getChildren().add(item);
		}
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
		FileUtil.copyFile(QuestionConsts.QUESTION_TEMPLATE, target.getAbsolutePath(), true);
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
		SpringUtil.getBean(QuestionAction.class).addQuestions(questions);
	}

	@FXML
	private void exportExcel() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setInitialFileName(this.libName.getText() + ".xls");
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
			ExcelUtil.writeExcelByTemplate(QuestionConsts.QUESTION_TEMPLATE, target.getAbsolutePath(), ExcelUtil.EXCEL_TYPE_2003, datas, 1);
			showAlert(AlertType.INFORMATION, MsgUtil.getMessage("excel.export.success"));
		} catch (Exception e) {
			showAlert(AlertType.ERROR, e.getMessage());
		}
	}

	@FXML
	private void rename() {

	}

	@FXML
	private void publish() {

	}

	@FXML
	private void practice() {

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
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.CHOICE.getSheetName(), 4);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.stem"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.stem"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object optionsObj = result[i][1];
			if (Util.isEmpty(optionsObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.option"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String options = String.valueOf(optionsObj).replace("\r\n", "\\|").replace("\n", "\\|").trim();
			if (options.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.option"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object referenceObj = result[i][2];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.reference"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String reference = String.valueOf(referenceObj).trim();
			if (reference.length() > 10) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.reference"), String.valueOf(10));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object analysisObj = result[i][3];
			if (Util.isEmpty(analysisObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.analysis"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.analysis"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setOptions(options);
			question.setAnalysis(analysis);
			question.setReference(reference);
			question.setType(QuestionType.CHOICE.getType());
			question.setCreator(Session.get(Session.KEY_LOGIN_ID));
			questions.add(question);
		}
		return true;
	}

	private boolean loadJudgment(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.JUDGMENT.getSheetName(), 3);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.stem"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.stem"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object referenceObj = result[i][1];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.reference"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String reference = String.valueOf(referenceObj).trim();
			if (reference.length() > 10) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.reference"), String.valueOf(10));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object analysisObj = result[i][2];
			if (Util.isEmpty(analysisObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.analysis"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.analysis"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setReference(reference);
			question.setType(QuestionType.JUDGMENT.getType());
			question.setCreator(Session.get(Session.KEY_LOGIN_ID));
			questions.add(question);
		}
		return true;
	}

	private boolean loadBlank(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.BLANK.getSheetName(), 3);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.stem"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").replaceAll("（）", "（|）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.stem"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object referenceObj = result[i][1];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.reference"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String reference = String.valueOf(referenceObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (reference.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.reference"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object analysisObj = result[i][2];
			if (Util.isEmpty(analysisObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.analysis"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.analysis"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
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
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.stem"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.stem"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			Object analysisObj = result[i][1];
			if (Util.isEmpty(analysisObj)) {
				String reason = MsgUtil.getMessage("excel.cant.null", MsgUtil.getMessage("question.analysis"));
				showAlert(AlertType.WARNING, reason);
				return false;
			}
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("excel.over.length", MsgUtil.getMessage("question.analysis"), String.valueOf(250));
				showAlert(AlertType.WARNING, reason);
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

	private void showAlert(AlertType type, String reason) {
		Alert alert = new Alert(type, reason);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	@Override
	public void distroy() {

	}

	@Override
	public Node[] getContent() {
		return new Region[] { this };
	}

}
