package com.cas.test;

import java.io.File;
import java.nio.file.Files;

import org.junit.Test;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.xml.util.JaxbUtil;

public class JaxbTest {
	@Test
	public void testName() throws Exception {

		File file = new File("src/main/resources/config/");
//		File[] subFiles = file.listFiles();
//		for (int i = 0; i < subFiles.length; i++) {
//			File xml = subFiles[i];
//			if(xml.getName().endsWith("xml")) {
//				ElecCompDef eleccomp = JaxbUtil.converyToJavaBean(new String(Files.readAllBytes(xml.toPath())), ElecCompDef.class);
//				System.out.println(JaxbUtil.convertToXml(eleccomp));
//				eleccomp.build();
//			}
//		}
		ElecCompDef eleccomp = JaxbUtil.converyToJavaBean(new String(Files.readAllBytes(new File(file, "DZ47-63C16.xml").toPath())), ElecCompDef.class);
		System.out.println(JaxbUtil.convertToXml(eleccomp));

	}
}
