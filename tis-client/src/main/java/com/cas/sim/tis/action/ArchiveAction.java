package com.cas.sim.tis.action;

import javax.annotation.Nullable;

import org.springframework.stereotype.Component;

import com.cas.circuit.util.JaxbUtil;
import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.util.HTTPUtils;

@Component
public class ArchiveAction {
	@Nullable
	public Archive parse(String archivePath) {
		if(archivePath == null) {
			return null;
		}
		return JaxbUtil.converyToJavaBean(HTTPUtils.getUrl(archivePath), Archive.class);
	}

}
