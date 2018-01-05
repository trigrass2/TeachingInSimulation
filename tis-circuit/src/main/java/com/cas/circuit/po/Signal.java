package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class Signal {
//	信号地址
	private String addr;
//	信号注释
	private String anno;
//	信号的符号
	private String symbol;
//	信号功能描述
	private String func;
//  信号使用范围
	private String limit;
//  信号激活条件,上升沿,下降沿
	private String active;

	@XmlAttribute
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@XmlAttribute
	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	@XmlAttribute
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@XmlAttribute
	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	@XmlAttribute
	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	@XmlAttribute
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
