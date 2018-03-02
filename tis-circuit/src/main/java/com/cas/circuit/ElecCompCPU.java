package com.cas.circuit;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cas.circuit.util.MultiPower;
import com.cas.circuit.util.R;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Jack;
import com.cas.circuit.vo.Terminal;

public abstract class ElecCompCPU extends BaseElectricCompLogic {
	public static final float CB_SIGNAL_VOLT = 24;
	public static final float JF_SIGNAL_VOLT = 5;
	public static final float COP_SIGNAL_VOLT = 24;

	public static final String Power_Evn_Filter = MultiPower.SIGNAL + "=false";
	public static final String Signal_Evn_Filter = MultiPower.SIGNAL + "=true";

//	默认元器件无法工作
	protected boolean workable;
//	每一个插孔提供的电压环境前缀,防止使用多台机床时数据混乱
	protected String EVN_NAME_PRE;

	protected List<Jack> cbx_jacks = new ArrayList<Jack>();
	protected List<String> cb_evn_names = new ArrayList<String>();
	protected List<Jack> jf_jacks = new ArrayList<Jack>();
	protected List<String> jf_evn_names = new ArrayList<String>();
	protected List<Jack> cop_jacks = new ArrayList<Jack>();
	protected List<String> cop_evn_names = new ArrayList<String>();
	protected List<Jack> jfr_jacks = new ArrayList<Jack>();
	protected List<String> jfr_evn_names = new ArrayList<String>();
	protected int[][] jfrEnvEnds = new int[][] { { 1, 11 }, { 3, 13 }, { 5, 15 } };

	public ElecCompCPU() {
		EVN_NAME_PRE = String.valueOf(hashCode());
	}

	/*
	 * (non-Javadoc)
	 * @see com.cas.me.appState.electric.BaseElectricCompState#setElecComp(com.cas.me.data.layout.ElecComp)
	 */
	@Override
	public void setElecComp(ElecCompDef elecComp) {
		super.setElecComp(elecComp);
		Map<String, Jack> jackMap = elecComp.getJackMap();
		for (Jack jack : jackMap.values()) {
			if ("JF".equals(jack.getFormat())) {
				jf_evn_names.add(EVN_NAME_PRE + jack.getName());
				jf_jacks.add(jack);
			} else if ("CB".equals(jack.getFormat())) {
				cb_evn_names.add(EVN_NAME_PRE + jack.getName());
				cbx_jacks.add(jack);
			} else if ("COP".equals(jack.getFormat())) {
				cop_evn_names.add(EVN_NAME_PRE + jack.getName());
				cop_jacks.add(jack);
			} else if (jack.getFormat().indexOf("JFR") != -1) {
				jfr_evn_names.add(EVN_NAME_PRE + jack.getName());
				jfr_jacks.add(jack);
			} else {
				LOG.info("这个插孔{}不能产生信号电,关于'产生信号电'不理解的,可以问振宇", jack.getFormat());
			}
		}
	}

	/**
	 * @param terminal
	 */
	public final void onPulse(Terminal terminal) {
		TermTeam team = terminal.getTermTeam();
		team.signIn(terminal);
		if (!team.resetIfReady()) {
			return;
		}
		onPulseLocal(terminal);
	}

	/**
	 * @param terminal
	 */
	protected void onPulseLocal(Terminal terminal) {

	}

	public Map<String, Jack> getJackMap() {
		return getElecComp().getJackMap();
	}

	/**
	 * @param jackNm 信号发生的插孔名称
	 * @param index 具体的针脚号
	 */
	public Terminal getStitch(String jackName, int index) {
		Jack jack = getJackMap().get(jackName);
		if (jack == null) {
			String errMsg = MessageFormat.format("没有找到名称为: {0}的插孔", jackName);
			LOG.warn(errMsg);
			throw new RuntimeException(errMsg);
		}
		Terminal stitch = jack.getStitch(index);
		if (stitch == null) {
			String errMsg = MessageFormat.format("插孔{0}中没有找到第{1}跟针脚", jackName, index);
			LOG.warn(errMsg);
			throw new RuntimeException(errMsg);
		}
		return stitch;
	}

	/**
	 * @param jackNm 信号发生的插孔名称
	 * @param startIndex 具体的针脚号
	 * @param endIndex 具体的针脚号
	 * @param sign 电压对象
	 */
	public void sendSignal(String jackNm, int startIndex, int endIndex, CommandSignal sign) {
		synchronized (this) {
			// 判断当前插口是否允许发送指令
			Jack jack = getJackMap().get(jackNm);
			if (jack.isStopSend()) {
				LOG.warn("无法发送信号[{}]!!", jackNm);
				return;
			}
			Terminal startTerminal = getStitch(jackNm, startIndex);
			Terminal endTerminal = getStitch(jackNm, endIndex);
			String env = EVN_NAME_PRE + jackNm;
			R r = R.getR(env);
			if (r == null) {
				r = R.createSignal(env, startTerminal, endTerminal, 24);
				r.shareVoltage();
			}

			SignalVolt sv = (SignalVolt) r.getVoltage();
			sv.changeCommandSignal(sign);
			r.shareVoltage();
		}
	}

	/**
	 * 开元器件指示灯<br>
	 * 产生信号电<br>
	 */
	protected void elecCompStart() {
		LOG.info("开始启动" + elecComp.getTagName());
//		System.err.println(this.getClass().getCanonicalName() + ".elecCompStart(start...)");
		getElecComp().getLightIOList().forEach(l -> l.openLight());
//		信号电压起源
		R r = null;
		Terminal ipHigher = null;
		Terminal ipLower = null;
//		CBX - CB104 CB105 CB106 CB107
		if (cb_evn_names != null) {
			for (int i = 0; i < cb_evn_names.size(); i++) {
				r = R.getR(cb_evn_names.get(i));
				if (r == null) {
					ipLower = cbx_jacks.get(i).getStitch(1);
					ipHigher = cbx_jacks.get(i).getStitch(2);
					if (ipHigher == null || ipLower == null) {
//						System.err.println(getClass() + cbx_jacks.get(i).getId() + "中找不到1 或 2号针脚");
						continue;
					}
					r = R.createSignal(cb_evn_names.get(i), ipHigher, ipLower, CB_SIGNAL_VOLT);
				}
				r.shareVoltage();
			}
		}
//		JF - JD1A JD1B
		if (jf_evn_names != null) {
			for (int i = 0; i < jf_evn_names.size(); i++) {
				r = R.getR(jf_evn_names.get(i));
				if (r == null) {
					ipHigher = jf_jacks.get(i).getStitch(3);
					ipLower = jf_jacks.get(i).getStitch(13);
					if (ipHigher == null || ipLower == null) {
//						System.err.println(getClass() + jf_jacks.get(i).getId() + "中找不到3 或 13号针脚");
						continue;
					}
					r = R.createSignal(jf_evn_names.get(i), ipHigher, ipLower, JF_SIGNAL_VOLT);
				}
				r.shareVoltage();
			}
		}
//		COP - COP10B COP10B
		if (cop_jacks != null) {
			for (int i = 0; i < cop_evn_names.size(); i++) {
				r = R.getR(cop_evn_names.get(i));
				if (r == null) {
					ipHigher = cop_jacks.get(i).getStitch(1);
					ipLower = cop_jacks.get(i).getStitch(3);
					if (ipHigher == null || ipLower == null) {
						LOG.warn("{}.{}中找不到1 或 3号针脚", getClass(), cop_jacks.get(i).getId());
						continue;
					}
					r = R.createSignal(cop_evn_names.get(i), ipHigher, ipLower, COP_SIGNAL_VOLT);
				}
				r.shareVoltage();
			}
		}
//		jfr* - jfr1 jfr2
		if (jfr_jacks != null) {
			String env = null;
			for (int i = 0; i < jfr_evn_names.size(); i++) {
				for (int j = 0; j < jfrEnvEnds.length; j++) {
//					env = jfr_evn_names.get(i) + jfrEnvEnds[j][0] + "_" + jfrEnvEnds[j][1];
					env = MessageFormat.format("{0}{1}_{2}", jfr_evn_names.get(i), jfrEnvEnds[j][0], jfrEnvEnds[j][1]);
					r = R.getR(env);
					if (r == null) {
						ipHigher = jfr_jacks.get(i).getStitch(jfrEnvEnds[j][0]);
						ipLower = jfr_jacks.get(i).getStitch(jfrEnvEnds[j][1]);
						if (ipHigher == null || ipLower == null) {
//							System.err.println(getClass() + jfr_jacks.get(i).getId() + "中找不到" + jfrEnvEnds[j][0] + " 或 " + jfrEnvEnds[j][1] + "号针脚");
							continue;
						}
						r = R.createSignal(env, ipHigher, ipLower, COP_SIGNAL_VOLT);
					}
					r.shareVoltage();
				}
			}
		}
//		System.err.println(this.getClass().getCanonicalName() + ".elecCompStart(end...)");
		LOG.info("{}启动完成!", elecComp.getTagName());
	}

	/**
	 * 关闭元器件指示灯<br>
	 * 关闭信号电<br>
	 */
	protected void elecCompEnd() {
		LOG.info("shutdown {}", elecComp.getTagName());
//		System.out.println(this.getClass().getCanonicalName() + ".elecCompEnd(start...)");
		getElecComp().getLightIOList().forEach(l -> l.closeLight());

		R r = null;
		if (cb_evn_names != null) {
			for (int i = 0; i < cb_evn_names.size(); i++) {
				r = R.getR(cb_evn_names.get(i));
				if (r != null) {
					r.shutPowerDown();
				}
			}
		}
		if (jf_evn_names != null) {
			for (int i = 0; i < jf_evn_names.size(); i++) {
				r = R.getR(jf_evn_names.get(i));
				if (r != null) {
					r.shutPowerDown();
				}
			}
		}
		if (cop_evn_names != null) {
			for (int i = 0; i < cop_evn_names.size(); i++) {
				r = R.getR(cop_evn_names.get(i));
				if (r != null) {
					r.shutPowerDown();
				}
			}
		}
		if (jfr_evn_names != null) {
			String env = null;
			for (int i = 0; i < jfr_evn_names.size(); i++) {
				for (int j = 0; j < jfrEnvEnds.length; j++) {
//					env = jfr_evn_names.get(i) + jfrEnvEnds[j][0] + "_" + jfrEnvEnds[j][1];
					env = MessageFormat.format("{0}{1}_{2}", jfr_evn_names.get(i), jfrEnvEnds[j][0], jfrEnvEnds[j][1]);
					r = R.getR(env);
					if (r != null) {
						r.shutPowerDown();
					}
				}
			}
		}
		LOG.info("shutdown done {}", elecComp.getTagName());
//		System.out.println(this.getClass().getCanonicalName() + ".elecCompEnd(end...)");
	}
}
