package com.cas.circuit.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.circuit.CfgConst;
import com.cas.circuit.Plug;
import com.cas.circuit.xml.adapter.Vector3fRotAdapter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

/**
 * 元器件上的电缆插孔类.<br/>
 * 信号的传递过程:信号通过电缆传递,其本质还是通过电缆中的芯传递的.
 * @author sco_pra
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Jack implements Savable {
	private static final Logger LOG = LoggerFactory.getLogger(Jack.class);
	@XmlAttribute
	private String id;
//	/**
//	 * 当前插口对应线缆类型
//	 */
//	@XmlAttribute
//	private String cable;
	/**
	 * 当前插口对应线缆的插头制式
	 */
	@XmlAttribute
	private String format;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	private String name;
	// 插座的方向
	@XmlAttribute
	private String jackDirection;
	// 插座上线连出去的方向
	@XmlAttribute
	private String direction;
	@XmlAttribute
	@XmlJavaTypeAdapter(Vector3fRotAdapter.class)
	private Vector3f rotation;
	@XmlAttribute
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	private Boolean isPositive;
	@XmlAttribute
	private Integer core;
	@XmlAttribute
	private String belongElecComp;
	@XmlElement(name = "Terminal")
	private List<Stitch> stitchList = new ArrayList<>();
	@XmlElement(name = "Param")
	@XmlElementWrapper(name = "Params")
	private List<Param> params = new ArrayList<>();

//	------------------------------

//	该插孔上所插入的电缆线
	private Plug plug;
//	该插座上的连接的线缆（唯一）
	private Cable cable;
	// 默认无故障
	private String brokenState = CfgConst.BROKEN_STATE_DEFAULT;
//	Key: Stitch::getIndex
	private Map<Integer, Stitch> stitchMap;

	private boolean positive;

	private Spatial spatial;

	// G信号控制是否停止发送
	private boolean stopSend;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMdlName() {
		return mdlName;
	}

	public List<Stitch> getStitchList() {
		return stitchList;
	}

	@Nullable
	public Stitch getStitch(Integer index) {
		return stitchMap.get(index);
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		stitchMap = stitchList.stream().collect(Collectors.toMap(Stitch::getIndex, data -> data));
	}

	public void setPlug(Plug plug) {
		this.plug = plug;
	}

	public Plug getPlug() {
		return plug;
	}

	public boolean isPositive() {
		return positive;
	}

	public String getBrokenState() {
		return brokenState;
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		if (spatial == null) {
			LOG.error("没有找到Jack::ID为{}的模型{}", getId(), mdlName);
		}
		this.spatial = spatial;
		spatial.setUserData("entity", this);
	}

	public boolean isStopSend() {
		return stopSend;
	}

	public void setStopSend(boolean stopSend) {
		this.stopSend = stopSend;
	}

	public Cable getCable() {
		return cable;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return "插孔 [" + name + "] 制式 [" + format + "]";
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void read(JmeImporter im) throws IOException {
		// TODO Auto-generated method stub

	}
}
