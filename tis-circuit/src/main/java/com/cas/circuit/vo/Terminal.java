package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.ElecCompCPU;
import com.cas.circuit.TermTeam;
import com.cas.circuit.Voltage;
import com.cas.circuit.consts.IOType;
import com.cas.sim.tis.xml.adapter.VoltageAdapter;
import com.jme3.scene.Spatial;

@XmlAccessorType(XmlAccessType.NONE)
public class Terminal {// implements ILinkTarget {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	private String direction;
//	如果该端子是某个插孔上拓展出来的,则在对应插孔中哪一个针脚(编号)
	@XmlAttribute
	private Integer index;
	@XmlAttribute
	private String mark;
	@XmlAttribute
	@XmlJavaTypeAdapter(VoltageAdapter.class)
	private Voltage voltage;
	@XmlList
	private List<String> switchIn;
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String team;
	@XmlAttribute
	private Integer num;// 限制可连接导线的数量，要么是1，要么是2.

	public Terminal() {
	}

	public Terminal(int index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public Integer getIndex() {
		return index;
	}

	public String getMark() {
		return mark;
	}

	public String getName() {
		return name;
	}

	public String getTeam() {
		return team;
	}

	public String getMdlName() {
		return mdlName;
	}

	public String getType() {
		if (type == null) {
			return IOType.BOTH;
		}
		return type;
	}

	private Spatial model;

	private List<VoltageIO> voltIOs = new ArrayList<VoltageIO>();
//	与该连接头之间存在电阻的连接头
	private Map<Terminal, ResisRelation> resisRelationMap = new HashMap<Terminal, ResisRelation>();

	private TermTeam termTeam;

	private ElecCompDef elecComp;

//	如果该端子是在某个插孔中,则当插入电缆后,与之相连接的是哪个端子
	private Terminal contacted;
//	该端子上的连接的导线
	private List<Wire> wires = new ArrayList<>();
//
//	电势位Key:电源环境，Value：在该电源环境下，该连接头的电势位
	private Map<String, IP> isopotential = new HashMap<String, IP>();
//	清电势后残余的电势值
	private Map<String, Voltage> residualVolt = new HashMap<String, Voltage>();

//
	public List<VoltageIO> getVoltIOs() {
		return voltIOs;
	}

	public ElecCompDef getElecComp() {
		return elecComp;
	}
	
	public Terminal getContacted() {
		return contacted;
	}
//	private String axis;
//
//	private boolean positive;
//
//	public Terminal() {
//		super();
//	}
//
//	public Terminal(TerminalPO po) {
//		super(po);
//	}
//

//	@Override
//	protected void toValueObject() {
//		super.toValueObject();
//		if (!Util.isEmpty(po.getDirection())) {
//			axis = po.getDirection().substring(0, 1);
//			positive = '+' == po.getDirection().charAt(1);
//		}
//	}
//
//	@Override
//	protected void changeStateIndex(Integer index) {
//		// 往复切换state
//		if (switchIndex == 0) {
//			switchIndex = 1;
//		} else {
//			switchIndex = 0;
//		}
//	}
//
//	public Terminal getContacted() {
//		return contacted;
//	}
//
//	public void setContacted(Terminal contacted) {
//		this.contacted = contacted;
//	}
//
//	public boolean isPositive() {
//		return positive;
//	}
//
//	public String getAxis() {
//		return axis;
//	}
//
	public void setModel(Spatial model) {
		if (model == null) {
//			log.error("元器件" + parent + "没有找到连接头ID为" + po.getId() + "的模型");
		}
		this.model = model;

//		if (model != null) {
//			model.setUserData(JmeConst.AXIS_NAME, getAxis());
//			model.setUserData(JmeConst.AXIS_DIR, isPositive());
//			model.setUserData(UDKConsts.OBJECT, this);
//		}
	}

//	public List<VoltageIO> getVoltIOs() {
//		return voltIOs;
//	}
//
//	public List<VoltageIO> getInputVoltIOs() {
//		List<VoltageIO> ret = new ArrayList<VoltageIO>();
//		for (VoltageIO voltIO : voltIOs) {
//			if (voltIO.getPO().getType().contains(CfgConst.SWITCH_CTRL_INPUT)) {
//				ret.add(voltIO);
//			}
//		}
//		return ret;
//	}
//
//	public boolean hasInputVoltageIO() {
//		for (VoltageIO voltIO : voltIOs) {
//			if (voltIO.getPO().getType().contains(CfgConst.SWITCH_CTRL_INPUT)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean hasOutputVoltageIO() {
//		for (VoltageIO voltIO : voltIOs) {
//			if (voltIO.getPO().getType().contains(CfgConst.SWITCH_CTRL_OUTPUT)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
	public List<Wire> getWires() {
		return wires;
	}

	public Map<Terminal, ResisRelation> getResisRelationMap() {
		return resisRelationMap;
	}

	public Map<String, Voltage> getResidualVolt() {
		return residualVolt;
	}

	/**
	 * @return the isopotential
	 */
	public IP getIsopotential(String env) {
		return isopotential.get(env);
	}

	public Map<String, IP> getIsopotential() {
		return isopotential;
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
//			if (elecComp != null) {
//				System.err.println("---清理 env={" + env + "}" + (elecComp.getRef().getPO().getTagName() + "上的 id=" + po.getId()) + ",后剩余电压: env=" + residualVolt + ">");
//			} else {
//				if (parent != null && parent instanceof Jack) {
//					System.out.println("---清理 env={" + env + "}" + (parent + "上的 id=" + po.getId()) + ",后剩余电压: env=" + residualVolt + ">");
//				}
//			}
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

//		FIXME
//		if (model != null) {
//			if (hasVolt) {
//				FilterUtil.setSpatialElectrical(model, true);
//			} else {
//				FilterUtil.setSpatialElectrical(model, false);
//			}
//		}
		for (Wire wire : wires) {
			wire.voltageChanged(hasVolt);
		}
		if (parent != null) {
//			线缆中的连接头
			if (parent instanceof ElecCompDef) {
				elecComp.getRef().getCompLogic().onReceived(this);
			} else if (parent instanceof Jack) {
				ElecCompDef stitchElecComp = (ElecCompDef) parent.getParent();
				stitchElecComp.getRef().getCompLogic().onReceived(this);
			}
		}
	}

	/**
	 * 
	 */
	public void pulse() {
		if (parent != null) {
			BaseElectricCompLogic bec = null;
			if (parent instanceof ElecCompDef) {
				bec = elecComp.getRef().getCompLogic();
			} else if (parent instanceof Jack) {
				ElecCompDef stitchElecComp = (ElecCompDef) parent.getParent();
				bec = stitchElecComp.getRef().getCompLogic();
			}
			if (bec != null) {
				if (bec instanceof ElecCompCPU) {
					((ElecCompCPU) bec).onPulse(this);
				}
			}
		}
	}

	/**
	 * @return the isopotential
	 */
	public Voltage getResidualVolt(String env) {
		return residualVolt.get(env);
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
//	@Override
//	public String getElecCompKey() {
//		return elecComp.getRef().getLocalKey();
//	}
}
