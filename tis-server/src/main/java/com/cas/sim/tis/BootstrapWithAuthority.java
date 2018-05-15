package com.cas.sim.tis;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.util.StringUtils;

import com.cas.authority.Consts;
import com.cas.authority.action.RegisterAction;
import com.cas.authority.model.AuthorityEntity;
import com.cas.authority.validate.ValidateThread;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.util.SpringUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
	private Stage primaryStage;

	public static void main(String[] args) {
		BootstrapWithAuthority.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(getRegistContent()));
		primaryStage.setTitle(RESOURCES.getString("authority.title"));
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		this.primaryStage = primaryStage;
		validate(null);
	}

	public void validate(String[] args) {
		ValidateThread thread = new ValidateThread(SystemInfo.APP_ID);

		ExecutorService pool = Executors.newSingleThreadExecutor();
		Future<Integer> task = pool.submit(thread);
		try {
			Integer result = task.get();
			if (result != Consts.AUTHORITY_FILE_AVAILABLE) {
				failure(result);
			} else {
				success(args, thread);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	protected void failure(Integer code) {
		System.err.println("无效的证书文件。 错误代码:" + code);
		switch (code) {
		case Consts.AUTHORITY_FILE_COPY: {
			Alert alert = new Alert(AlertType.ERROR, MessageFormat.format(RESOURCES.getString("authority.startup.failed"), code, RESOURCES.getString("authority.file.copy")));
			alert.setHeaderText(null);
			alert.showAndWait();
			break;
		}
		case Consts.AUTHORITY_FILE_EXPIRED: {
			Alert alert = new Alert(AlertType.ERROR, MessageFormat.format(RESOURCES.getString("authority.startup.failed"), code, RESOURCES.getString("authority.file.expired")));
			alert.setHeaderText(null);
			alert.showAndWait();
			break;
		}
		case Consts.AUTHORITY_FILE_MODIFIED: {
			Alert alert = new Alert(AlertType.ERROR, MessageFormat.format(RESOURCES.getString("authority.startup.failed"), code, RESOURCES.getString("authority.file.modified")));
			alert.setHeaderText(null);
			alert.showAndWait();
			break;
		}
		case Consts.AUTHORITY_FILE_UNPITCH: {
			Alert alert = new Alert(AlertType.ERROR, MessageFormat.format(RESOURCES.getString("authority.startup.failed"), code, RESOURCES.getString("authority.file.unpitch")));
			alert.setHeaderText(null);
			alert.showAndWait();
			break;
		}
		case Consts.AUTHORITY_FILE_NOT_FOUNT: {
			Alert alert = new Alert(AlertType.INFORMATION, MessageFormat.format(RESOURCES.getString("authority.startup.init"), SystemInfo.APP_NAME));
			alert.setHeaderText(null);
			alert.showAndWait();
			break;
		}
		default:
			break;
		}
		primaryStage.show();
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

			// 避免连接服务器线程阻塞FX的UI线程
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					try {
						new RegisterAction() {
							protected String getProductID() {
								return SystemInfo.APP_ID;
							};

							@Override
							protected String getRegistCode() {
								return registCode.getText();
							}

							protected String getUserName() {
								return userName.getText();
							}

							protected void onRegistResult(boolean success) {
								Platform.runLater(() -> {
									if (success) {
										Alert alert = new Alert(AlertType.INFORMATION, RESOURCES.getString("authority.regist.success"));
										alert.initOwner(primaryStage);
										alert.setHeaderText(null);
										alert.showAndWait();
										System.exit(0);
									} else {
										Alert alert = new Alert(AlertType.ERROR, RESOURCES.getString("authority.regsit.failed"));
										alert.initOwner(primaryStage);
										alert.setHeaderText(null);
										alert.showAndWait();
										regist.setDisable(false);
										cancel.setDisable(false);
										error.setText(null);
									}
								});
							}
						}.execute();
					} catch (Exception e1) {
						e1.printStackTrace();
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.ERROR, RESOURCES.getString("authority.regist.disconnect"));
							alert.initOwner(primaryStage);
							alert.setHeaderText(null);
							alert.showAndWait();
							regist.setDisable(false);
							cancel.setDisable(false);
							error.setText(null);
						});
					}
					return null;
				}
			};
			new Thread(task).start();
		});

		HBox btns = new HBox(40);
		btns.setAlignment(Pos.CENTER);
		btns.getChildren().addAll(regist, cancel);
		box.getChildren().add(btns);
		return box;
	}
}
