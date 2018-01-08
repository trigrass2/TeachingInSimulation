package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class ResisRelation {
	@XmlAttribute
	private String term1Id;
	@XmlAttribute
	private String term2Id;
	@XmlAttribute
	private Float value;

	private Terminal term1;
	private Terminal term2;

	private boolean activated;

	private String termIds;

	/**
	 * 
	 */
	public ResisRelation() {
		super();
	}

	/**
	 * @param term1
	 * @param term2
	 * @param value
	 */
	public ResisRelation(Terminal term1, Terminal term2, Float value, boolean activated) {
		super();
		this.term1 = term1;
		this.term2 = term2;
		this.value = value;
		this.activated = activated;
	}

	public String getAnotherTermId(String termId) {
		if (termId == null) {
			return null;
		}
		return termId.equals(term1Id) ? term2Id : term1Id;
	}

//	/**
//	 * @param termAndStich
//	 */
//	void initTerminals(Map<String, Terminal> termAndStich) {
//		term1 = termAndStich.get(po.getTerm1Id());
//		term2 = termAndStich.get(po.getTerm2Id());
//		if (term1 == null || term2 == null) {
//			throw new RuntimeException(ResisRelation.class.getCanonicalName() + "  initTerminals");
//		}
//
//		term1.getResisRelationMap().put(term2, this);
//		term2.getResisRelationMap().put(term1, this);
//	}

	
	public boolean isActivated() {
		return activated;
	}

	public String getTerm1Id() {
		return term1Id;
	}

	public void setTerm1Id(String term1Id) {
		this.term1Id = term1Id;
	}

	public String getTerm2Id() {
		return term2Id;
	}

	public void setTerm2Id(String term2Id) {
		this.term2Id = term2Id;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getTermIds() {
		if (termIds == null) {
			termIds = term1Id + "-" + term2Id;
		}
		return termIds;
	}

	public Terminal getTerm1() {
		return term1;
	}

	public void setTerm1(Terminal term1) {
		this.term1 = term1;
	}

	public Terminal getTerm2() {
		return term2;
	}

	public void setTerm2(Terminal term2) {
		this.term2 = term2;
	}

}
