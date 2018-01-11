package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.util.R;
import com.cas.sim.tis.xml.adapter.ColorRGBAAdapter;
import com.cas.sim.tis.xml.adapter.TerminalAdapter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

/**
 * 导线
 * @author sco_pra
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Wire {
	@XmlAttribute(name = "stitch1")
	@XmlJavaTypeAdapter(TerminalAdapter.class)
	private Terminal term1;
	@XmlAttribute(name = "stitch2")
	private Terminal term2;
	@XmlAttribute
	private String mark;

	@XmlAttribute
	private Float width;
	@XmlAttribute
	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
	private ColorRGBA color;

	private Map<Spatial, Terminal> models = new HashMap<>();

	private boolean brokenBreak = false;

	private String wireNum;

	public Wire() {
	}

	public Wire(float width, ColorRGBA color) {
		this.width = width;
		this.color = color;
	}

	public void build() {
		bind(term1);
		bind(term2);
	}

	/**
	 * 将导线绑到连接头上
	 */
	public void bind(Terminal term) {
		if (term == null || isBothBinded()) {
			return;
		}
//		System.out.println(this + "Wire.bind()" +  term);
		if (term1 == null) {
			term1 = (Terminal) term;
		} else if (term2 == null) {
			term2 = (Terminal) term;
		}

		List<Wire> linkers = term.getWires();
		if (!linkers.contains(this)) {
			linkers.add(this);
		}

		if (term1 != null && term2 != null) {
//			电路发生改变
			Set<String> envs = R.findEnvsOn(term1, term2);
			for (String string : envs) {
				R.getR(string).shareVoltage();
			}
//			term1.receivedVoltage(term2);
//			term2.receivedVoltage(term1);
		}
	}

	public void unbind(Terminal term) {
		if (term == null) {
			return;
		}

		term.getWires().remove(this);

		if (term1 != null && term2 != null) {
//			电路发生改变
			Set<String> envs = R.findEnvsOn(term1, term2);

			for (String string : envs) {
				R.getR(string).shareVoltage();
			}
		}

		if (term1 == term) {
			term1 = null;
		} else if (term2 == term) {
			term2 = null;
		}
	}

	public void unbind() {
		if (term1 != null) {
			term1.getWires().remove(this);
		}
		if (term2 != null) {
			term2.getWires().remove(this);
		}

		unbind(term1);
		unbind(term2);
		models = new HashMap<Spatial, Terminal>();
	}

	public Terminal getAnother(Terminal term) {

		if (term1 == null || term2 == null) {
			System.err.println("不可能!!!!!!!!!!!!!!!!! \r\n" + this + "term12 == " + term1 + " , term2=" + term2);
		}

		if (term1 == term) {
			return term2;
		} else if (term2 == term) {
			return term1;
//		} else {
		}
		return null;
	}

	/**
	 * 导线上有电压变化
	 */
	public void voltageChanged(boolean volt) {
		if (term1 == null || term2 == null) {
			return;
		}

//		// 线产生电效果
//		List<Spatial> lineModel = models;
//		if (belongCable != null) {
//			lineModel = belongCable.getModels();
//		}
//		final SimpleApplication app = Dispatcher.getIns().getMainApp();
////		System.out.println("Wire.voltageChanged()"+ lineModel);
//		for (Spatial spatial : lineModel) {
//			if (spatial != null) {
//				if (volt) {
////					System.out.println("高亮显示导线" + this);
//					JmeUtil.setMaterialEffect(app, HightLightType.WIREFRAME, spatial, ColorRGBA.Red, true);
//				} else {
////					System.out.println("取消高亮显示导线" + this);
//					JmeUtil.setMaterialEffect(app, HightLightType.WIREFRAME, spatial, ColorRGBA.Red, false);
//				}
//			}
//		}
	}

	public boolean isBothBinded() {
		return term1 != null && term2 != null;
	}

	public List<Spatial> getLinkMdlByTarget(Terminal target) {
		List<Spatial> linkMdls = new ArrayList<>();
		for (Entry<Spatial, Terminal> model : models.entrySet()) {
			if (model.getValue().equals(target)) {
				linkMdls.add(model.getKey());
			}
		}
		return linkMdls;
	}

	public void setBrokenBreak(boolean brokenBreak) {
		this.brokenBreak = brokenBreak;
		shareTerm(term1);
		shareTerm(term2);
	}

	public boolean isBrokenBreak() {
		return brokenBreak;
	}

	private void shareTerm(Terminal term) {
		Set<String> envs = term.getResidualVolt().keySet();
		R r = null;
		for (String env : envs) {
			r = R.getR(env);
			r.shareVoltage();
		}
	}
}
