
package com.cas.circuit.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.cas.circuit.CfgConst;
import com.cas.circuit.consts.IOType;
import com.cas.circuit.control.MoveCtrl;
import com.cas.circuit.control.RotateCtrl;
import com.cas.circuit.xml.adapter.BooleanIntAdapter;
import com.cas.circuit.xml.adapter.FloatArrayAdapter;
import com.cas.circuit.xml.adapter.StringArrayAdapter;
import com.cas.circuit.xml.adapter.UnsignedAxisAdapter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * 包括:按钮(button) 和 旋钮(switch)
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ControlIO extends SwitchCtrl implements Savable {
	private static final Logger LOG = LoggerFactory.getLogger(Terminal.class);

	/**
	 * 揿钮分/揿钮合
	 */
	public static final String INTERACT_UNIDIR = "unidir";
	/**
	 * 按下不弹起按钮或上下拨动的开关
	 */
	public static final String INTERACT_CLICK = "click";
	/**
	 * 按下弹起型按钮
	 */
	public static final String INTERACT_PRESS = "press";
	/**
	 * 拨转型旋钮
	 */
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
	@XmlJavaTypeAdapter(UnsignedAxisAdapter.class)
	private Vector3f axis;
	@XmlAttribute
	private float speed = 4;
	@XmlAttribute
	@XmlJavaTypeAdapter(BooleanIntAdapter.class)
	private Boolean smooth = Boolean.FALSE;
	@XmlElement(name = "Param")
	@XmlElementWrapper(name = "Params")
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

	/**
	 * 对ROTATE类型：index 0：向上滚， 1：向下滚. 对非ROTATE类型：index 0：弹起， 1：按下
	 */
	public void playMotion() {
		if (CfgConst.BUTTON_MOTION_ROTATE.equals(motion)) {
//			迅速动
			rotateModel(spatial, motionParams[1 - motionIndex] - motionParams[motionIndex], (v) -> switchEnd());
		} else if (CfgConst.BUTTON_MOTION_MOVE.equals(motion)) {
			if (smooth) {
			} else if (motionIndex == 0) {// 如果是从
				zeroIndexLocation = spatial.getLocalTranslation();
			} else {
				spatial.setLocalTranslation(zeroIndexLocation);
			}
			moveModel(spatial, motionParams[1 - motionIndex] - motionParams[motionIndex], (v) -> switchEnd());
		}
		motionIndex = 1 - motionIndex;
	}

	public void setSpatial(Spatial spatial) {
		if (spatial == null) {
			LOG.error("没有找到ControlIO::name为{}的模型{}", name, mdlName);
		}
		this.spatial = spatial;
		spatial.setUserData("entity", this);
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public String getName() {
		return name;
	}

	/**
	 * 移动模型一定距离
	 * @param model 模型名
	 * @param deltaDist 距离
	 * @param c 结束动作
	 */
	private void moveModel(final Spatial model, final float deltaDist, final Consumer<Void> c) {
		if (StringUtils.isEmpty(model)) {
			LOG.error("移动目标模型为空！");
			return;
		}
		MoveCtrl control = model.getControl(MoveCtrl.class);
		if (control == null) {
			model.addControl(control = new MoveCtrl(speed, smooth, axis));
		}
		control.setDistance(deltaDist);
		control.setExecutor(c);
		control.setEnabled(true);
	}

	/**
	 * 旋转模型一定角度
	 * @param model 模型名
	 * @param deltaDeg 角度
	 * @param c 结束动作
	 */
	private void rotateModel(final Spatial model, final float deltaDeg, final Consumer<Void> c) {
		if (StringUtils.isEmpty(model)) {
			LOG.error("旋转目标模型为空！");
			return;
		}
		RotateCtrl control = model.getControl(RotateCtrl.class);
		if (control == null) {
			model.addControl(control = new RotateCtrl(speed, smooth, axis));
		}
		control.setRad(deltaDeg);
		control.setExecutor(c);
		control.setEnabled(true);
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}
}
