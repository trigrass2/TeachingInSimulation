package com.cas.sim.tis;

import com.cas.authority.javafx.RegistApplication;
import com.cas.sim.tis.consts.SystemInfo;

public class RegisterApp {

	public static void main(String[] args) {
		javafx.application.Application.launch(RegistApplication.class, "--productID=" + SystemInfo.APP_ID);
	}

}
