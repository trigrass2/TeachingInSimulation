package com.cas.test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.Test;

import com.cas.circuit.vo.CompRoot;
import com.cas.sim.tis.xml.util.JaxbUtil;

public class JaxbTest {
	@Test
	public void testName() throws Exception {
		
		File file = new File("src/main/resources/components");
//		File[] subFiles = file.listFiles();
//		for (int i = 0; i < subFiles.length; i++) {
//			File xml = subFiles[i];
//			System.out.println("JaxbTest.testName()" + xml);
//			if(xml.getName().endsWith("xml")) {
//				CompRoot eleccomp = JaxbUtil.converyToJavaBean(new String(Files.readAllBytes(xml.toPath())), CompRoot.class);
//				System.out.println(JaxbUtil.convertToXml(eleccomp));
//				eleccomp.build();
//			}
//		}
		
		CompRoot eleccomp = JaxbUtil.converyToJavaBean(new String(Files.readAllBytes(new File(file,"HPStation.xml").toPath())), CompRoot.class);
		System.out.println(JaxbUtil.convertToXml(eleccomp));
		eleccomp.build();

	}
}
