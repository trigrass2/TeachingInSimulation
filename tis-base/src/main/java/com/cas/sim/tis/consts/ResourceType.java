package com.cas.sim.tis.consts;

import javafx.scene.paint.Color;

public enum ResourceType {
	/**
	 * 图片
	 */
	IMAGE(0, "iconfont.svg.image", Color.web("#2bc9f7"), new String[] { "*.png", "*.jpg" }, false),
	/**
	 * 动画
	 */
	SWF(1, "iconfont.svg.video", Color.web("#9760f1"), new String[] { "*.swf" }, false),
	/**
	 * 视频
	 */
	VIDEO(2, "iconfont.svg.video", Color.web("#1cabbb"), new String[] { "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi" }, false),
	/**
	 * 文本
	 */
	TXT(3, "iconfont.svg.txt", Color.web("#eacb42"), new String[] { "*.txt" }, true),
	/**
	 * Word
	 */
	WORD(4, "iconfont.svg.word", Color.web("#2b73f7"), new String[] { "*.doc", "*.docx" }, true),
	/**
	 * Ppt
	 */
	PPT(5, "iconfont.svg.ppt", Color.web("#ff9e2c"), new String[] { "*.ppt", "*.pptx" }, true),
	/**
	 * Excel
	 */
	EXCEL(6, "iconfont.svg.excel", Color.web("#9abd57"), new String[] { "*.xls", "*.xlsx" }, true),
	/**
	 * Pdf
	 */
	PDF(7, "iconfont.svg.pdf", Color.web("#dd3a2e"), new String[] { "*.pdf" }, false),
	/**
	 * 在备案中使用的元件认知、典型案例、故障维修的图标
	 */
	LINK(8, "iconfont.svg.link", Color.web("#22678f"), new String[] {}, false);

//	资源类型代号
	private int type;
//	资源图标
	private String icon;
//	图标颜色
	private Color color;
//	suffixs
	private String[] suffixs;

	private boolean convertable;

	private ResourceType(int id, String icon, Color color, String[] suffixs, boolean convertable) {
		this.type = id;
		this.icon = icon;
		this.color = color;
		this.suffixs = suffixs;
		this.convertable = convertable;
	}

	public String[] getSuffixs() {
		return suffixs;
	}

	public int getType() {
		return type;
	}

	public String getIcon() {
		return icon;
	}

	public Color getColor() {
		return color;
	}

	public boolean isConvertable() {
		return convertable;
	}

	public static ResourceType getResourceType(int type) {
		for (ResourceType resourceType : values()) {
			if (resourceType.type == type) {
				return resourceType;
			}
		}
		throw new RuntimeException("不支持的资源类型" + type);
	}

	public static int parseType(String suffix) throws RuntimeException {
		if (suffix == null || "".equals(suffix)) {
			throw new RuntimeException("请检查文件后缀名");
		}
		for (ResourceType type : values()) {
			for (String string : type.suffixs) {
				if (string.contains(suffix.toLowerCase())) {
					return type.type;
				}
			}
		}
		throw new RuntimeException("不支持的文件后缀名:" + suffix);
	}
}
