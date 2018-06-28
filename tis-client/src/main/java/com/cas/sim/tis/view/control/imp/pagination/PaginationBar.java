package com.cas.sim.tis.view.control.imp.pagination;

import java.util.function.Consumer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class PaginationBar extends HBox {
	/**
	 * 最多连续显示页号按钮数量
	 */
	private static final int LIMIT = 5;
	/**
	 * 当前页
	 */
	private IntegerProperty pageIndex = new SimpleIntegerProperty(-1);
	/**
	 * 总页数
	 */
	private IntegerProperty pageCount = new SimpleIntegerProperty(10);

	// 返回当前页
	private Consumer<Integer> content;

	private Button prev = new Button();
	private Button next = new Button();

	private HBox pages = new HBox();
	private ToggleGroup pageBtn = new ToggleGroup();
	private ToggleButton first = new ToggleButton("1");
	private ToggleButton last = new ToggleButton();
	private Label etc1 = new Label("...");
	private Label etc2 = new Label("...");

	public PaginationBar() {
		setAlignment(Pos.CENTER);
		getStyleClass().add("pagination-bar");
		getChildren().addAll(prev, pages, next);

		prev.getStyleClass().add("prev");
		prev.setDisable(true);
		prev.setOnAction(e -> {
			setPageIndex(getPageIndex() - 1);
		});
		Region prevIcon = new Region();
		prevIcon.getStyleClass().add("prev-arrow");
		prev.setGraphic(prevIcon);

		next.getStyleClass().add("next");
		next.setDisable(true);
		next.setOnAction(e -> {
			setPageIndex(getPageIndex() + 1);
		});
		Region nextIcon = new Region();
		nextIcon.getStyleClass().add("next-arrow");
		next.setGraphic(nextIcon);

		first.setUserData(0);
		first.getStyleClass().add("page");

		last.getStyleClass().add("page");

		etc1.getStyleClass().add("etc");
		etc2.getStyleClass().add("etc");

		refrash();
		pageBtn.getToggles().get(0).setSelected(true);

//		页码监听
		pageIndex.addListener((ChangeListener<Number>) (s, o, n) -> {
			next.setDisable(false);
			prev.setDisable(false);
			if (n.intValue() >= getPageCount() - 1) {
				next.setDisable(true);
//				n = getPageCount() - 1;
			}
			if (n.intValue() <= 0) {
				prev.setDisable(true);
//				n = 0;
			}
			refrash();
		});

		pageCount.addListener((s, o, n) -> {
			setPageIndex(0);
		});
	}

	/**
	 * 创建除首页、末页以外的中间页
	 */
	private void refrash() {
		if (content != null) {
			content.accept(getPageIndex());
		}

		pages.getChildren().clear();
		pageBtn.getToggles().clear();

		first.setDisable(false);
		first.setOnAction(e -> {
			setPageIndex(0);
		});
		pages.getChildren().add(first);
		pageBtn.getToggles().add(first);
		if (getPageCount() == 0) {
			first.setDisable(true);
			return;
		} else if (getPageCount() == 1) {
			return;
		} else if (getPageCount() <= LIMIT + 1) {
			addMiddles(getPageCount() - 2, 2);
		} else if (getPageIndex() - LIMIT / 2 <= 1) {
			addMiddles(LIMIT - 1, 2);
			pages.getChildren().add(etc2);
		} else if (getPageIndex() + LIMIT / 2 + 1 >= getPageCount() - 1) {
			pages.getChildren().add(etc1);
			addMiddles(LIMIT - 1, getPageCount() - LIMIT + 1);
		} else {
			pages.getChildren().add(etc1);
			addMiddles(LIMIT, getPageIndex() + 1 - LIMIT / 2);
			pages.getChildren().add(etc2);
		}
		last.setUserData(getPageCount() - 1);
		last.setText(String.valueOf(getPageCount()));
		last.setOnAction(e -> {
			setPageIndex((int) last.getUserData());
		});
		pages.getChildren().add(last);
		pageBtn.getToggles().add(last);
		for (Toggle toggle : pageBtn.getToggles()) {
			if (getPageIndex() == (int) toggle.getUserData()) {
				toggle.setSelected(true);
			}
		}
	}

	private void addMiddles(int size, int start) {
		for (int i = 0; i < size; i++) {
			int no = start + i;
			ToggleButton middle = new ToggleButton(String.valueOf(no));
			middle.getStyleClass().add("page");
			middle.setUserData(no - 1);
			middle.setOnAction(e -> {
				setPageIndex(no - 1);
			});
			pages.getChildren().add(middle);
			pageBtn.getToggles().add(middle);
		}
	}

	private void setPageIndex(int value) {
		pageIndexProperty().set(value);
	}

	private int getPageIndex() {
		return pageIndex.get();
	}

	private IntegerProperty pageIndexProperty() {
		return pageIndex;
	}

	public void setPageCount(int value) {
		pageCountProperty().set(value);
	}

	private int getPageCount() {
		return pageCount == null ? 10 : pageCount.get();
	}

	private IntegerProperty pageCountProperty() {
		if (pageCount == null) {
			pageCount = new SimpleIntegerProperty(this, "pageCount", 10);
		}
		return pageCount;
	}

	public void reload() {
//		确保第0页刷新内容
		if(getPageIndex() == 0) {
			refrash();
		}
		setPageIndex(0);
	}

	public void setContent(Consumer<Integer> content) {
		this.content = content;
	}
}
