package com.cas.circuit.vo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.sim.tis.xml.adapter.Vector3fAdapter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

/**
 * 元器件上的电缆插孔类.<br/>
 * 信号的传递过程:信号通过电缆传递,其本质还是通过电缆中的芯传递的.
 * @author sco_pra
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Jack {
	@XmlAttribute
	private String id;
	/**
	 * 当前插口对应线缆类型
	 */
	@XmlAttribute
	private String cable;
	/**
	 * 当前插口对应线缆的插头制式
	 */
	@XmlAttribute
	private String format;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String module;
	// 插座的方向
	@XmlAttribute
	private String jackDirection;
	// 插座上线连出去的方向
	@XmlAttribute
	private String direction;
	@XmlAttribute
	@XmlJavaTypeAdapter(Vector3fAdapter.class)
	private Vector3f rotation;
	@XmlAttribute
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	private Boolean isPositive;
	@XmlAttribute
	private Integer core;
	@XmlAttribute
	private String belongElecComp;
	@XmlElement(name = "Terminal")
	private List<Terminal> terminalList;

//	一个插孔中包含多个针脚(也可以说是连接头)
//	Key:针脚的编号
//	Value:针脚
	private Map<String, Terminal> stitchMap;

	public void build() {
		stitchMap = terminalList.stream().collect(Collectors.toMap(Terminal::getId, data -> data));
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMdlName() {
		return mdlName;
	}

	public Integer getCore() {
		return core;
	}

	public String getFormat() {
		return format;
	}

	public List<Terminal> getTerminalList() {
		return terminalList;
	}

	public Terminal getStitch(String index) {
		return stitchMap.get(index);
	}

	public boolean hasStitch(Terminal terminal) {
		return stitchMap.values().contains(terminal);
	}

//	private Format format;
////	一个插孔中包含多个针脚(也可以说是连接头)
////	Key:针脚的编号
////	Value:针脚
//	private Map<String, Terminal> stitchMap = new LinkedHashMap<String, Terminal>();
////	该插孔上所插入的电缆线
//	private Plug plug;
////	该插座上的连接的线缆（唯一）
//	private List<ILinker> linkers = new ArrayList<ILinker>();
//	// 默认无故障
//	private String brokenState = CfgConst.BROKEN_STATE_DEFAULT;
//
//	private boolean positive;
//
//	private float[] rotation = new float[3];
//
////	private float flyHeight;
////	private float flyDistance;

	private Spatial model;
//
//	// G信号控制是否停止发送
//	private boolean stopSend;
//
//	private Map<String, String> properties;
//

	public void setModel(Spatial model) {
		this.model = model;
	}

//	@Override
//	protected void toValueObject() {
//		super.toValueObject();
////		FIXME
//		ParserFactory factory = ParserFactory.getFactory("Jack_Cable_Format");
//		if (factory == null) {
//			factory = ParserFactory.createFactory("Jack_Cable_Format");
//		}
//		if (!Util.isEmpty(po.getFormat())) {
//			format = factory.getParser(Format.class, "com/cas/circuit/config/format.xml").getDataMap().get(po.getFormat());
//		}
//		if (!Util.isEmpty(po.getCable())) {
//			Cable cable = factory.getParser(Cable.class, "com/cas/circuit/config/cable.xml").getDataMap().get(po.getCable());
//			linkers.clear();
//			linkers.add(cable);
//		}
//	}
//
//	@Override
//	protected void addChild(BaseVO<?> child) {
//		super.addChild(child);
//		if (child instanceof Terminal) {
//			Terminal terminal = (Terminal) child;
//			stitchMap.put(terminal.getPO().getIndex(), terminal);
//
//			ElecCompDef elecComp = (ElecCompDef) this.parent;
//			terminal.setElecComp(elecComp);
////		} else if (child instanceof SignalMapping) {
////			SignalMapping mapping = (SignalMapping) child;
////			SignalMapping.STITCH_MAP.put(mapping.getStitchName(), mapping);
//////			格式化信号地址
////			SignalMapping.ADDR_MAP.put(CommonAssist.formatSignal(mapping.getPO().getAddress()), mapping);
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.cas.cfg.vo.BaseVO#getLocalKey()
//	 */
//	@Override
//	protected String getLocalKey() {
//		return po.getId();
//	}
//
//	public Format getFormat() {
//		return format;
//	}
//
//	public void setFormat(Format format) {
//		this.format = format;
//	}
//
//	public Map<String, Terminal> getStitch() {
//		return stitchMap;
//	}
//
//	public Plug getPlug() {
//		return plug;
//	}
//
//	public void setPlug(Plug plug) {
//		this.plug = plug;
//	}
//
//	public boolean isPositive() {
//		return positive;
//	}
//
//	public String getBrokenState() {
//		return brokenState;
//	}
//
//	public void setBrokenState(String brokenState) {
//		this.brokenState = brokenState;
//	}
//
//	@Override
//	public void write(JmeExporter ex) throws IOException {
//	}
//
//	@Override
//	public void read(JmeImporter im) throws IOException {
//	}
//
//	public ElecCompDef getElecComp() {
//		return (ElecCompDef) parent;
//	}
//
//	public Spatial getModel() {
//		return model;
//	}
//
//	public void setModel(Spatial model) {
//		if (model == null) {
//			ElecCompDef def = (ElecCompDef) parent;
//			if (!def.isCable()) {
//				log.error("元器件" + parent + "没有找到插口ID为" + po.getId() + "的模型");
////				throw new RuntimeException("元器件" + parent + "没有找到插口ID为" + po.getId() + "的模型");
//			}
//		}
//		this.model = model;
////		FIXME 
////		if (model != null) {
////			model.setUserData(UDKConsts.OBJECT, this);
////		}
//	}
//
//	public boolean isStopSend() {
//		return stopSend;
//	}
//
//	public void setStopSend(boolean stopSend) {
//		this.stopSend = stopSend;
//	}
//	
//	public Map<String, String> getProperties() {
//		return properties;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.cas.cfg.vo.BaseVO#cleanUp()
//	 */
//	@Override
//	protected void cleanUp() {
//		stitchMap = new HashMap<String, Terminal>();
//		linkers = new ArrayList<ILinker>();
//		format = null;
////		该插孔上所插入的电缆线
//		plug = null;
//		model = null;
//	}
//
//	public float[] getRotation() {
//		return rotation;
//	}
//
//	@Override
//	public List<ILinker> getLinkers() {
//		return linkers;
//	}
//
//	@Override
//	public String getDirection() {
//		return po.getDirection();
//	}
//
//	@Override
//	public String getElecCompKey() {
//		return getElecComp().getRef().getLocalKey();
//	}
//
//	@Override
//	public String getTargetKey() {
//		return po.getId();
//	}
////	
////	@Override
////	public String getTargetName() {
////		return po.getName();
////	}
//
//	@Override
//	public JackPO getPO() {
//		return po;
//	}
//
//	@Override
//	public String toString() {
//		return "插孔 [" + po.getName() + "] 制式 [" + po.getFormat() + "]";
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.cas.cfg.vo.BaseVO#clone()
//	 */
//	@Override
//	protected Jack clone() {
//		return (Jack) super.clone();
//	}
}
