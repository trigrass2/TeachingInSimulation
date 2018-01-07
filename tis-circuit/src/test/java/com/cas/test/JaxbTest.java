package com.cas.test;

import org.junit.Test;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.xml.util.JaxbUtil;

public class JaxbTest {
	@Test
	public void testName() throws Exception {
		String xml = "	<ElecCompDef name=\"继电器\" model=\"MY2NJ/PYF08A-E\"  desc=\"继电器是一种电控制器件，当输入量的变化达到规定要求时，在电气输出电路中使被控量发生预定的阶跃变化的一种电器。它具有控制系统（输入回路）和被控制系统（输出回路）之间的互动关系，通常应用于自动化的控制电路中，在电路中起着自动调节、安全保护、转换电路等作用。\">\r\n" + 
				"		<Terminal id=\"NC1\" name=\"NC1\" mdlName=\"NC1\" direction=\"y+\" />\r\n" + 
				"		<Terminal id=\"NC2\" name=\"NC2\" mdlName=\"NC2\" direction=\"y+\" />\r\n" + 
				"		<Terminal id=\"NO1\" name=\"NO1\" mdlName=\"NO1\" direction=\"y+\" />\r\n" + 
				"		<Terminal id=\"NO2\" name=\"NO2\" mdlName=\"NO2\" direction=\"y+\" />\r\n" + 
				"		<Terminal id=\"COM1\" name=\"COM1\" mdlName=\"C1\" direction=\"y-\" />\r\n" + 
				"		<Terminal id=\"COM2\" name=\"COM2\" mdlName=\"C2\" direction=\"y-\" />\r\n" + 
				"		<Terminal id=\"DCIN+\" name=\"DCIN+\" mdlName=\"D1\" direction=\"y-\" team=\"power\" />\r\n" + 
				"		<Terminal id=\"DCIN-\" name=\"DCIN-\" mdlName=\"D2\" direction=\"y-\" team=\"power\" />\r\n" + 
				"		<Magnetism>\r\n" + 
				"			<VoltageIO type=\"input\" term1Id=\"DCIN+\" term2Id=\"DCIN-\" value=\"+24\" deviation=\"5\" switchIn=\"off|on\" />\r\n" + 
				"			<LightIO name=\"继电器指示灯\" mdlName=\"L1\" glowColor=\"0|1|0|1\" />\r\n" + 
				"		</Magnetism>\r\n" + 
				"		<ResisState isDef=\"1\">\r\n" + 
				"			<ResisRelation term1Id=\"DCIN+\" term2Id=\"DCIN-\" value=\"650\" />\r\n" + 
				"		</ResisState>\r\n" + 
				"		<ResisState id=\"off\" isDef=\"1\">\r\n" + 
				"			<ResisRelation term1Id=\"NC1\" term2Id=\"COM1\" value=\"0\" />\r\n" + 
				"			<ResisRelation term1Id=\"NC2\" term2Id=\"COM2\" value=\"0\" />\r\n" + 
				"		</ResisState>\r\n" + 
				"		<ResisState id=\"on\">\r\n" + 
				"			<ResisRelation term1Id=\"NO1\" term2Id=\"COM1\" value=\"0\" />\r\n" + 
				"			<ResisRelation term1Id=\"NO2\" term2Id=\"COM2\" value=\"0\" />\r\n" + 
				"		</ResisState>\r\n" + 
				"	</ElecCompDef>\r\n";
		ElecCompDef eleccomp = JaxbUtil.converyToJavaBean(xml, ElecCompDef.class);
		System.out.println(eleccomp);
				
	}
}
