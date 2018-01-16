/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates. All rights reserved. Use is subject to license terms. This file is available and licensed under the following license: Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.view.LoginView;

import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Login Controller.
 */
@FXMLController
public class NetworkController extends VBox implements Initializable {

	protected double xOffset;
	protected double yOffset;
	
	@FXML
	private TextField ip;
	@FXML
	private TextField port;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void ok() {
	}

	@FXML
	public void cancel() {
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), Application.getScene().getRoot());
		rotateTransition.setAxis(new Point3D(0, 1, 0));
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(90);
		rotateTransition.setOnFinished(e->{
			Application.showView(LoginView.class);
			RotateTransition r = new RotateTransition(Duration.millis(200), Application.getScene().getRoot());
			r.setAxis(new Point3D(0, 1, 0));
			r.setFromAngle(90);
			r.setToAngle(0);
			r.play();
		});
		rotateTransition.play();
	}
}
