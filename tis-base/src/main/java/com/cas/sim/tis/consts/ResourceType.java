package com.cas.sim.tis.consts;

public enum ResourceType {
	/**
	 * 图片
	 */
	IMAGE(0, "static/images/resource/pic.png", new String[] { "*.png", "*.jpg" }, false),
	/**
	 * 动画
	 */
	SWF(1, "static/images/resource/swf.png", new String[] { "*.swf" }, false),
	/**
	 * 视频
	 */
	VIDEO(2, "static/images/resource/video.png", new String[] { "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi" }, false),
	/**
	 * 文本
	 */
	TXT(3, "static/images/resource/txt.png", new String[] { "*.txt" }, true),
	/**
	 * Word
	 */
	WORD(4, "static/images/resource/word.png", new String[] { "*.doc", "*.docx" }, true),
	/**
	 * Ppt
	 */
	PPT(5, "static/images/resource/ppt.png", new String[] { "*.ppt", "*.pptx" }, true),
	/**
	 * Excel
	 */
	EXCEL(6, "static/images/resource/excel.png", new String[] { "*.xls", "*.xlsx" }, true),
	/**
	 * Pdf
	 */
	PDF(7, "static/images/resource/pdf.png", new String[] { "*.pdf" }, false);

//	资源类型代号
	private int type;
//	资源图标
	private String icon;
//	suffixs
	private String[] suffixs;

	private boolean convertable;

	private ResourceType(int id, String icon, String[] suffixs, boolean convertable) {
		this.type = id;
		this.icon = icon;
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
				if (string.contains(suffix)) {
					return type.type;
				}
			}
		}
		throw new RuntimeException("不支持的文件后缀名:" + suffix);
	}
}
