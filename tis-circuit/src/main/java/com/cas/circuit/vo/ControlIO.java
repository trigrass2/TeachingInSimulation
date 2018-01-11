
package com.cas.circuit.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.consts.IOType;
import com.cas.sim.tis.xml.adapter.FloatArrayAdapter;
import com.jme3.scene.Spatial;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

/**
 * 包括:按钮(button) 和 旋钮(switch)
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ControlIO {// extends SwitchCtrl<ControlIOPO> {

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
	@XmlList
	private List<String> switchIn;
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

	public String getInteract() {
		return interact;
	}

	public String getType() {
		if (type == null) {
			return IOType.BOTH;
		}
		return type;
	}

//	private String elementText;
//	
//	
////	ReadOnly
//	private List<Float> motionParams = new ArrayList<Float>();

	private Spatial model;

//
//	private Vector3f zeroIndexLocation;
//
//	private int motionIndex = 0;
//
//	private Map<String, String> properties;
//
	public String getName() {
		return name;
	}

	public String getMdlName() {
		return mdlName;
	}

	public float getSpeed() {
		return speed;
	}

//	@Override
//	protected void toValueObject() {
//		super.toValueObject();
//		this.properties = getStringMap(po.getElementText());
//		if (Util.isInteger(po.getDefSwitch())) {
//			int defIndex = Integer.parseInt(po.getDefSwitch());
//			switchIndex = defIndex;
//			motionIndex = defIndex;
//		}
//	}
//
//	@Override
//	protected void changeStateIndex(Integer index) {
//		switchIndex = 1 - switchIndex;
//	}
//
////	@Override
////	public void doSwitch(Integer index) {
////		super.doSwitch(index);
////	}
//
//	/**
//	 * 对ROTATE类型：index 0：向上滚， 1：向下滚. 对非ROTATE类型：index 为null
//	 */
//	public void switchStateChanged(final Integer wheel, final MouseEvent e) {
//		switchStart(1 - switchIndex);
//	}
//
//	//FIXME
//	/**
//	 * 对ROTATE类型：index 0：向上滚， 1：向下滚. 对非ROTATE类型：index 0：弹起， 1：按下
//	 */
//	public void playMotion(/* AssetManager assetManager, */MouseEvent e) {
//		if (CfgConst.BUTTON_MOTION_ROTATE.equals(po.getMotion())) {
//			// 迅速动
//			ModelMotionUtil.rotateModel(model, po.getAxis(), motionParams.get(1 - motionIndex) - motionParams.get(motionIndex));
//			switchEnd();
//		} else if (CfgConst.BUTTON_MOTION_MOVE.equals(po.getMotion())) {
//			if (smooth) {
//			} else if (motionIndex == 0) {// 如果是从
//				zeroIndexLocation = model.getLocalTranslation();
//			} else {
//				model.setLocalTranslation(zeroIndexLocation);
//			}
//
//			float dist = motionParams.get(1 - motionIndex) - motionParams.get(motionIndex);
//			if (smooth) {
//				ModelMotionUtil.moveModelSmooth(model, po.getAxis(), dist, speed, new Runnable() {
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
//				ModelMotionUtil.moveModel(model, po.getAxis(), dist);
//				switchEnd();
//			}
//		}
//		motionIndex = 1 - motionIndex;
//	}

	public void setModel(Spatial model) {
		if (model == null) {
//			log.error("没有找到按钮名为" + po.getName() + "的模型");
		}
		this.model = model;
	}

//	/*
//	 * (non-Javadoc)
//	 * @see com.cas.cfg.vo.BaseVO#cleanUp()
//	 */
//	@Override
//	protected void cleanUp() {
//		model = null;
//		motionParams = new ArrayList<Float>();
//	}
//
//	public Map<String, String> getProperties() {
//		return properties;
//	}
//
//	public List<Float> getMotionParams() {
//		return motionParams;
//	}
//
//	@Override
//	public String toString() {
//		return "ControlIO []";
//	}

}
