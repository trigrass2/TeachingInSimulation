package com.cas.sim.tis;

import java.util.ResourceBundle;

import com.softkey.SoftKey;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("restriction")
public class BootstrapWithLockAndAuthority {
    public static void main(String[] args) {
        BootstrapWithLockAndAuthority app = new BootstrapWithLockAndAuthority();
        app.validateLock(args);
    }

	public void validateLock(final String[] args) {
        SoftKey sk = new SoftKey(null);
        boolean hasRightLock = sk.CheckKeyByReadEprom() == 0;
        if (hasRightLock) {
            javafx.application.Application.launch(BootstrapWithAuthority.class, args);
        } else {
        	PlatformImpl.startup(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText(ResourceBundle.getBundle("i18n/messages").getString("unavaliable.locker"));
				alert.showAndWait();
				Platform.exit();
				System.exit(0);
			});
        }
    }
}
