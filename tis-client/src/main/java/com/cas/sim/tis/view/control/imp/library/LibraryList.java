package com.cas.sim.tis.view.control.imp.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.consts.LibraryType;
import com.cas.sim.tis.view.control.IContent;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class LibraryList extends HBox implements IContent {
	private static final Logger LOG = LoggerFactory.getLogger(LibraryList.class);
	
	private LibraryMenuType menuType;

	public LibraryList(LibraryMenuType menuType) {
		this.menuType = menuType;
	}

	public enum LibraryMenuType {
		ADMIN_MOCK(LibraryType.MOCK, true), ADMIN_OLD(LibraryType.OLD, true), //
		TEACHER_MOCK(LibraryType.MOCK, false), TEACHER_OLD(LibraryType.OLD, false), TEACHER_MINE(LibraryType.TEACHERS, true), //
		STUDENT_MOCK(LibraryType.MOCK, false), STUDENT_OLD(LibraryType.OLD, false), STUDENT_MINE(LibraryType.TEACHERS, false);

		private LibraryType type;
		private boolean editable;

		private LibraryMenuType(LibraryType type, boolean editable) {
			this.type = type;
			this.editable = editable;
		}

		public LibraryType getType() {
			return type;
		}

		public void setType(LibraryType type) {
			this.type = type;
		}

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
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
