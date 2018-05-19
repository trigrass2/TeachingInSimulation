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

import lombok.Getter;
import lombok.Setter;

/**
 * 元器件上的电缆插孔类.<br/>
 * 信号的传递过程:信号通过电缆传递,其本质还是通过电缆中的芯传递的.
 * @author sco_pra
 */
@Getter
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
	private ElecCompDef elecCompDef;

//	该插孔上所插入的电缆线
	@Setter
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
	@Setter
	private boolean stopSend;

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
		this.elecCompDef = (ElecCompDef) parent;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		stitchMap = stitchList.stream().collect(Collectors.toMap(Stitch::getIndex, data -> data));
	}

	@Nullable
	public Stitch getStitch(Integer index) {
		return stitchMap.get(index);
	}

	public void setSpatial(Spatial spatial) {
		if (spatial == null) {
			String errMsg = String.format("没有找到Jack::ID为%s的模型%s", getId(), mdlName);
			LOG.error(errMsg);
			throw new RuntimeException(errMsg);
		}
		this.spatial = spatial;
		spatial.setUserData("entity", this);
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}
}
