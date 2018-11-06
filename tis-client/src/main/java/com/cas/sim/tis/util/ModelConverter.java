package com.cas.sim.tis.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * 将模型缩放比例 从node转移到geometry中
 * @author Administrator
 */
public class ModelConverter {

	public static final String DIR = "E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets";

	public static void main(String[] args) {
		AssetManager mgr = new DesktopAssetManager(true);
		mgr.registerLocator(DIR, FileLocator.class);

//		List<File> files = readDir();//
		List<File> files = new ArrayList<>();
//		files.add(new File("E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets\\Model\\NBE7\\NBE7-static.j3o"));
		files.forEach(f -> save(f, mgr.loadModel(f.getAbsolutePath().replace(DIR, ""))));
	}

	private static void save(File f, Spatial sp) {
		System.out.println("convert" + f);
		cleanUserData(sp);

//		convert(sp);
		convert2(sp);

//		SaveGame.saveGame(gamePath, dataName, data);
		BinaryExporter ex = BinaryExporter.getInstance();
		try {

			ex.save(sp, new File(f.getParentFile(), f.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	};

	private static void cleanUserData(Spatial sp) {
		if (sp instanceof Node) {
			((Node) sp).getChildren().forEach(ModelConverter::cleanUserData);
		} else {
//			ORIGINAL_PATH	/Models/vm1204/CJX2-1210-static.blend/1/L1
//			ORIGINAL_NAME	1/L1
//			_SCALE	(100.0, 100.0, 100.0)
			if (sp.getParent() != null) {
				sp.getParent().setUserData("ORIGINAL_PATH", null);
				sp.getParent().setUserData("ORIGINAL_NAME", null);
				sp.getParent().setUserData("_SCALE", null);
			}
		}
	}

	private static void convert(Spatial sp) {
		if (sp instanceof Node) {
			((Node) sp).getChildren().forEach(ModelConverter::convert);
		} else {
			if (sp.getParent() == null) {
				return;
			}

			Vector3f parentScale = sp.getParent().getUserData("_SCALE");
			if ("YBLX-K1_05".equals(sp.getParent().getName())) {
				System.out.println("ModelConverter.convert()");
			}
			if (parentScale == null) {
				parentScale = sp.getParent().getLocalScale();
				sp.getParent().setUserData("_SCALE", parentScale.clone());
			}

			sp.setLocalScale(sp.getLocalScale().mult(parentScale));
			sp.getParent().setLocalScale(Vector3f.UNIT_XYZ);

			System.out.println(sp.getLocalScale() + " --> " + sp.getParent().getLocalScale());
		}
	}

	private static void convert2(Spatial sp) {
		if (sp instanceof Node) {
			((Node) sp).getChildren().forEach(c -> {
				c.setLocalScale(c.getLocalScale().mult(sp.getLocalScale()));
				c.setLocalTranslation(c.getLocalTranslation().mult(sp.getLocalScale()));
			});
			sp.setLocalScale(Vector3f.UNIT_XYZ);
			((Node) sp).getChildren().forEach(ModelConverter::convert2);
		}
	}

	private static List<File> readDir() {
		File dir = new File(DIR + "/Model"); // 这一层是目录Model
		File[] dirs = dir.listFiles();// 这一层是目录Model

		List<File> result = new LinkedList<>();
		for (File file : dirs) {
			File[] dels = file.listFiles(f -> f.getName().contains("convert_"));
			for (File d : dels) {
				d.deleteOnExit();
			}

			result.addAll(Arrays.asList(file.listFiles(f -> f.getName().endsWith("static.j3o"))));
		}
		return result;
	}
}
