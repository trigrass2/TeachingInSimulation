package com.cas.test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;

import com.cas.circuit.vo.Archive;
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

//		Archive archive = JaxbUtil.converyToJavaBean(new File(file, "Test.xml").toURI().toURL(), Archive.class);
		Archive archive = JaxbUtil.converyToJavaBean(new URL("http://192.168.1.123:8082/archives/Test.xml"), Archive.class);
		Assert.assertNotNull(archive);
		System.out.println(archive);
	}
}
