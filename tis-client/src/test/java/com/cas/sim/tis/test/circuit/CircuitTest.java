package com.cas.sim.tis.test.circuit;

import java.net.URL;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CircuitTest {

	@Resource
	@Qualifier(value = "elecCompServiceFactory")
	private RmiProxyFactoryBean elecCompServiceFactory;

	@Resource
	private HTTPUtils util;

	@Test
	public void testParseCfg() throws Exception {
		ElecCompService service = (ElecCompService) elecCompServiceFactory.getObject();
//		ID:10\11
//		ElecComp elecComp = service.findById(10);
		ElecComp elecComp = service.findBy("model", "CJX2-12");

		String cfgPath = util.getHttpUrl(elecComp.getCfgPath());
		URL url = new URL(cfgPath);
		ElecCompDef elecCompDef = JaxbUtil.converyToJavaBean(url, ElecCompDef.class);
		System.out.println(elecCompDef);
		System.out.println(JaxbUtil.convertToXml(elecCompDef));
	}
}
