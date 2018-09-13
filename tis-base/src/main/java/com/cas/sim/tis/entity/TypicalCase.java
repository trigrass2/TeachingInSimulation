package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TypicalCase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8103115364848459642L;
	@Id
	private Integer id;
//	电路名称
	private String name;
//	电路存档的路径
	private String archivePath;
	private String drawings;
	private Integer creator;
	private Date createDate;
	private Integer updater;
	private Date updateDate;
	private Boolean del = false;
	private Boolean publish = false;
}
