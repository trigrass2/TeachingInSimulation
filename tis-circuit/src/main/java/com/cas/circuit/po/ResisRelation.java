package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class ResisRelation {

	private String term1Id;
	private String term2Id;
	private Float value;

	@XmlAttribute
	public String getTerm1Id() {
		return term1Id;
	}

	public void setTerm1Id(String term1Id) {
		this.term1Id = term1Id;
	}

	@XmlAttribute
	public String getTerm2Id() {
		return term2Id;
	}

	public void setTerm2Id(String term2Id) {
		this.term2Id = term2Id;
	}

	@XmlAttribute
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

//	@Override
//	public String toString() {
//		return term1Id + "--" + term2Id + ":" + value;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((term1Id == null) ? 0 : term1Id.hashCode() + ((term2Id == null) ? 0 : term2Id.hashCode()));
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) return true;
//		if (obj == null) return false;
//		if (getClass() != obj.getClass()) return false;
//		ResisRelation other = (ResisRelation) obj;
//		return (Util.equals(term1Id, other.term1Id) && Util.equals(term2Id, other.term2Id)) || (Util.equals(term1Id, other.term2Id) && Util.equals(term2Id, other.term1Id));
//	}

}
