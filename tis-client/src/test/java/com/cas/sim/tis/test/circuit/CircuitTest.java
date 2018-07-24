package com.cas.sim.tis.test.circuit;

import java.net.URL;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.util.JaxbUtil;
import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.HTTPUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CircuitTest {

	@Resource
	ElecCompAction action;

	@Test
	public void testParseCfg() throws Exception {
//		ID:10\11
//		ElecComp elecComp = service.findById(10);
		ElecComp elecComp = action.getElecComp("CJX2-1210");
		System.out.println(elecComp);
		String cfgPath = HTTPUtils.getFullPath(elecComp.getCfgPath());
		URL url = new URL(cfgPath);
		ElecCompDef elecCompDef = JaxbUtil.converyToJavaBean(url, ElecCompDef.class);
		System.out.println(elecCompDef);
		System.out.println(JaxbUtil.convertToXml(elecCompDef));
	}
}
