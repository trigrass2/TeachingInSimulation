package com.cas.sim.tis.view.control.imp.library;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.LibraryPublishAction;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.IPublish;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.control.imp.library.LibraryList.LibraryMenuType;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LibraryMenu extends LeftMenu implements IPublish, IDistory {

	private HBox box;
	private RotateTransition rotateTransition;

	@Override
	protected void initMenu() {
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (RoleConst.ADMIN == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.ADMIN_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.ADMIN_OLD), PageLevel.Level1);
			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.TEACHER_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.TEACHER_OLD), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.teacher"), "iconfont.svg.more", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.TEACHER_MINE), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.exam"), "iconfont.svg.record", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
			});
			addExamingMenu();
			Integer publishId = Session.get(Session.KEY_LIBRARY_PUBLISH_ID);
			if (publishId != null) {
				publish(publishId);
			}
		} else if (RoleConst.STUDENT == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.STUDENT_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.STUDENT_OLD), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.teacher"), "iconfont.svg.more", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.STUDENT_MINE), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.exam"), "iconfont.svg.record", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.train"), "iconfont.svg.record", e -> {

			});
		}
	}

	/**
	 * 
	 */
	private void addExamingMenu() {
		box = new HBox();
		box.setAlignment(Pos.CENTER);
		VBox.setVgrow(box, Priority.ALWAYS);
		menu.getChildren().add(box);
	}

	private void createExamingButton(LibraryPublish publish) {
		box.getChildren().clear();

		SVGGlyph glyph = new SVGGlyph("iconfont.svg.clock", Color.WHITE, 22);
		rotateTransition = new RotateTransition(Duration.millis(100), glyph);
		rotateTransition.setFromAngle(-30);
		rotateTransition.setToAngle(30);
		rotateTransition.setAutoReverse(true);

		rotateTransition.setOnFinished(e -> {
			if (rotateTransition.getToAngle() == 30) {
				rotateTransition.setFromAngle(30);
				rotateTransition.setToAngle(0);
				rotateTransition.setCycleCount(1);
				rotateTransition.setDelay(Duration.ZERO);
				rotateTransition.playFromStart();
			} else {
				rotateTransition.setFromAngle(-30);
				rotateTransition.setToAngle(30);
				rotateTransition.setCycleCount(3);
				rotateTransition.setDelay(Duration.seconds(2));
				rotateTransition.playFromStart();
			}
		});
		rotateTransition.playFromStart();

		String name = publish.getLibrary().getName();

		Button examing = new Button();
		examing.setGraphic(glyph);
		examing.setText(name);
		examing.setTooltip(new Tooltip(name));
		examing.getStyleClass().add("examing-menu");
		examing.setOnAction(e->{
			//TODO 
		});

		box.getChildren().add(examing);
	}

	@Override
	public void publish(int id) {
		if (rotateTransition != null) {
			rotateTransition.stop();
		}
		if (id == -1) {
			box.getChildren().clear();
		} else {
			LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(id);
			createExamingButton(publish);
		}
	}

	@Override
	public void distroy() {
		if (rotateTransition != null) {
			rotateTransition.stop();
		}
	}
}
