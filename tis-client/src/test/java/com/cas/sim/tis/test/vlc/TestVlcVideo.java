package com.cas.sim.tis.test.vlc;

import javax.swing.JFrame;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class TestVlcVideo {
	public static void main(String[] args) {
//		**********************************
//		注意：需要将VLC插件拷贝到项目根目录中
//		**********************************

		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "VLC");
//		System.out.println("LibVlc.INSTANCE.libvlc_get_version():"+LibVlc.INSTANCE.libvlc_get_version());
		JFrame frame = new JFrame("vlcj Tutorial");
		EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		frame.setContentPane(mediaPlayerComponent);
		frame.setLocation(100, 100);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		mediaPlayerComponent.getMediaPlayer().playMedia("http://192.168.1.19:8082/Test/试题.mp4");// please change it to an existed media file
	}
}
