package com.cas.sim.tis.view.control.imp.pagination;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private IntegerProperty pageIndex = new SimpleIntegerProperty(0);
	/**
	 * 总页数
	 */
	private IntegerProperty pageCount = new SimpleIntegerProperty(10) {
		public void set(int newValue) {
			int oldValue = get();
			if (newValue == oldValue) {
				return;
			} else {
				super.set(newValue);
				refrash();
			}
		};
	};

	private Button prev = new Button();
	private Button next = new Button();

	private HBox pages = new HBox();
	private ToggleGroup pageBtn = new ToggleGroup();
	private ToggleButton first = new ToggleButton("1");
	private ToggleButton last = new ToggleButton();
	private Label etc1 = new Label("...");
	private Label etc2 = new Label("...");
	private boolean refresh;

	public PaginationBar() {
		setAlignment(Pos.CENTER);
		getStyleClass().add("pagination-bar");
		getChildren().addAll(prev, pages, next);

		prev.getStyleClass().add("prev");
		prev.setDisable(true);
		prev.setOnAction(e -> {
			prev();
		});
		Region prevIcon = new Region();
		prevIcon.getStyleClass().add("prev-arrow");
		prev.setGraphic(prevIcon);

		next.getStyleClass().add("next");
		next.setDisable(true);
		next.setOnAction(e -> {
			next();
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
		pageIndex.addListener((b, o, n) -> {
			if (n.intValue() == o.intValue()) {
				refrash();
			} else if (n.intValue() > o.intValue()) {
				next();
			} else {
				prev();
			}
		});
		pageBtn.selectedToggleProperty().addListener((b, o, n) -> {
			if (refresh) {
				if (n == null) {
					pageBtn.selectToggle(o);
				} else {
					refresh = false;
				}
				return;
			}
			if (n == null) {
				if (pageBtn.getToggles().size() > 0) {
					pageBtn.selectToggle(o);
				}
				return;
			}
			Object userDate = n.getUserData();
			if (userDate instanceof Integer) {
				setPageIndex((int) userDate);
			}
		});
		pageBtn.getToggles().get(0).setSelected(true);
	}

	/**
	 * 创建除首页、末页以外的中间页
	 */
	private void refrash() {
		refresh = true;
		pages.getChildren().clear();
		pageBtn.getToggles().clear();

		pages.getChildren().add(first);
		pageBtn.getToggles().add(first);
		if (getPageCount() == 1) {
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
		last.setUserData(getPageCount());
		last.setText(String.valueOf(getPageCount() - 1));
		pages.getChildren().add(last);
		pageBtn.getToggles().add(last);
		pageBtn.getToggles().get(getPageIndex()).setSelected(true);
	}

	private void addMiddles(int size, int start) {
		for (int i = 0; i < size; i++) {
			int no = start + i;
			ToggleButton middle = new ToggleButton(String.valueOf(no));
			middle.getStyleClass().add("page");
			middle.setUserData(no - 1);
			pages.getChildren().add(middle);
			pageBtn.getToggles().add(middle);
		}
	}

	public void setPageIndex(int value) {
		pageIndexProperty().set(value);
	}

	public int getPageIndex() {
		return pageIndex == null ? 0 : pageIndex.get();
	}

	public IntegerProperty pageIndexProperty() {
		if (pageIndex == null) {
			pageIndex = new SimpleIntegerProperty(this, "pageIndex", 0);
		}
		return pageIndex;
	}

	public void setPageCount(int value) {
		pageCountProperty().set(value);
	}

	public int getPageCount() {
		return pageCount == null ? 10 : pageCount.get();
	}

	public IntegerProperty pageCountProperty() {
		if (pageCount == null) {
			pageCount = new SimpleIntegerProperty(this, "pageCount", 10);
		}
		return pageCount;
	}

	/**
	 * 向前一页
	 */
	private void prev() {
		if (getPageIndex() == 0) {
			prev.setDisable(true);
		} else {
			prev.setDisable(false);
		}
		refrash();
	}

	/**
	 * 向后一页
	 */
	private void next() {
		if (getPageIndex() + 1 == getPageCount()) {
			next.setDisable(true);
		} else {
			next.setDisable(false);
		}
		refrash();
	}

	public ToggleGroup getPageBtn() {
		return pageBtn;
	}
}
