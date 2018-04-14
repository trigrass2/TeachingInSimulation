package com.cas.circuit.vo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.cas.circuit.util.R;
import com.cas.circuit.vo.archive.WireProxy;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;

/**
 * 导线
 * @author sco_pra
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Wire implements Savable{
	@XmlAttribute
//	导线的两个连接头
	private int stitch1;
	@XmlAttribute
	private int stitch2;
	@XmlAttribute
	private String mark;

//	@XmlAttribute
//	private Float width;
//	@XmlAttribute
//	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
//	private ColorRGBA color;

	private Terminal term1;
	private Terminal term2;

	private boolean brokenBreak = false;

	private String wireNum;

	private Spatial spatial;

	private WireProxy proxy;

	public Wire() {
	}

	public void build() {
//		需要手动创建,这里的terminal是线缆插头上的针孔.
		parseStitch(stitch1);
		parseStitch(stitch2);
	}

	private void parseStitch(int stitchNo) {
		bind(new Stitch(stitchNo));
	}

	/**
	 * 将导线绑到连接头上
	 */
	public void bind(Terminal term) {
		if (isBothBinded() || term == null) {
			return;
		}
//		System.out.println(this + "Wire.bind()" +  term);
		if (term1 == null) {
			term1 = term;
		} else if (term2 == null) {
			term2 = term;
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
//		models = new HashMap<Spatial, ILinkTarget>();
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

	public Terminal getTerm1() {
		return term1;
	}

	public Terminal getTerm2() {
		return term2;
	}

//	public List<Spatial> getLinkMdlByTarget(Terminal target) {
//		List<Spatial> linkMdls = new ArrayList<Spatial>();
//		for (Entry<Spatial, Terminal> model : models.entrySet()) {
//			if (model.getValue().equals(target)) {
//				linkMdls.add(model.getKey());
//			}
//		}
//		return linkMdls;
//	}

	public String getWireNum() {
		return wireNum;
	}

	public void setWireNum(String wireNum) {
		this.wireNum = wireNum;
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
		spatial.setUserData("entity", this);
	}

	@Override
	public String toString() {
		return "Wire [term1=" + term1 + ", term2=" + term2 + "]" + hashCode();
	}

	public boolean isBrokenBreak() {
		return brokenBreak;
	}

	public void setBrokenBreak(boolean brokenBreak) {
		this.brokenBreak = brokenBreak;
		shareTerm(term1);
		shareTerm(term2);
	}

	private void shareTerm(Terminal term) {
		Set<String> envs = term.getResidualVolt().keySet();
		R r = null;
		for (String env : envs) {
			r = R.getR(env);
			r.shareVoltage();
		}
	}

	@Nonnull
	public WireProxy getProxy() {
		if (proxy == null) {
			proxy = new WireProxy();
		}
		return proxy;
	}

	public void setProxy(WireProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}

}
