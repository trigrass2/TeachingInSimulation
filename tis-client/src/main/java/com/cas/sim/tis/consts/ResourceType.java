package com.cas.sim.tis.consts;

public enum ResourceType {
	/**
	 * 图片
	 */
	IMAGE(0, "static/images/resource/pic.png", new String[] { "*.png", "*.jpg" }),
	/**
	 * 动画
	 */
	SWF(1, "static/images/resource/swf.png", new String[] { "*.swf" }),
	/**
	 * 视频
	 */
	VIDEO(2, "static/images/resource/video.png", new String[] { "*.mp4", "*.flv", "*.wmv", "*.rmvb", "*.avi" }),
	/**
	 * 文本
	 */
	TXT(3, "static/images/resource/txt.png", new String[] { "*.txt" }),
	/**
	 * Word
	 */
	WORD(4, "static/images/resource/word.png", new String[] { "*.doc", "*.docx" }),
	/**
	 * Ppt
	 */
	PPT(5, "static/images/resource/ppt.png", new String[] { "*.ppt", "*.pptx" }),
	/**
	 * Excel
	 */
	EXCEL(6, "static/images/resource/excel.png", new String[] { "*.xls", "*.xlsx" }),
	/**
	 * Pdf
	 */
	PDF(7, "static/images/resource/pdf.png", new String[] { "*.pdf" });

//	资源类型代号
	private int type;
//	资源图标
	private String icon;
//	suffixs
	private String[] suffixs;

	private ResourceType(int id, String icon, String[] suffixs) {
		this.type = id;
		this.icon = icon;
		this.suffixs = suffixs;
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
	
	public static ResourceType getResourceType(int type) {
		for (ResourceType resourceType : values()) {
			if(resourceType.type == type) {
				return resourceType;
			}
		}
		throw new RuntimeException("不支持的资源类型" + type);
	}

	public static int parseType(String suffix) throws RuntimeException{
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
