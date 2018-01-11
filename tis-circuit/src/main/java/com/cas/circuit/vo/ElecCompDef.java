package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.ElecCompCPU;
import com.cas.circuit.TermTeam;
import com.cas.circuit.util.MesureResult;
import com.cas.circuit.util.R;
import com.cas.gas.vo.BlockState;
import com.cas.gas.vo.GasPort;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

/**
 * key : model【型号】
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ElecCompDef {

	// 元气件名称
	@XmlAttribute
	private String name;
	// 元气件型号
	@XmlAttribute
	private String model;
	// 元气件简介
	@XmlAttribute
	private String desc;
	// 元气件的处理逻辑类
	@XmlAttribute
	private String appStateCls;
	// BaseVO中规定的属性用来保存标签的文本节点内容
	@XmlAttribute
	private String elementText;
	//
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	@XmlAttribute
	private Boolean isCable;
	@XmlElement(name = "Terminal")
	private List<Terminal> terminalList = new ArrayList<>();
	@XmlElement(name = "Jack")
	private List<Jack> jackList = new ArrayList<>();
	@XmlElement(name = "Magnetism")
	private List<Magnetism> magnetisms = new ArrayList<>();
	@XmlElement(name = "ResisState")
	private List<ResisState> resisStates = new ArrayList<>();
	@XmlElement(name = "BlockState")
	private List<BlockState> blockStates = new ArrayList<>();
	@XmlElement(name = "LightIO")
	private List<LightIO> lightIOs = new ArrayList<>();
	// Key:电缆插孔的名字
	// Value:电缆插孔
	private Map<String, Jack> jackMap = new HashMap<>();
//	Key ResisState:id
	private Map<String, ResisState> resisStatesMap = new HashMap<>();
//	BlockState:id
	private Map<String, BlockState> blockStatesMap = new HashMap<>();
	// Key: id
	// Value:Terminal
	// 存放所有的连接头
	private Map<String, Terminal> terminalMap = new HashMap<>();
	// Key: id
	// Value:Terminal
	// 存放所有连接头及插孔中的针脚
	private Map<String, Terminal> termAndStich = new HashMap<>();
//	// Key: id
//	// Value: GasPort
//	// 存放所有的气口
//	private Map<String, GasPort> gasPortMap;

	// key: terminal ID
	private Map<String, List<ResisRelation>> nowResisRelations = new HashMap<>();

	private Map<String, String> properties;

	private Set<String> createdEnv = null;
	//
	private ElecComp ref;

	private BaseElectricCompLogic compLogic;

	public ElecCompDef() {
		// System.err.println("def:::" + this);
	}

	public void build() {
		jackMap = jackList.stream().collect(Collectors.toMap(Jack::getId, data -> data));
		terminalMap = terminalList.stream().collect(Collectors.toMap(Terminal::getId, data -> data));
		resisStatesMap = resisStates.stream().collect(Collectors.toMap(ResisState::getId, data -> data));
		blockStatesMap = blockStates.stream().collect(Collectors.toMap(BlockState::getId, data -> data));
//
		termAndStich.putAll(terminalMap);
		jackList.stream().forEach(jack -> {
			termAndStich.putAll(jack.getTerminalList().stream().collect(Collectors.toMap(Terminal::getId, Function.identity(), (key1, key2) -> key2, HashMap::new)));
		});

		magnetisms.stream().forEach(mag -> {
			mag.getVoltageIOs().forEach(io -> {
				Terminal term1 = getTerminal(io.getTerm1Id());
				Terminal term2 = getTerminal(io.getTerm2Id());
				if (term1 == null) {
					throw new NullPointerException(name + "里的" + io.getTerm1Id() + "端子没找到");
				} else {
					term1.getVoltIOs().add(io);
					io.setTerm1(term1);
				}
				if (term2 == null) {
					throw new NullPointerException(name + "里的" + io.getTerm2Id() + "端子没找到");
				} else {
					term2.getVoltIOs().add(io);
					io.setTerm2(term2);
				}
			});
		});

		resisStates.stream().forEach(state -> {
			state.getResisRelationList().forEach(resis -> {
				resis.setTerm1(getTerminal(resis.getTerm1Id()));
				resis.setTerm2(getTerminal(resis.getTerm2Id()));
				if (state.getIsDef() == null) {
					System.out.println("ElecCompDef.build()" + state.getIsDef());
				}
				if (state.getIsDef()) {
					resisRelationAdded(resis);
				}
			});
		});
//		blockStates.stream().forEach(state->{
//			state.getBlockRelationList().forEach(resis->{
//				resis.setPort1(getGasPort(resis.getPortId1()));
//				resis.setPort2(getGasPort(resis.getPortId2()));
//				if (state.getIsDef()) {
//					resisRelationAdded(resis);
//				}
//			});
//		});

//		将所有连接头进行分类
//		Key：分类的名称， List<Terminal>一组的连接头
		Map<String, List<Terminal>> groupTerminals = termAndStich.values().stream().filter(t -> t.getTeam() != null).collect(Collectors.groupingBy(Terminal::getTeam));
		groupTerminals.entrySet().forEach(e -> {
			new TermTeam(e.getKey(), e.getValue());
		});

//		FIXME
//		if (appStateCls == null || "".equals(appStateCls)) {
//			compLogic = new BaseElectricCompLogic();
//		} else {
//			try {
//				Class<?> clazz = Class.forName(appStateCls);
//				if (clazz.isAssignableFrom(BaseElectricCompLogic.class)) {
//					compLogic = (BaseElectricCompLogic) clazz.newInstance();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

	public Terminal getTerminal(String key) {
		if (termAndStich.containsKey(key)) {
			return termAndStich.get(key);
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public List<Magnetism> getMagnetisms() {
		return magnetisms;
	}

	public List<LightIO> getLightIOs() {
		return lightIOs;
	}

//	public Map<String, ResisState> getResisStatesMap() {
//		return resisStatesMap;
//	}

	public ResisState getResisState(String resisStateId) {
		return resisStatesMap.get(resisStateId);
	}

//	public Map<String, Jack> getJackMap() {
//		return jackMap;
//	}

	public Jack getJack(String jackId) {
		return jackMap.get(jackId);
	}

	public Map<String, Terminal> getTerminalMap() {
		return terminalMap;
	}

//	public GasPort getGasPort(String key) {
//		if (gasPortMap.containsKey(key)) {
//			return gasPortMap.get(key);
//		}
//		return null;
//	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	// 电生磁->磁生电或力3版
	public final void doMagnetism() {
		if (magnetisms.size() == 0) {
			return;
		}
		String env_prefix = "";
		for (Magnetism magnetism : magnetisms) {
			float bili = 0;
			VoltageIO effectVoltageIO = null;
			for (VoltageIO voltageIO : magnetism.getInputVoltageIOs()) {
//				String termIds = voltageIO.getPO().getTerm1Id() + "-" + voltageIO.getPO().getTerm2Id();
//				需求电压值和类型
				float requireVolt = voltageIO.getValue();
				int requireType = voltageIO.getVoltType();
				MesureResult realVolt = R.matchRequiredVolt(requireType, voltageIO.getTerm1(), voltageIO.getTerm2(), requireVolt, voltageIO.getDeviation(), ElecCompCPU.Power_Evn_Filter);
				// 电生磁成功-- 不是自己创建的点才符合接入条件
				if (realVolt != null && (createdEnv == null || !createdEnv.contains(realVolt.getEvn()))) {
					bili = realVolt.getVolt() / requireVolt;
					effectVoltageIO = voltageIO;
					if (!magnetism.isEffect() && voltageIO.getSwitchIn().size() == 2) {
						voltageIO.doSwitch(1);
					}
					break;
				}
			}
			R r = null;
			if (bili > 0 && !magnetism.isEffect()) {
				// 磁生电 -- 找出output的VoltageIO
				List<VoltageIO> outputVoltIOs = new ArrayList<VoltageIO>();
				outputVoltIOs.addAll(magnetism.getOutputVoltageIOs());
				outputVoltIOs.remove(effectVoltageIO);
				for (VoltageIO outputVoltIO : outputVoltIOs) {
					MesureResult checkVolt = R.matchRequiredVolt(outputVoltIO.getVoltType(), outputVoltIO.getTerm1(), outputVoltIO.getTerm2(), outputVoltIO.getValue() * bili, outputVoltIO.getDeviation(), ElecCompCPU.Power_Evn_Filter);
					if (checkVolt == null) {
						String env = env_prefix + outputVoltIO.getTerm1Id() + "-" + outputVoltIO.getTerm2Id();
						addCreatedEnv(env);
						r = R.create(env, outputVoltIO.getVoltType(), outputVoltIO.getTerm1(), outputVoltIO.getTerm2(), outputVoltIO.getValue() * bili);
						r.shareVoltage();
						// }else{
						// if("VC1".equals(ref.getPO().getTagName())){
						// System.err.println(outputVoltIO.getVoltType()
						// +":"+outputVoltIO.getTerm1().getResidualVolt() +","+
						// outputVoltIO.getTerm2().getResidualVolt() +" volt=" +
						// outputVoltIO.getRequireVolt() * bili +"Deviation="+
						// outputVoltIO.getDeviation());
						// }
					}
				}
				// 磁生力
				for (final ControlIO outputControlIO : magnetism.getOutputControlIOs()) {
					if (ControlIO.INTERACT_PRESS.equals(outputControlIO.getInteract())) {
						if (!magnetism.isEffect() && outputControlIO.getSwitchIndex() == 0) {
							outputControlIO.switchStateChanged(null, null);
							// FIXME
							// outputControlIO.playMotion(Dispatcher.getIns().getMainApp().getAssetManager(),
							// null);
						}
					}
				}
				// 指示灯
				for (LightIO lightIO : magnetism.getLightIOs()) {
					lightIO.openLight();
				}
				magnetism.setEffect(true);
			} else if (bili == 0 && magnetism.isEffect()) {
				// 不符合接入条件
				// 无磁去力
				// System.err.println(ref.getPO().getTagName() + "上不满足输入条件");
				for (final ControlIO outputControlIO : magnetism.getOutputControlIOs()) {
					if (ControlIO.INTERACT_PRESS.equals(outputControlIO.getInteract())) {
						if (magnetism.isEffect() && outputControlIO.getSwitchIndex() == 1) {
							// outputControlIO.doSwitch(0);
							outputControlIO.switchStateChanged(null, null);
							// FIXME
							// outputControlIO.playMotion(Dispatcher.getIns().getMainApp().getAssetManager(),
							// null);
						}
					}
				}
				if (magnetism.isEffect()) {
					for (VoltageIO inputVoltIO : magnetism.getInputVoltageIOs()) {
						if (inputVoltIO.getSwitchIn().size() == 2) {
							inputVoltIO.doSwitch(0);
						}
					}
				}
				for (VoltageIO outputVoltIO : magnetism.getOutputVoltageIOs()) {
					String powerEnv = env_prefix + outputVoltIO.getTerm1Id() + "-" + outputVoltIO.getTerm2Id();
					// System.out.println(this + "@" + hashCode() + createdEnv);
					if (createdEnv != null && createdEnv.contains(powerEnv)) {
						// if ("VC1".equals(ref.getPO().getTagName())) {
						// System.out.println("清除电源 " + powerEnv + "上输出的电压" + powerEnv +
						// System.nanoTime());
						// }
						r = R.getR(powerEnv);
						if (r != null) {
							r.shutPowerDown();
							removeCreatedEvn(powerEnv);
						}
					}
				}
				for (LightIO lightIO : magnetism.getLightIOs()) {
					lightIO.closeLight();
				}
				magnetism.setEffect(false);
			}
		}
	}

	/**
	 * 元器件内部电阻状态改变
	 * @param resisState 发生变化的电阻状态
	 */
	public void resisRelationAdded(ResisRelation relation) {
		relation.setActivated(true);
		Terminal term1 = relation.getTerm1();
		Terminal term2 = relation.getTerm2();
		try {
			term1.getResisRelationMap().put(term2, relation);
			term2.getResisRelationMap().put(term1, relation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resisRelationRemoved(ResisRelation relation) {
		relation.setActivated(false);
		Terminal term1 = relation.getTerm1();
		Terminal term2 = relation.getTerm2();
		term1.getResisRelationMap().remove(term2);
		term2.getResisRelationMap().remove(term1);
	}

	public void brokenResisRelationAdded(ResisRelation relation) {
		if (!relation.isActivated()) {
			Terminal term1 = relation.getTerm1();
			Terminal term2 = relation.getTerm2();
			term1.getResisRelationMap().put(term2, relation);
			term2.getResisRelationMap().put(term1, relation);
		}
	}

	public void brokenResisRelationRemoved(ResisRelation relation) {
		if (!relation.isActivated()) {
			Terminal term1 = relation.getTerm1();
			Terminal term2 = relation.getTerm2();
			term1.getResisRelationMap().remove(term2);
			term2.getResisRelationMap().remove(term1);
		}
	}

//	public void blockRelationAdded(BlockRelation relation) {
//		relation.setActivated(true);
//		GasPort port1 = relation.getPort1();
//		GasPort port2 = relation.getPort2();
//		port1.getBlockRelationMap().put(port2, relation);
//		port2.getBlockRelationMap().put(port1, relation);
//	}
//
//	public void blockRelationRemoved(BlockRelation relation) {
//		relation.setActivated(false);
//		GasPort port1 = relation.getPort1();
//		GasPort port2 = relation.getPort2();
//		port1.getBlockRelationMap().remove(port2);
//		port2.getBlockRelationMap().remove(port1);
//	}
//
//	public GasPort getGasPort(String key) {
//		if (gasPortMap.containsKey(key)) {
//			return gasPortMap.get(key);
//		}
//		return null;
//	}
//
//	/**
//	 * @return
//	 */
//	public Map<String, String> getProperties() {
//		if (properties == null) {
//			properties = getStringMap(po.getElementText());
//		}
//		return properties;
//	}

	/**
	 * @return the createdEnv
	 */
	public Set<String> getCreatedEnv() {
		if (createdEnv == null) {
			createdEnv = new HashSet<String>();
		}
		return createdEnv;
	}

	public void addCreatedEnv(String env) {
		getCreatedEnv().add(env);
		// if ("VC1".equals(ref.getPO().getTagName())) {
		// System.err.println(this + "ElecCompDef.addCreatedEnv()" + createdEnv);
		// }
	}

	public void removeCreatedEvn(String env) {
		getCreatedEnv().remove(env);
		// if ("VC1".equals(ref.getPO().getTagName())) {
		// System.err.println(this + "ElecCompDef.removeCreatedEvn()" + createdEnv);
		// }
	}

	/**
	 * @param endTerminal
	 * @param startTerminal
	 */
	public void powerShorted(Terminal startTerminal, Terminal endTerminal) {
		// 将对应的短路现象转发给对应的state处理
		// ref.getCompState().powerShorted(startTerminal, endTerminal);
	}
}
