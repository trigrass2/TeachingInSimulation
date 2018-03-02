package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class ArchiveAction {
	@Resource
	private HTTPUtils httpUtils;

	public Archive parse(String archivePath) {
		return JaxbUtil.converyToJavaBean(httpUtils.getUrl(archivePath), Archive.class);
	}

}
