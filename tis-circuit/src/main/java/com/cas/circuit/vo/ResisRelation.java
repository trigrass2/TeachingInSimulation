package com.cas.circuit.vo;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class ResisRelation {//extends BaseVO<ResisRelationPO> {
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
		// TODO Auto-generated constructor stub
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
		if (getTerm1Id().equals(termId)) {
			return getTerm2Id();
		}
		return getTerm1Id();
	}

	public Float getValue() {
		return value;
	}

	
	public String getTerm1Id() {
		return term1Id;
	}
	
	public String getTerm2Id() {
		return term2Id;
	}
	
	/**
	 * @return the term1
	 */
	public Terminal getTerm1() {
		return term1;
	}

	/**
	 * @return the term2
	 */
	public Terminal getTerm2() {
		return term2;
	}

	public void setTerm1(Terminal term1) {
		this.term1 = term1;
	}

	public void setTerm2(Terminal term2) {
		this.term2 = term2;
	}

	/**
	 * @param termAndStich
	 */
	void initTerminals(Map<String, Terminal> termAndStich) {
		term1 = termAndStich.get(getTerm1Id());
		term2 = termAndStich.get(getTerm2Id());
		if (term1 == null || term2 == null) {
			throw new RuntimeException(ResisRelation.class.getCanonicalName() + "  initTerminals");
		}

		term1.getResisRelationMap().put(term2, this);
		term2.getResisRelationMap().put(term1, this);
	}

	public boolean isActivated() {
		return activated;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return term1.getId() + "--" + term2.getId();
	}

}
