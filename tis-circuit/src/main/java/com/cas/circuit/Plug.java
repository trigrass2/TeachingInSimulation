package com.cas.circuit;

import java.util.ArrayList;
import java.util.List;

import com.cas.circuit.vo.Cable;
import com.cas.circuit.vo.Format;
import com.cas.circuit.vo.Jack;
import com.cas.circuit.vo.Stitch;

public class Plug {

	private Cable cable;
	private Jack jack;
	private boolean inserted;
	private Format format;

//	Key: index，对应插孔中的针脚号
	private List<Stitch> stitchList = new ArrayList<>();

	public Plug(Cable cable) {
		super();
		this.cable = cable;
	}

	public void addTerminal(Stitch term1) {
		stitchList.add(term1.getIndex(), term1);
	}

	public boolean isInserted() {
		return inserted;
	}

	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}

	public Cable getCable() {
		return cable;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		if (format == null) {
			throw new RuntimeException("不能给设置插头设置null值");
		}
		this.format = format;
//		format.getCableMap().put(cable.getPO().getId(), cable);
		format.getCableList().add(cable);
	}

	public Jack getJack() {
		return jack;
	}

	public void setJack(Jack jack) {
		this.jack = jack;
	}
	public List<Stitch> getStitchList() {
		return stitchList;
	}
}