package com.cas.sim.tis;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.util.StringUtils;

import com.cas.authority.model.AuthorityEntity;
import com.cas.authority.validate.ValidateThread;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.util.SpringUtil;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BootstrapWithAuthority extends javafx.application.Application {
	public static final ResourceBundle RESOURCES = ResourceBundle.getBundle("i18n/messages");

	public static void main(String[] args) {
		ValidateThread thread = new ValidateThread(SystemInfo.APP_ID);
		thread.validate((result) -> {
			BootstrapWithAuthority.launch(args);
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(getRegistContent()));
		primaryStage.setTitle(RESOURCES.getString("authority.title"));
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	protected void success(String[] args, ValidateThread validate) {
		System.err.println("证书文件验证通过。可以启动程序");
		Application.jul2slf4j();
		SpringApplication.run(Application.class);
		AuthorityEntity entity = validate.getEntity();
		int maxLogin = entity.getNode();
		ServerConfig serverConfig = SpringUtil.getBean(ServerConfig.class);
		serverConfig.setMaxLogin(maxLogin);
		Logger.getLogger(BootstrapWithAuthority.class).info("客户端连接数量限制：" + maxLogin + "人");

		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
			try {
				validate.getTimerClock().validate();
			} catch (Exception e1) {
				serverConfig.close();
				Alert alert = new Alert(AlertType.ERROR, RESOURCES.getString("authority.file.expired"));
				alert.setHeaderText(null);
				alert.show();
				System.exit(0);
			}
		}, 0, 3, TimeUnit.SECONDS);
	}

	private VBox getRegistContent() {
		VBox box = new VBox(10);
		box.setPrefSize(500, 225);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(20));

		GridPane grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(0, 40, 0, 40));

		RowConstraints row1 = new RowConstraints();
		RowConstraints row2 = new RowConstraints();

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();

		grid.getRowConstraints().add(row1);
		grid.getRowConstraints().add(row2);

		grid.getColumnConstraints().add(col1);
		grid.getColumnConstraints().add(col2);

		Label nameLabel = new Label(RESOURCES.getString("authority.user.name"));
		nameLabel.setMinSize(100, 40);
		GridPane.setConstraints(nameLabel, 0, 0);

		TextField userName = new TextField();
		userName.setMaxSize(300, 40);
		userName.setMinSize(300, 40);
		GridPane.setConstraints(userName, 1, 0);

		Label codeLabel = new Label(RESOURCES.getString("authority.regist.code"));
		codeLabel.setMinSize(100, 40);
		GridPane.setConstraints(codeLabel, 0, 1);

		TextField registCode = new TextField();
		registCode.setMaxSize(300, 40);
		registCode.setMinSize(300, 40);
		GridPane.setConstraints(registCode, 1, 1);

		grid.getChildren().addAll(nameLabel, userName, codeLabel, registCode);
		box.getChildren().add(grid);

		Label error = new Label();
		error.setStyle("-fx-text-fill: #ff5656;");
		box.getChildren().add(error);

		Button cancel = new Button(RESOURCES.getString("authority.button.cancel"));
		cancel.setPrefSize(100, 40);
		cancel.setOnAction(e -> {
			System.exit(0);
		});

		Button regist = new Button(RESOURCES.getString("authority.button.regist"));
		regist.setPrefSize(100, 40);
		regist.setOnAction(e -> {
			String name = userName.getText();
			if (StringUtils.isEmpty(name)) {
				error.setText(MessageFormat.format(RESOURCES.getString("authority.check.cant.be.null"), RESOURCES.getString("authority.user.name")));
				return;
			}
			String time = registCode.getText();
			if (StringUtils.isEmpty(time)) {
				error.setText(MessageFormat.format(RESOURCES.getString("authority.check.cant.be.null"), RESOURCES.getString("authority.regist.code")));
				return;
			}
			error.setText(RESOURCES.getString("authority.regist.connecting"));
			regist.setDisable(true);
			cancel.setDisable(true);
		});

		HBox btns = new HBox(40);
		btns.setAlignment(Pos.CENTER);
		btns.getChildren().addAll(regist, cancel);
		box.getChildren().add(btns);
		return box;
	}
}
