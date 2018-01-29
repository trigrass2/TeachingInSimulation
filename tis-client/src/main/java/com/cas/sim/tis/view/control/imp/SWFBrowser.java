package com.cas.sim.tis.view.control.imp;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;

public class SWFBrowser extends Browser {
	private static final String HTML_SWF_HEAD = //
			"<html xmlns='http://www.w3.org/1999/xhtml'>" + //
					"<head>" + //
					"<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />" + //
					"<style type='text/css'>" + //
					"html,body{  " + //
					"height:98%;  " + //
					"margin:0px;  " + //
					"}" + //
					"#navigation {" + //
					"height:100%;" + //
					"}" + //
					"</style>" + //
					"</head>" + //
					"<body>" + //
					"<div id='navigation'>" + //
					"<embed align=center src='";
	private static final String HTML_SWF_END = //
			"' width='100%' height='100%' type=application/x-shockwave-flash wmode='transparent' quality='high' />" + //
					"</div>" + //
					"</body>" + //
					"</html>"; //

	
	
	public SWFBrowser(BrowserType type) {
		super(type);
	}



	@Override
	public synchronized void loadHTML(String html) {
		html = HTML_SWF_HEAD + html + HTML_SWF_END;
		super.loadHTML(html);
	}
}
