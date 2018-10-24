package com.cas.sim.tis.message.handler;

import java.util.List;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.BrokenPublishAction;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.action.PreparationPublishAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.BrokenPublish;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.ExamView;
import com.cas.sim.tis.view.PageView;
import com.cas.sim.tis.view.PreparationExamView;
import com.cas.sim.tis.view.control.imp.broken.BrokenCase3D;
import com.cas.sim.tis.view.control.imp.broken.BrokenCaseBtnController;
import com.cas.sim.tis.view.controller.ExamController;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PreparationExamController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
import com.jme3.network.Client;

import javafx.application.Platform;

public class ExamMessageHandler implements ClientHandler<ExamMessage> {

	@Override
	public void execute(Client client, ExamMessage m) throws Exception {
		int examType = m.getExamType();
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (ExamMessage.EXAM_TYPE_LIBRARY == examType) {
			if (role == RoleConst.TEACHER) {
				Session.set(Session.KEY_LIBRARY_PUBLISH_ID, m.getPid());
			} else if (role == RoleConst.STUDENT) {
				libraryExam(m);
			}
		} else if (ExamMessage.EXAM_TYPE_PREPARATION == examType) {
			if (role == RoleConst.TEACHER) {
				Session.set(Session.KEY_PREPARATION_PUBLISH_ID, m.getPid());
			} else if (role == RoleConst.STUDENT) {
				preparationExam(m);
			}
		} else if (ExamMessage.EXAM_TYPE_BROKEN == examType) {
			if (role == RoleConst.TEACHER) {
				Session.set(Session.KEY_BROKEN_CASE_PUBLISH_ID, m.getPid());
			} else if (role == RoleConst.STUDENT) {
				repairExam(m);
			}
		}
	}

	private void libraryExam(ExamMessage m) {
		int messageType = m.getMessageType();
		if (ExamMessage.MESSAGE_TYPE_START == messageType) {
			LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(m.getPid());
			Platform.runLater(() -> {
				Application.showView(ExamView.class);

				ExamController controller = SpringUtil.getBean(ExamController.class);
				controller.initialize(publish);
			});
		} else if (ExamMessage.MESSAGE_TYPE_OVER == messageType) {
			Platform.runLater(() -> {
				ExamController controller = SpringUtil.getBean(ExamController.class);
				if (m.getPid() == controller.getLibraryPublish().getId()) {
					controller.submit(true);
				}
			});
		}
	}

	private void preparationExam(ExamMessage m) {
		int messageType = m.getMessageType();
		if (ExamMessage.MESSAGE_TYPE_START == messageType) {
			PreparationPublish publish = SpringUtil.getBean(PreparationPublishAction.class).findPublishById(m.getPid());
			List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByQuestionIds(publish.getLibrary().getQuestionIds());

			Platform.runLater(() -> {
				Application.showView(PreparationExamView.class);

				PreparationExamController controller = SpringUtil.getBean(PreparationExamController.class);
				controller.initialize(publish, questions);
			});
		} else if (ExamMessage.MESSAGE_TYPE_OVER == messageType) {
			Platform.runLater(() -> {
				PreparationExamController controller = SpringUtil.getBean(PreparationExamController.class);
				if (m.getPid() == controller.getPreparationPublish().getId()) {
					controller.submit(true);
				}
			});
		}
	}

	private void repairExam(ExamMessage m) {
		int messageType = m.getMessageType();
		if (ExamMessage.MESSAGE_TYPE_START == messageType) {
			BrokenPublish publish = SpringUtil.getBean(BrokenPublishAction.class).findPublishById(m.getPid());
			// TODO 加载维修案例考核界面
			Platform.runLater(() -> {
				Application.showView(PageView.class);
				PageController controller = SpringUtil.getBean(PageController.class);
				BrokenCase3D content = new BrokenCase3D(new BrokenCaseState(), new BrokenCaseBtnController(CaseMode.BROKEN_EXAM_MODE));

				controller.loadContent(content, PageLevel.Level1);
				controller.setLeftMenuEnable(false);
				controller.setBackEnable(false);
				controller.setEndHideLoading(c -> {
					Session.set(Session.KEY_BROKEN_CASE_PUBLISH_ID, publish.getId());
					content.setupCase(publish.getBrokenCase(), CaseMode.BROKEN_EXAM_MODE);
				});
			});
		} else if (ExamMessage.MESSAGE_TYPE_OVER == messageType) {
			// TODO 考核结束显示界面
			Platform.runLater(() -> {

			});
		}
	}
}
