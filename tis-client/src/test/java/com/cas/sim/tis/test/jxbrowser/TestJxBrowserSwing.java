package com.cas.sim.tis.test.jxbrowser;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class TestJxBrowserSwing {
	public static void main(String[] args) {
		Browser browser = new Browser();
		BrowserView view = new BrowserView(browser);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(view, BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

//		browser.loadURL("https://www.baidu.com");
		browser.loadURL("ftp://192.168.1.19/test/Fanuc0i参数说明书.pdf");
	}
}
