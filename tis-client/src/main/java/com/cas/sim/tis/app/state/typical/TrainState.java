package com.cas.sim.tis.app.state.typical;

import java.util.ArrayList;
import java.util.List;

import com.cas.circuit.component.Wire;
import com.cas.circuit.component.WireProxy;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.listener.ElecCompTrainListener;
import com.cas.sim.tis.app.state.BaseState;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.flow.Step.StepType;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class TrainState extends BaseState {

	private Node rootCompNode;
	private Geometry elecCompBox;

	private Step curr;
	private List<Step> steps = new ArrayList<Step>();
	private ElecCompTrainListener listener;
	private HoldStatePro holdState;

	private TypicalCase3D ui;

	public TrainState(TypicalCase3D ui, List<Step> steps, Node rootCompNode, HoldStatePro holdState) {
		this.ui = ui;
		this.steps = steps;
		this.rootCompNode = rootCompNode;
	}

	@Override
	protected void initializeLocal() {

		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Cyan);

		Box mesh = new Box(0.001f, 0.001f, 0.001f);
		elecCompBox = new Geometry("ElecCompBox", mesh);
		elecCompBox.setMaterial(mat);
		elecCompBox.setCullHint(CullHint.Always);
		mouseEventState.addCandidate(elecCompBox, listener = new ElecCompTrainListener(ui, this, holdState));

		rootCompNode.attachChild(elecCompBox);

		next();
	}

	public boolean checkWire(Wire wire) {
		WireProxy proxy = curr.getWireProxy();
		WireProxy linked = wire.getProxy();
		String comp1Uuid = wire.getTerm1().getElecCompDef().getProxy().getUuid();
		String comp2Uuid = wire.getTerm2().getElecCompDef().getProxy().getUuid();
		String ternimal1Id = wire.getTerm1().getId();
		String ternimal2Id = wire.getTerm2().getId();
		if (!proxy.getColor().equals(linked.getColor())) {
			return false;
		} else if (!proxy.getRadius().equals(linked.getRadius())) {
			return false;
		} else if (proxy.getComp1Uuid().equals(proxy.getComp2Uuid())) {
			if (proxy.getComp1Uuid().equals(comp1Uuid)) {
				return proxy.getComp2Uuid().equals(comp2Uuid);
			} else if (proxy.getComp1Uuid().equals(comp2Uuid)) {
				return proxy.getComp2Uuid().equals(comp1Uuid);
			}
		} else if (proxy.getComp1Uuid().equals(comp1Uuid)) {
			if (!proxy.getTernimal1Id().equals(ternimal1Id)) {
				return false;
			} else if (!proxy.getComp2Uuid().equals(comp2Uuid)) {
				return false;
			} else if (!proxy.getTernimal2Id().equals(ternimal2Id)) {
				return false;
			} else {
				return true;
			}
		} else if (proxy.getComp1Uuid().equals(comp2Uuid)) {
			if (!proxy.getTernimal1Id().equals(ternimal2Id)) {
				return false;
			} else if (!proxy.getComp2Uuid().equals(comp1Uuid)) {
				return false;
			} else if (!proxy.getTernimal2Id().equals(ternimal1Id)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public void next() {
		holdState.discard();
		int index = steps.indexOf(curr);
		if (index >= steps.size() - 1) {
			Platform.runLater(() -> {
				AlertUtil.showAlert(AlertType.INFORMATION, "大吉大利今晚吃鸡！");
			});
		} else {
			curr = steps.get(index + 1);
			if (StepType.ElecComp == curr.getType()) {
				Box box = (Box) elecCompBox.getMesh();
				BoundingBox bound = ((BoundingBox) curr.getModel().getWorldBound());
				box.updateGeometry(Vector3f.ZERO, bound.getXExtent(), bound.getYExtent(), bound.getZExtent());
				elecCompBox.updateModelBound();

				elecCompBox.setCullHint(CullHint.Dynamic);
				elecCompBox.setLocalTranslation(bound.getCenter());
				listener.setStep(curr);
			} else {
				elecCompBox.setCullHint(CullHint.Always);
				mouseEventState.removeCandidate(elecCompBox);
			}
		}
	}

	public void flowNext() {
		Platform.runLater(() -> {
			ui.flowNext();
		});
	}
}
