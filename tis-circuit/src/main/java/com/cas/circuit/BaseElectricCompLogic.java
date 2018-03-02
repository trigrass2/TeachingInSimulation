
/**
 * 
 */
package com.cas.circuit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.circuit.consts.IOType;
import com.cas.circuit.vo.ControlIO;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Terminal;

/**
 * @author 张振宇 Jul 29, 2015 2:20:09 PM
 */
public class BaseElectricCompLogic {
	public static final Logger LOG = LoggerFactory.getLogger(BaseElectricCompLogic.class);
	public static final String ELECCOMP_STATE = "eleccomp_state";
	protected ElecCompDef elecComp;
	protected boolean workable;

	public BaseElectricCompLogic() {
	}

	/**
	 * @param elecComp the elecComp to set
	 */
	public void setElecComp(ElecCompDef elecComp) {
		this.elecComp = elecComp;
	}

	public void initialize() {
//		System.out.println("BaseElectricCompLogic.initialize()" + Thread.currentThread().getName());
//		log.log(Level.INFO, "加载元器件{0},用时{1}", new Object[] { elecComp.getTagName(), System.currentTimeMillis() });
//		读取元器件铭牌信息
		readNameplateInfo();
//		elecCompMdl = (Node) MdlMapUtil.loadMdlWithAbbr(getElecComp().getDef().getMdlRef(), app.getAssetManager());
//		elecCompMdl = (Node) parentMdl.getChild(getElecComp().getDef().getMdlRef());
//		elecCompMdl = (Node) parentMdl.getChild(getElecComp().getMdlName());
//		elecCompMdl = parentMdl;
//		registerEvent();
//		elecCompMdl.setLocalTranslation(getElecComp().getLocation());
//		elecCompMdl.rotate(getElecComp().getRotation()[0], getElecComp().getRotation()[1], getElecComp().getRotation()[2]);
//		elecCompMdl.setUserData(BaseElectricCompLogic.ELECCOMP_STATE, BaseElectricCompLogic.this);
	}

	protected void readNameplateInfo() {
	}

	protected String getNameplate(String key, String defValue) {
		String value = getElecComp().getParam(key);
		if (value != null) {
			return value;
		}
		return defValue;
	}

	/**
	 * @param terminal
	 */
	public final void onReceived(Terminal terminal) {
		if (terminal.getIoType() == IOType.OUTPUT) {
			return;
		}
		TermTeam team = terminal.getTermTeam();
		team.signIn(terminal);
		if (!team.resetIfReady()) {
			return;
		}
		onReceivedLocal(terminal);
	}

	/**
	 * @param terminal
	 */
	protected void onReceivedLocal(Terminal terminal) {
	}

	/**
	 * @return the elecComp
	 */
	public ElecCompDef getElecComp() {
		return elecComp;
	}

	public void destroy() {
	}

	/**
	 * @return the batchEnable
	 */
	public boolean isBatchEnable() {
//		默认不可批处理
//		return "1".equals(elecCompMdl.getUserData(UDKConsts.Boolean_BATCHABLE));
		return false;
	}

	public void onButtonAction(ControlIO button, boolean press) {
	}

	public boolean isWorkable() {
		return workable;
	}

}