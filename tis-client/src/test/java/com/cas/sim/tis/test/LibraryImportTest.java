package com.cas.sim.tis.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.consts.QuestionType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.util.FileUtil;
import com.cas.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class LibraryImportTest {

	private Integer rid;
	private String PATH = "C:\\Users\\Administrator\\Desktop\\Excel";
	@Autowired
	@Qualifier("libraryServiceFactory")
	private RmiProxyFactoryBean libraryServiceFactory;
	@Autowired
	@Qualifier("questionServiceFactory")
	private RmiProxyFactoryBean questionServiceFactory;

	private void load(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				load(child);
			}
		} else {
			importExcel(file);
		}
	}

	private void importExcel(File source) {
		String name = FileUtil.getFileName(source.getAbsolutePath());
		Library library = new Library();
		library.setName(name);
		library.setNum(0);
		library.setTime(100);
		library.setType(0);
		library.setCreator(1);

		LibraryService libraryService = (LibraryService) libraryServiceFactory.getObject();
		libraryService.save(library);

		List<Library> librarys = libraryService.findAll();
		library = librarys.get(librarys.size()-1);
		
		rid=library.getId();

		List<Question> questions = new ArrayList<>();
		if (!loadChoice(source, questions) || !loadJudgment(source, questions) || !loadBlank(source, questions) || !loadSubjective(source, questions)) {
			return;
		}
		try {
			SpringUtil.getBean(QuestionAction.class).addQuestions(rid, questions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean loadChoice(File source, List<Question> questions) {
		Object[][] result = ExcelUtil.readExcelSheet(source.getAbsolutePath(), QuestionType.CHOICE.getSheetName(), 5);
		for (int i = 2; i < result.length; i++) {
			Object descObj = result[i][0];
			if (Util.isEmpty(descObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.stem"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object optionsObj = result[i][1];
			if (Util.isEmpty(optionsObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.option"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String options = String.valueOf(optionsObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (options.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.option"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object referenceObj = result[i][2];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.reference"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			String reference = String.valueOf(referenceObj).trim();
			if (reference.length() > 10) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.reference"), 10);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			Object pointObj = result[i][3];
			if (Util.isEmpty(pointObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.point"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			String point = String.valueOf(pointObj).trim();
			if (!Util.isNumeric(point)) {
				String reason = MsgUtil.getMessage("alert.warning.not.number", MsgUtil.getMessage("question.point"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			Object analysisObj = result[i][4];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 5, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setOptions(options);
			question.setAnalysis(analysis);
			question.setPoint(Float.parseFloat(point));
			question.setReference(reference);
			question.setType(0);
			question.setCreator(1);
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
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object referenceObj = result[i][1];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.reference"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String reference = String.valueOf(referenceObj).trim();
			if (reference.length() > 10) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.reference"), 10);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object pointObj = result[i][2];
			if (Util.isEmpty(pointObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.point"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			String point = String.valueOf(pointObj).trim();
			if (!Util.isNumeric(point)) {
				String reason = MsgUtil.getMessage("alert.warning.not.number", MsgUtil.getMessage("question.point"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 3, reason));
				return false;
			}
			Object analysisObj = result[i][3];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setPoint(Float.parseFloat(point));
			question.setReference(reference);
			question.setType(1);
			question.setCreator(1);
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
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").replaceAll("（）", "（|）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object referenceObj = result[i][1];
			if (Util.isEmpty(referenceObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.reference"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String reference = String.valueOf(referenceObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (reference.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.reference"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object pointObj = result[i][2];
			if (Util.isEmpty(pointObj)) {
				String reason = MsgUtil.getMessage("alert.warning.cant.null", MsgUtil.getMessage("question.point"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			String point = String.valueOf(pointObj).replace("\r\n", "|").replace("\n", "|").trim();
			if (!Util.isNumeric(point)) {
				String reason = MsgUtil.getMessage("alert.warning.not.number", MsgUtil.getMessage("question.point"));
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Object analysisObj = result[i][3];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 4, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setPoint(Float.parseFloat(point));
			question.setReference(reference);
			question.setType(2);
			question.setCreator(1);
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
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			String title = String.valueOf(descObj).replaceAll("\\(", "（").replaceAll("\\)", "）").trim();
			if (title.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.stem"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 1, reason));
				return false;
			}
			Object analysisObj = result[i][1];
			String analysis = String.valueOf(analysisObj).trim();
			if (analysis.length() > 250) {
				String reason = MsgUtil.getMessage("alert.warning.over.length", MsgUtil.getMessage("question.analysis"), 250);
				System.out.println(MsgUtil.getMessage("excel.import.error", i + 1, 2, reason));
				return false;
			}
			Question question = new Question();
			question.setRelateId(rid);
			question.setTitle(title);
			question.setAnalysis(analysis);
			question.setType(3);
			question.setCreator(1);
			questions.add(question);
		}
		return true;
	}

	@Test
	public void test() {
		load(new File(PATH));
	}
}
