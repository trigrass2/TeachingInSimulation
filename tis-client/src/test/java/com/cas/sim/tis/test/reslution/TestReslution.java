package com.cas.sim.tis.test.reslution;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class TestReslution {
	@Test
	public void display2() {
		GLFW.glfwInit();
		long monitor = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode.Buffer modes = GLFW.glfwGetVideoModes(monitor);
		while (modes.hasRemaining()) {
			GLFWVidMode mode = modes.get();
			System.out.println(String.format("%sx%s", mode.width(), mode.height()));
		}
	}

	@Test
	public void display1() {
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice sd = g.getDefaultScreenDevice();
		DisplayMode[] dm = sd.getDisplayModes();

		List<String> resolution = new ArrayList<>();
		for (int i = 0; i < dm.length; i++) {
			String res = String.format("%sx%s", dm[i].getWidth(), dm[i].getHeight());
			if (resolution.indexOf(res) == -1) {
				resolution.add(res);
			}
		}
		resolution.forEach(System.out::println);
	}
}
