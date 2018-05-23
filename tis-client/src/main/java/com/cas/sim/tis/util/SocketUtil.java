package com.cas.sim.tis.util;

import java.io.IOException;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.listener.ClientMessageListener;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.message.DisconnectMessage;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SocketUtil {
	INSTENCE;
	private Client client;

	public void connect(@NotNull String address, int port, Consumer<Boolean> result) {
		try {
			client = Network.connectToServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, address, port, -1);
		} catch (IOException e) {
			result.accept(false);
		}
		client.addMessageListener(ClientMessageListener.INSTENCE);
		client.addClientStateListener(new ClientStateListener() {

			@Override
			public void clientConnected(Client c) {

			}

			@Override
			public void clientDisconnected(Client c, DisconnectInfo info) {
				if (info.reason.equals(DisconnectMessage.KICK)) {
					Platform.runLater(() -> {
						AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("server.connect.kick"));
						Platform.exit();
						System.exit(0);
					});
				} else {
					Platform.runLater(() -> {
						AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("server.disconnect"));
						Platform.exit();
						System.exit(0);
					});
				}
			}
		});
		result.accept(true);
	}

	public void start() {
		if (client == null) {
			return;
		}
		if (client.isConnected()) {
			return;
		}
		client.start();
	}

	public void send(Message msg) {
		if (client == null) {
			return;
		}
		if (!client.isStarted()) {
			log.info("客户端尚未启动");
			return;
		}
		while (!client.isConnected()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		client.send(msg);
		log.info("发送成功{}", msg);
	}
}
