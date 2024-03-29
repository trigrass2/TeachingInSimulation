package com.cas.circuit.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.CfgConst;
import com.cas.circuit.ElecCompCPU;
import com.cas.circuit.TermTeam;
import com.cas.circuit.Voltage;
import com.cas.circuit.consts.IOType;
import com.cas.circuit.xml.adapter.AxisAdapter;
import com.cas.circuit.xml.adapter.StringArrayAdapter;
import com.cas.circuit.xml.adapter.VoltageAdapter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

@XmlAccessorType(XmlAccessType.NONE)
public class Terminal implements Savable {
	private static final Logger LOG = LoggerFactory.getLogger(Terminal.class);
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	@XmlJavaTypeAdapter(value = AxisAdapter.class)
	private Vector3f direction;
//	如果该端子是某个插孔上拓展出来的,则在对应插孔中哪一个针脚(编号)
	@XmlAttribute
	private String mark;
	@XmlAttribute
	@XmlJavaTypeAdapter(VoltageAdapter.class)
	private Voltage voltage;
	@XmlAttribute
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	private String[] switchIn;
	@XmlAttribute
	private String type = IOType.BOTH;
	@XmlAttribute
	private String team;
	@XmlAttribute
	private Integer num;// 限制可连接导线的数量，要么是1，要么是2.

//	----------------------------------------------------------

	private ElecCompDef elecCompDef;

	private SwitchCtrl switchCtrl = new SwitchCtrl() {

		@Override
		protected void changeStateIndex(Integer index) {
			// 往复切换state
			if (switchIndex == 0) {
				switchIndex = 1;
			} else {
				switchIndex = 0;
			}
		}

		@Override
		protected ElecCompDef getElecComp() {
			return Terminal.this.elecCompDef;
		}
	};

	private List<VoltageIO> voltIOs = new ArrayList<VoltageIO>();

	private Spatial spatial;

//	如果该端子是在某个插孔中,则当插入电缆后,与之相连接的是哪个端子
	private Terminal contacted;
//	该端子上的连接的导线
	private List<Wire> wires = new ArrayList<>();
//	与该连接头之间存在电阻的连接头
	private Map<Terminal, ResisRelation> resisRelationMap = new HashMap<Terminal, ResisRelation>();

//	电势位Key:电源环境，Value：在该电源环境下，该连接头的电势位
	private Map<String, IP> isopotential = new HashMap<String, IP>();
//	清电势后残余的电势值
	private Map<String, Voltage> residualVolt = new HashMap<String, Voltage>();

	private TermTeam termTeam;

	public Terminal() {
	}

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
		if (parent instanceof ElecCompDef) {
			this.elecCompDef = (ElecCompDef) parent;
		} else if (parent instanceof Jack) {
			this.elecCompDef = ((Jack) parent).getElecCompDef();
		}
	}

	public ElecCompDef getElecCompDef() {
		return elecCompDef;
	}

	public void setElecComp(ElecCompDef elecCompDef) {
		this.elecCompDef = elecCompDef;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
	}

	public String getId() {
		return id;
	}

	public String getMdlName() {
		return mdlName;
	}

	public String getTeam() {
		return team;
	}

	public List<Wire> getWires() {
		return wires;
	}

	public Terminal getContacted() {
		return contacted;
	}

	public void setContacted(Terminal contacted) {
		this.contacted = contacted;
	}

	public void setSpatial(Spatial spatial) {
		if (spatial == null) {
			String errMsg = String.format("没有找到Terminal::ID为%s的模型%s", getId(), mdlName);
			LOG.error(errMsg);
			throw new RuntimeException(errMsg);
		}
		this.spatial = spatial;
		spatial.setUserData("entity", this);
//		if (model != null) {
//			model.setUserData(JmeConst.AXIS_NAME, getAxis());
//			model.setUserData(JmeConst.AXIS_DIR, isPositive());
//			model.setUserData(UDKConsts.OBJECT, this);
//		}
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public Vector3f getDirection() {
		if (direction == null) {
			direction = Vector3f.UNIT_Z;
		}
		return direction;
	}

	public List<VoltageIO> getVoltIOs() {
		return voltIOs;
	}

	public List<VoltageIO> getInputVoltIOs() {
		List<VoltageIO> ret = new ArrayList<VoltageIO>();
		for (VoltageIO voltIO : voltIOs) {
			if (voltIO.getType().contains(CfgConst.SWITCH_CTRL_INPUT)) {
				ret.add(voltIO);
			}
		}
		return ret;
	}

	public boolean hasInputVoltageIO() {
		for (VoltageIO voltIO : voltIOs) {
			if (voltIO.getType().contains(CfgConst.SWITCH_CTRL_INPUT)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOutputVoltageIO() {
		for (VoltageIO voltIO : voltIOs) {
			if (voltIO.getType().contains(CfgConst.SWITCH_CTRL_OUTPUT)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the resisRelationMap
	 */
	public Map<Terminal, ResisRelation> getResisRelationMap() {
		return resisRelationMap;
	}

	/**
	 * @return the isopotential
	 */
	public Map<String, IP> getIsopotential() {
		return isopotential;
	}

	/**
	 * @return the isopotential
	 */
	public IP getIsopotential(String env) {
		return isopotential.get(env);
	}

	/**
	 * @param env
	 * @return
	 */
	public boolean hasIsopotential(String env) {
		return isopotential.containsKey(env) && isopotential.get(env) != null;
	}

	/**
	 * @param env
	 */
	public void addIsopotetial(String env, IP isopo) {
		if (!this.isopotential.containsKey(env)) {
			this.isopotential.put(env, isopo);
//			System.out.println("Terminal.removeIsopotetial()");
//		} else {
//			System.err.println("Terminal.removeIsopotetial() Failure: cause already had" + env);
		}
	}

	/**
	 * @param env
	 */
	public void removeIsopotetial(String env) {
//		System.out.println("Terminal.removeIsopotetial()");

		isopotential.remove(env);
//		if (clearVolt) {
//			removeVolt(env);
//		}
	}

	/**
	 * @param env
	 */
	public void removeVolt(String env) {
		if (residualVolt.containsKey(env)) {
			Voltage voltage = residualVolt.remove(env);
			if (voltage != null) {
				voltage.removeTerminal(this);
			}

			if (elecCompDef != null) {
				System.err.println("---清理 env={" + env + "}" + (elecCompDef.getProxy().getTagName() + "上的 id=" + getId()) + ",后剩余电压: env=" + residualVolt + ">");
			}
			calculateVoltage();
		}
	}

	/**
	 * @param env
	 * @param voltage
	 */
	public void addVolt(Voltage voltage) {
		boolean changed = !residualVolt.containsKey(voltage.getEnv()) || voltage != residualVolt.get(voltage.getEnv());
		residualVolt.put(voltage.getEnv(), voltage);
		voltage.addTermianl(this);
		if (changed) {
			calculateVoltage();
		}
	}

	private void calculateVoltage() {
		boolean hasVolt = residualVolt.size() > 0;

		for (Voltage volt : residualVolt.values()) {
			hasVolt = hasVolt || volt.getValue() > 0;
		}
		if (hasVolt) {
			System.out.println(getName() + "-> 有电");
		} else {
			System.out.println(getName() + "-> 没电");
		}
		if (spatial != null) {
			if (hasVolt) {
//				color(spatial, ColorRGBA.Red, true);
				spatial.setCullHint(CullHint.Dynamic);
			} else {
				spatial.setCullHint(CullHint.Always);
//				uncolor(spatial);
			}
		}
		for (Wire wire : wires) {
			wire.voltageChanged(hasVolt);
		}

		elecCompDef.getLogic().onReceived(this);
	}

	/**
	 * 改变材质颜色
	 * @param sp
	 * @param color
	 */
	public static void color(Spatial sp, ColorRGBA color, boolean saveMat) {
		if (sp == null) {
			return;
		}
		if (sp instanceof Geometry) {
			Material mat = ((Geometry) sp).getMaterial();
			if (saveMat) {
				sp.setUserData("Color", mat);
			}

			Material colorMat = mat.clone();
			MatParam diffuseParam = colorMat.getParam("Diffuse");
			if (diffuseParam != null) {
				colorMat.setColor("Diffuse", color);
				sp.setMaterial(colorMat);
			}
		} else if (sp instanceof Node) {
			for (Spatial child : ((Node) sp).getChildren()) {
				color(child, color, saveMat);
			}
		}
	}

	/**
	 * 恢复材质本来颜色
	 * @param sp
	 */
	public static void uncolor(Spatial sp) {
		if (sp == null) {
			return;
		}
		if (sp instanceof Geometry) {
			Material mat = sp.getUserData("Color");
			if (mat != null) {
				sp.setUserData("Color", null);
				sp.setMaterial(mat);
			}
		} else if (sp instanceof Node) {
			for (Spatial child : ((Node) sp).getChildren()) {
				uncolor(child);
			}
		}
	}

	/**
	 * 
	 */
	public void pulse() {
		BaseElectricCompLogic bec = elecCompDef.getLogic();
		if (bec instanceof ElecCompCPU) {
			((ElecCompCPU) bec).onPulse(this);
		}
	}

	/**
	 * @return the residualVolt
	 */
	public Map<String, Voltage> getResidualVolt() {
		return residualVolt;
	}

	/**
	 * @return the isopotential
	 */
	public Voltage getResidualVolt(String env) {
		return residualVolt.get(env);
	}

	public String getName() {
		return name;
	}

	public String getMark() {
		return mark;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	/**
	 * @return the team
	 */
	public TermTeam getTermTeam() {
		if (termTeam == null) {
			termTeam = new TermTeam(team, this);
		}
		return termTeam;
	}

	/**
	 * @param team the team to set
	 */
	public void setTermTeam(TermTeam termTeam) {
		this.termTeam = termTeam;
	}

	public SwitchCtrl getSwitchCtrl() {
		return switchCtrl;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}

}
