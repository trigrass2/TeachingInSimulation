package com.cas.sim.tis.flow;

import com.cas.circuit.component.ElecCompProxy;
import com.cas.circuit.component.WireProxy;
import com.jme3.scene.Spatial;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Step {
	public enum StepType {
		ElecComp, Wire;
	}

	private StepType type;
	private String name;
	private ElecCompProxy compProxy;
	private WireProxy wireProxy;
	private Spatial model;
}
