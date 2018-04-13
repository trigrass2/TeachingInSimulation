package com.cas.circuit.logic;

import java.util.List;
import java.util.Map;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.Voltage;
import com.cas.circuit.util.MesureResult;
import com.cas.circuit.util.R;
import com.cas.circuit.vo.Jack;
import com.cas.circuit.vo.Terminal;
import com.cas.util.Util;

/**
 * 变频器
 */
public class Inverter extends BaseElectricCompLogic {
	protected Terminal _r;
	protected Terminal _s;
	protected Terminal _t;
	private Terminal _u;
	private Terminal _v;
	private Terminal _w;

//	PU网线接口中针脚
	private Terminal _rda;
	private Terminal _rdb;
	private Terminal _sda;
	private Terminal _sdb;

	private InvertorAssist assist;
//	需要改变变频器输出的控制电压
	private boolean voltNeedChange;
	private String controlVoltEnv;
	private Dir dir;
//	控制电压的频率
	private float frequency = 0;

	public Inverter() {
	}

	enum Dir {
		/**
		 * 顺时针方向（clockwise）
		 */
		CW(120),
		/**
		 * 反时针方向（counterclockwise）
		 */
		CCW(-120);
//		相位差
		private int phase;

		Dir(int phase) {
			this.phase = phase;
		}

		public int getPhase() {
			return phase;
		}
	}

	@Override
	public void initialize() {
		super.initialize();

		assist = new InvertorAssist(this);
		controlVoltEnv = "Invertor_" + hashCode() + "_ControlVoltage";

		_r = elecComp.getTerminal("R");
		_s = elecComp.getTerminal("S");
		_t = elecComp.getTerminal("T");

		_u = elecComp.getTerminal("U");
		_v = elecComp.getTerminal("V");
		_w = elecComp.getTerminal("W");

		Jack jack_pu = elecComp.getJackMap().get("PU");
		_rda = jack_pu.getStitch(3);
		_rdb = jack_pu.getStitch(4);
		_sda = jack_pu.getStitch(1);
		_sdb = jack_pu.getStitch(2);
	}

	@Override
	protected void onReceivedLocal(Terminal terminal) {
		super.onReceivedLocal(terminal);
		if (_r == terminal || _s == terminal || _t == terminal) {
//			分别测量uvw三者之间的电压情况
			MesureResult resultUV = R.matchRequiredVolt(Voltage.IS_AC, _r, _s, 380, 10);
			MesureResult resultVW = R.matchRequiredVolt(Voltage.IS_AC, _s, _t, 380, 10);
			MesureResult resultWU = R.matchRequiredVolt(Voltage.IS_AC, _t, _r, 380, 10);

//			判断三者之间是否存在电势差
			boolean tmp = Util.notEmpty(resultUV) && Util.notEmpty(resultWU) && Util.notEmpty(resultVW);

			List<R> controlVoltPower = R.get3Phase(controlVoltEnv);
			if (tmp && !workable) {
				workable = true;
//				if (controlVoltPower == null) {
//					controlVoltPower = R.create3Phase(controlVoltEnv, _u, _v, _w, new Terminal(), 380);
//				}
//				R.set3PhaseFrequency(controlVoltEnv, frequency);
//				R.reversePhase(controlVoltEnv, dir == Dir.CW);
//
//				for (R r : controlVoltPower) {
//					r.shareVoltage();
//				}
			} else if (!tmp && workable) {
				if (controlVoltPower != null) {
					for (R r : controlVoltPower) {
						r.shutPowerDown();
					}
				}
				workable = false;
			}
		} else if (!workable) {
			return;
		} else if (_rda == terminal || _rdb == terminal) {
//			处理PLC侧发来的数据
			MesureResult result = R.matchRequiredVolt(Voltage.IS_DC, _rda, _rdb, 5, 1);
			if (result != null) {
				Map<String, String> data = R.getR(result.getEvn()).getVoltage().getData();
				assist.decode(data);
			}

			if (voltNeedChange) {
				List<R> controlVoltPower = R.get3Phase(controlVoltEnv);
				if (controlVoltPower != null) {
//					断电
					for (R r : controlVoltPower) {
						r.shutPowerDown();
					}
				}
				if (dir != null) {
					controlVoltPower = R.create3Phase(controlVoltEnv, _u, _v, _w, new Terminal(), 380);
//					通电
//					System.err.println("Inverter.onReceivedLocal()" + frequency);
					R.set3PhaseFrequency(controlVoltEnv, frequency);
					R.reversePhase(controlVoltEnv, dir == Dir.CW);
					for (R r : controlVoltPower) {
						r.shareVoltage();
					}
				}
			}
		} else {

		}
	}

	/**
	 * 设置要控制电机旋转的方向
	 * @param dir
	 */
	public void setDir(Dir dir) {
		this.voltNeedChange = dir != this.dir;
		this.dir = dir;
//		System.out.println("Inverter.setDir(" + dir + ") voltNeedChange?" + voltNeedChange);
	}

	/**
	 * 设置要控制电机旋转的速度
	 * @param frequency
	 */
	public void setFrequency(float frequency) {
		this.voltNeedChange = frequency != this.frequency;
		this.frequency = frequency;
//		System.out.println("Inverter.setFrequency(" + frequency + ") voltNeedChange?" + voltNeedChange);
	}
}
