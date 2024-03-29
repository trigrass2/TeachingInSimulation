package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.ILinkTarget;
import com.cas.gas.vo.GasPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * @author 张振宇 Jul 30, 2015 3:32:19 PM
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ElecComp {
	@XmlAttribute
	private String id;
//	元器件模型名称
	@XmlAttribute
	private String mdlName;
//	元器件标签名
	@XmlAttribute
	private String tagName;
//	元器件额外信息
//	FIXME
	private String elementText;

	private ElecCompDef def;
	private BaseElectricCompLogic compLogic;
	private Collection<ILinkTarget> linkTargets = new ArrayList<ILinkTarget>();
	private Map<String, String> properties;

	public void init(ElecCompDef template, Spatial elecCompMdl) {
//		元气件模板
		if (template == null) {
			throw new NullPointerException("没有对应的元器件");
		}
		if (elecCompMdl == null) {
			throw new RuntimeException("没有模型");
		}

//		FIXME 2018-01-09 引用元器件的配置ID， 然后读取配置文件创建对象
		def = template.clone();
		def.setRef(this);

		properties = getStringMap(po.getElementText());
		Iterator<Entry<String, String>> iter = properties.entrySet().iterator();
		Map.Entry<String, String> entry = null;
		while (iter.hasNext()) {
			entry = iter.next();
			Terminal terminal = def.getTerminal(entry.getKey());
			if (terminal != null) {
				terminal.setName(entry.getValue());
			}
		}

//		创建元器件的逻辑类
		compLogic = def.buildCompLogic();
		compLogic.setElecComp(this);
		compLogic.initialize((Node) elecCompMdl);
//		添加端子监听事件
		Collection<Terminal> terminals = def.getTerminalMap().values();
		Collection<GasPort> gasPorts = def.getGasPortMap().values();
		Collection<Jack> jacks = def.getJackMap().values();

		linkTargets.addAll(terminals);
		linkTargets.addAll(gasPorts);
		linkTargets.addAll(jacks);

//		FIXME 实像显示元器件模型
//		JmeUtil.untransparent(elecCompMdl);
	}

	public ElecCompDef getDef() {
		if (def == null) {
			log.error("没有给元器件模型‘" + po.getMdlName() + "’配置一个型号");
		}
		return def;
	}

	/**
	 * @return the compState
	 */
	public BaseElectricCompLogic getCompLogic() {
		return compLogic;
	}

	public void setCompLogic(BaseElectricCompLogic compState) {
		this.compLogic = compState;
	}

	/**
	 * @return the linkTargets
	 */
	public Collection<ILinkTarget> getLinkTargets() {
		return linkTargets;
	}

	public boolean isCable() {
		return def.isCable();
	}

	/**
	 * @return
	 */
	public List<Magnetism> getMagnetisms() {
		return def.getMagnetisms();
	}

//	/*
//	 * (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		if (Util.notEmpty(def.getPO().getDesc())) {
//			return def.getPO().getName() + "：标签[" + po.getTagName() + "] / 型号：[" + def.getPO().getModel() + "]\r\n简介：" + def.getPO().getDesc();
//		}
//		return def.getPO().getName() + "：标签[" + po.getTagName() + "] / 型号：[" + def.getPO().getModel() + "]";
//	}
//
//	public String getProperty(String propName) {
//		return properties.get(propName);
//	}

}
