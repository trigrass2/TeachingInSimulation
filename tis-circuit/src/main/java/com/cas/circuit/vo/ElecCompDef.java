package com.cas.circuit.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.ElecCompCPU;
import com.cas.circuit.TermTeam;
import com.cas.circuit.util.MesureResult;
import com.cas.circuit.util.R;
import com.cas.circuit.vo.archive.ElecCompProxy;
import com.cas.circuit.xml.adapter.CompLogicAdapter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Node;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ElecCompDef")
public class ElecCompDef implements Savable {// extends BaseVO<ElecCompDefPO> {
	public static final String PARAM_KEY_SHELL = "shell";

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	/**
	 * key : model【型号】
	 */
	@XmlAttribute
	private String model;
	@XmlAttribute
	private String desc;
//	元器件模型名称
	@XmlAttribute
	private String mdlName;

	@XmlElement(name = "Base")
	private Base base;
	@XmlElement(name = "RelyOn")
	private RelyOn relyOn;

////	元器件标签名
//	@XmlAttribute
//	private String tagName;
	@XmlAttribute(name = "appStateCls")
	@XmlJavaTypeAdapter(CompLogicAdapter.class)
	private BaseElectricCompLogic logic;
	@XmlElement(name = "Param")
	@XmlElementWrapper(name = "Params")
	private List<Param> params = new ArrayList<>();
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	@XmlAttribute
	private Boolean isCable;
	@XmlElement(name = "Terminal")
	private List<Terminal> terminalList = new ArrayList<>();
	@XmlElement(name = "Jack")
	private List<Jack> jackList = new ArrayList<>();
	@XmlElement(name = "Magnetism")
	private List<Magnetism> magnetismList = new ArrayList<>();
	@XmlElement(name = "ResisState")
	private List<ResisState> resisStateList = new ArrayList<>();
//	@XmlElement(name = "BlockState")
//	private List<BlockState> blockStateList = new ArrayList<>();
	@XmlElement(name = "LightIO")
	private List<LightIO> lightIOList = new ArrayList<>();

//	----------------------------------------------------------------------

//	Key:电缆插孔的名字
	private Map<String, Jack> jackMap = new LinkedHashMap<String, Jack>();
//	Key: id
//	存放所有的连接头
	private Map<String, Terminal> terminalMap = new LinkedHashMap<String, Terminal>();
//	Key: id
//	存放所有连接头及插孔中的针脚
	private Map<String, Terminal> termAndStich = new LinkedHashMap<String, Terminal>();

	private Map<String, ResisState> resisStatesMap = new LinkedHashMap<String, ResisState>();
//	key: terminal ID
	private Map<String, List<ResisRelation>> nowResisRelations = new LinkedHashMap<String, List<ResisRelation>>();
//	属性
	private Map<String, String> paramMap;

	private Set<String> createdEnv = null;

//	元器件模型
	private Node spatial;

	private ElecCompProxy proxy;

	public ElecCompDef() {
	}

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		jackMap = jackList.stream().collect(Collectors.toMap(Jack::getId, data -> data));
		terminalMap = terminalList.stream().collect(Collectors.toMap(Terminal::getId, data -> data));
		resisStatesMap = resisStateList.stream().collect(Collectors.toMap(ResisState::getId, data -> data));
//
		termAndStich.putAll(terminalMap);
		jackList.stream().forEach(jack -> termAndStich.putAll(jack.getStitchList().stream().collect(Collectors.toMap(Terminal::getId, Function.identity(), (key1, key2) -> key2, HashMap::new))));

		magnetismList.stream().forEach(mag -> {
			mag.getVoltageIOList().forEach(io -> {
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

		resisStateList.stream().forEach(state -> {
			state.getResisRelationList().forEach(resis -> {
				resis.setTerm1(getTerminal(resis.getTerm1Id()));
				resis.setTerm2(getTerminal(resis.getTerm2Id()));
				if (state.getIsDef() == null) {
					System.out.println("ElecCompDef.build()");
				}
				if (state.getIsDef()) {
					resisRelationAdded(resis);
				}
			});
		});

		terminalList.forEach(t -> t.setElecComp(this));

//		将所有连接头进行分类
//		Key：分类的名称， List<Terminal>一组的连接头
		Map<String, List<Terminal>> groupTerminals = termAndStich.values().stream().filter(t -> t.getTeam() != null).collect(Collectors.groupingBy(Terminal::getTeam));
		groupTerminals.entrySet().forEach(e -> new TermTeam(e.getKey(), e.getValue()));

		paramMap = params.stream().collect(Collectors.toMap(Param::getKey, Param::getValue));

//		------------------
		if (logic == null) {
			logic = new BaseElectricCompLogic();
		}
		logic.setElecComp(this);
		logic.initialize();
	}

	public void bindModel(Node spatial) {
		this.spatial = spatial;
		spatial.setUserData("entity", this);
		
//		遍历元气件中所有插座
		jackList.forEach(jack -> jack.setSpatial(spatial.getChild(jack.getMdlName())));
//		遍历元气件中所有连接头
		terminalList.forEach(t -> t.setSpatial(spatial.getChild(t.getMdlName())));
//		TODO 加入元气件按钮开关...
		magnetismList.forEach(m -> {
			m.getControlIOList().forEach(c -> c.setSpatial(spatial.getChild(c.getMdlName())));
			m.getLightIOList().forEach(l -> l.setSpatial(spatial.getChild(l.getMdlName())));
		});
//		遍历元气件中所有指示灯
		lightIOList.forEach(l -> l.setSpatial(spatial.getChild(l.getMdlName())));
	}

	// 电生磁->磁生电或力3版
	public final void doMagnetism() {
		if (magnetismList.size() == 0) {
			return;
		}
//		System.out.println(ref.getTagName());
//		if ("VC1".equals(ref.getTagName())) {
//			System.out.println(this + "@" + hashCode() + ref.getTagName() + " createdEnv=" + createdEnv);
//		}
		String env_prefix = "";// ref.getCompState().getEquipmentState().getEquipment().getNumber() + ref.getTagName() + ref.hashCode();
		for (Magnetism magnetism : magnetismList) {
			float bili = 0;
			VoltageIO effectVoltageIO = null;
			for (VoltageIO voltageIO : magnetism.getInputVoltageIOs()) {
//				String termIds = voltageIO.getTerm1Id() + "-" + voltageIO.getTerm2Id();
				// 需求电压值和类型
				float requireVolt = voltageIO.getRequireVolt();
				int requireType = voltageIO.getVoltage().getType();
				MesureResult realVolt = R.matchRequiredVolt(requireType, voltageIO.getTerm1(), voltageIO.getTerm2(), requireVolt, voltageIO.getDeviation(), ElecCompCPU.Power_Evn_Filter);
				// 电生磁成功-- 不是自己创建的点才符合接入条件
				if (realVolt != null && (createdEnv == null || !createdEnv.contains(realVolt.getEvn()))) {
					bili = realVolt.getVolt() / requireVolt;
					effectVoltageIO = voltageIO;
					if (!magnetism.isEffect() && voltageIO.getResisStateIds().size() == 2) {
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
					MesureResult checkVolt = R.matchRequiredVolt(outputVoltIO.getVoltage().getType(), outputVoltIO.getTerm1(), outputVoltIO.getTerm2(), outputVoltIO.getRequireVolt() * bili, outputVoltIO.getDeviation(), ElecCompCPU.Power_Evn_Filter);
					if (checkVolt == null) {
						String env = env_prefix + outputVoltIO.getTerm1Id() + "-" + outputVoltIO.getTerm2Id();
//						System.out.println("ElecCompDef.doMagnetism()");
						addCreatedEnv(env);
						r = R.create(env, outputVoltIO.getVoltage().getType(), outputVoltIO.getTerm1(), outputVoltIO.getTerm2(), outputVoltIO.getRequireVolt() * bili);
						r.shareVoltage();
//					}else{
//						if("VC1".equals(ref.getTagName())){
//							System.err.println(outputVoltIO.getVoltType() +":"+outputVoltIO.getTerm1().getResidualVolt() +","+ outputVoltIO.getTerm2().getResidualVolt() +" volt=" + outputVoltIO.getRequireVolt() * bili +"Deviation="+  outputVoltIO.getDeviation());
//						}
					}
				}
				// 磁生力
				for (final ControlIO outputControlIO : magnetism.getOutputControlIOs()) {
					if (ControlIO.INTERACT_PRESS.equals(outputControlIO.getInteract())) {
						if (!magnetism.isEffect() && outputControlIO.getSwitchIndex() == 0) {
							outputControlIO.switchStateChanged(null);
//							FIXME outputControlIO.playMotion(Dispatcher.getIns().getMainApp().getAssetManager(), null);
						}
					}
				}
				// 指示灯
				for (LightIO lightIO : magnetism.getLightIOList()) {
					lightIO.openLight();
				}
				magnetism.setEffect(true);
			} else if (bili == 0 && magnetism.isEffect()) {
				// 不符合接入条件
				// 无磁去力
//				System.err.println(ref.getTagName() + "上不满足输入条件");
				for (final ControlIO outputControlIO : magnetism.getOutputControlIOs()) {
					if (ControlIO.INTERACT_PRESS.equals(outputControlIO.getInteract())) {
						if (magnetism.isEffect() && outputControlIO.getSwitchIndex() == 1) {
//								outputControlIO.doSwitch(0);
							outputControlIO.switchStateChanged(null);
//							FIXME outputControlIO.playMotion(Dispatcher.getIns().getMainApp().getAssetManager(), null);
						}
					}
				}
				if (magnetism.isEffect()) {
					for (VoltageIO inputVoltIO : magnetism.getInputVoltageIOs()) {
						if (inputVoltIO.getResisStateIds().size() == 2) {
							inputVoltIO.doSwitch(0);
						}
					}
				}
				for (VoltageIO outputVoltIO : magnetism.getOutputVoltageIOs()) {
					String powerEnv = env_prefix + outputVoltIO.getTerm1Id() + "-" + outputVoltIO.getTerm2Id();
//					System.out.println(this + "@" + hashCode() + createdEnv);
					if (createdEnv != null && createdEnv.contains(powerEnv)) {
//						if ("VC1".equals(ref.getTagName())) {
//							System.out.println("清除电源 " + powerEnv + "上输出的电压" + powerEnv + System.nanoTime());
//						}
						r = R.getR(powerEnv);
						if (r != null) {
							r.shutPowerDown();
							removeCreatedEvn(powerEnv);
						}
					}
				}
				for (LightIO lightIO : magnetism.getLightIOList()) {
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

	public Map<String, Jack> getJackMap() {
		return jackMap;
	}

	public Map<String, Terminal> getTerminalMap() {
		return terminalMap;
	}

	/**
	 * @param key Terminal::getId
	 */
	public Terminal getTerminal(String key) {
		if (termAndStich.containsKey(key)) {
			return termAndStich.get(key);
		}
		return null;
	}

	public Map<String, ResisState> getResisStatesMap() {
		return resisStatesMap;
	}

	public Map<String, List<ResisRelation>> getNowResisRelations() {
		return nowResisRelations;
	}

	public List<Magnetism> getMagnetismList() {
		return magnetismList;
	}

	public List<LightIO> getLightIOList() {
		return lightIOList;
	}

	public List<ResisState> getResisStateList() {
		return resisStateList;
	}

	public List<Terminal> getTerminalList() {
		return terminalList;
	}

	public List<Jack> getJackList() {
		return jackList;
	}

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
//		if ("VC1".equals(ref.getTagName())) {
//			System.err.println(this + "ElecCompDef.addCreatedEnv()" + createdEnv);
//		}
	}

	public void removeCreatedEvn(String env) {
		getCreatedEnv().remove(env);
//		if ("VC1".equals(ref.getTagName())) {
//			System.err.println(this + "ElecCompDef.removeCreatedEvn()" + createdEnv);
//		}
	}

	/**
	 * @param endTerminal
	 * @param startTerminal
	 */
	public void powerShorted(Terminal startTerminal, Terminal endTerminal) {
//		将对应的短路现象转发给对应的state处理
//		ref.getCompState().powerShorted(startTerminal, endTerminal);
	}

	/**
	 * @return the compState
	 */
	public BaseElectricCompLogic getLogic() {
		return logic;
	}

	public boolean isCable() {
		return isCable;
	}

	public String getModel() {
		return model;
	}

	public String getMdlName() {
		return mdlName;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getParam(String key) {
		return paramMap.get(key);
	}

	public Node getSpatial() {
		return spatial;
	}

	public void setProxy(ElecCompProxy proxy) {
		this.proxy = proxy;
	}

	public Base getBase() {
		return base;
	}

	public RelyOn getRelyOn() {
		return relyOn;
	}

	@Nonnull
	public ElecCompProxy getProxy() {
		if (proxy == null) {
			proxy = new ElecCompProxy();
		}
		return proxy;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}

}
