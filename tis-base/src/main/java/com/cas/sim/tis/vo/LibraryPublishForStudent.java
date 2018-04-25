package com.cas.sim.tis.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LibraryPublishForStudent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4236350438959994038L;
	private Integer id;
	private String name;
	private Integer score;
	private Long cost;
	private Date date;
}
