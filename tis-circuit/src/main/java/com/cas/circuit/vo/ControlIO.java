
package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.CfgConst;
import com.cas.circuit.consts.IOType;
import com.cas.circuit.xml.adapter.FloatArrayAdapter;
import com.cas.circuit.xml.adapter.StringArrayAdapter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

/**
 * 包括:按钮(button) 和 旋钮(switch)
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ControlIO extends SwitchCtrl {
	public static final String INTERACT_UNIDIR = "unidir";
	public static final String INTERACT_CLICK = "click";
	public static final String INTERACT_PRESS = "press";
	public static final String INTERACT_ROTATE = "rotate";

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	private String controlModName;
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String interact;
	@XmlAttribute
	private Integer defSwitch;
	@XmlAttribute
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	private String[] switchIn;
	@XmlAttribute
	private String motion;
	@XmlAttribute
	@XmlJavaTypeAdapter(FloatArrayAdapter.class)
	private float[] motionParams;
	@XmlAttribute
	private String axis;
	@XmlAttribute
	private float speed = 4;
	@XmlAttribute
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	private Boolean smooth;
	@XmlElement(name = "Param")
	private List<Param> params = new ArrayList<>();

	private Spatial spatial;

	private Vector3f zeroIndexLocation;

	private int motionIndex = 0;

	public String getMdlName() {
		return mdlName;
	}

	public String getType() {
		if (type == null) {
			return IOType.BOTH;
		}
		return type;
	}

	public String getInteract() {
		return interact;
	}

	@Override
	protected void changeStateIndex(Integer index) {
		switchIndex = 1 - switchIndex;
	}

	/**
	 * 对ROTATE类型：index 0：向上滚， 1：向下滚. 对非ROTATE类型：index 为null
	 */
	public void switchStateChanged(final Integer wheel) {
//		if (INTERACT_CLICK.equals(po.getInteract())) {
//			switchStart(1 - switchIndex);
//		} else if (INTERACT_PRESS.equals(po.getInteract())) {
//			if (switchIndex == 0 || (switchIndex == 1 && !((Magnetism) parent).isEffect())) {
//				switchStart(1 - switchIndex);
//			}
////		} else if (INTERACT_UNIDIR.equals(po.getInteract())) {
////			if (switchIndex == 1) {
////				switchStart(0);
////				// 磁生力
////				for (ControlIO otherButton : ((Magnetism) parent).getControlIOs()) {
////					if (otherButton != ControlIO.this && otherButton.getPO().getType().contains(CfgConst.SWITCH_CTRL_OUTPUT)) {
////						otherButton.doSwitch(1);
////					}
////				}
////			}
//		}
////		}
//		
		switchStart(1 - switchIndex);
	}

//	FIXME
	/**
	 * 对ROTATE类型：index 0：向上滚， 1：向下滚. 对非ROTATE类型：index 0：弹起， 1：按下
	 */
	public void playMotion() {
		if (CfgConst.BUTTON_MOTION_ROTATE.equals(motion)) {
//			// 迅速动
//			ModelMotionUtil.rotateModel(model, axis, motionParams[1 - motionIndex] - motionParams[motionIndex]);
			switchEnd();
		} else if (CfgConst.BUTTON_MOTION_MOVE.equals(motion)) {
			if (smooth) {
			} else if (motionIndex == 0) {// 如果是从
				zeroIndexLocation = spatial.getLocalTranslation();
			} else {
				spatial.setLocalTranslation(zeroIndexLocation);
			}

//			FIXME
//			float dist = motionParams[1 - motionIndex] - motionParams[motionIndex];
//			if (smooth) {
//				ModelMotionUtil.moveModelSmooth(model, axis, dist, speed, new Runnable() {
//					@Override
//					public void run() {
//						Pool.getCircuitPool().submit(new Runnable() {
//							@Override
//							public void run() {
//								try {
//									switchEnd();
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						});
//					}
//				}, null);
//			} else {
//				ModelMotionUtil.moveModel(model, axis, dist);
			switchEnd();
//			}
		}
		motionIndex = 1 - motionIndex;
	}

	public void setSpatial(Spatial spatial) {
//		if (model == null) {
//			log.error("没有找到按钮名为" + po.getName() + "的模型");
//		}
		this.spatial = spatial;
	}

}
