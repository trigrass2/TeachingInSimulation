package com.cas.sim.tis.entity;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokenCase implements Serializable {
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
	private Integer creatorId;
}
