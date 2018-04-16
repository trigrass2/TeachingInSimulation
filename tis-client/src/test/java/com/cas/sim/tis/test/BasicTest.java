package com.cas.sim.tis.test;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.xml.util.JaxbUtil;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class BasicTest {
	@Test
	public void testList() throws Exception {
		List<ResourceType> resourceTypes = new ArrayList<>();
		resourceTypes.add(ResourceType.EXCEL);
		resourceTypes.add(ResourceType.IMAGE);
		resourceTypes.add(ResourceType.SWF);
		resourceTypes.add(ResourceType.VIDEO);
		List<Integer> orders = resourceTypes.stream().map(ResourceType::getType).collect(Collectors.toList());
		orders.forEach(System.out::println);
	}

	@Test
	public void testParseCfgLocal() throws Exception {
		URL url = new URL("http://192.168.1.19:8082/configurations/Accontactor/CJX2-1210.xml");
//		URL url = new URL("file:///C:/Users/Administrator/Documents/New%20folder/Accontactor/CJX2-1210.xml");
		ElecCompDef eleccomp = JaxbUtil.converyToJavaBean(url, ElecCompDef.class);
		System.out.println(eleccomp);
	}

	@Test
	public void testFile() throws Exception {
//		File file = new File("C:\\dd\\dd.txt");
//		Assert.assertTrue(Files.exists(Paths.get("C:\\dd\\dd.txt")));

		// "ja","va","java"
		String str2 = new StringBuilder("ja").append("va").toString();
		Assert.assertFalse(str2 == "java"); //
		Assert.assertFalse(str2 == str2.intern());

		Assert.assertTrue(str2.intern() == "java");
		Assert.assertTrue(str2.intern() == str2.intern());
	}

	@Test
	public void testName() throws Exception {
		int flag = 0;
		System.out.println(flag |= 0x01);
		if ((flag & 0x01) != 0) {
			System.out.println("change");

			flag &= ~0x01;
		}

		System.out.println(flag |= 0x04);

		System.out.println((flag & 0x01) == 0);
	}
	
	@Test
	public void testSort() throws Exception {
		System.out.println(System.currentTimeMillis());
		for (int i = 0; i < 100000; i++) {
			
		}
		System.out.println(System.currentTimeMillis());
	}
	
	@Test
	public void testNotNull() throws Exception {
		MouseEventState state = new MouseEventState();
		state.addCandidate(null, null);
	}
	
	@Test
	public void testQua() throws Exception {
		Vector3f axisStore = new Vector3f();
		new Quaternion(0.0010627164f, 0.90029067f, -0.43528637f, 0.0021979825f).toAngleAxis(axisStore );
		System.out.println(axisStore.mult(FastMath.RAD_TO_DEG));
	}
}
