package com.cas.sim.tis.app.state;

import com.cas.circuit.vo.ElecCompDef;
import com.jme3.scene.Spatial;

public interface TypicalCaseOperating {

	Spatial takeUp(ElecCompDef comp);

	Spatial putDown(ElecCompDef comp);
}
