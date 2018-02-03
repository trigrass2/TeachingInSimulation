package com.cas.sim.tis.view.control.imp.vlc;

import java.nio.ByteBuffer;

import com.cas.sim.tis.view.control.IDistory;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;

import de.felixroske.jfxsupport.GUIState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VLCPlayer extends VBox implements IDistory {
	/**
	*
	*/
	private static final double FPS = 25.0;

	/**
	 * Lightweight JavaFX canvas, the video is rendered here.
	 */
	private ImageView canvas;

	/**
	 * Pixel writer to update the canvas.
	 */
	private PixelWriter pixelWriter;

	/**
	 * Pixel format.
	 */
	private WritablePixelFormat<ByteBuffer> pixelFormat;

	/**
	 * The vlcj direct rendering media player component.
	 */
	private DirectMediaPlayerComponent mediaPlayerComponent;

	/**
	*
	*/
	private Timeline timeline;

	/**
	*
	*/
	private EventHandler<ActionEvent> nextFrameHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent t) {
			if (playSkip) {
				return;
			}
			renderFrame();
		}
	};

	private HBox controls;

	private Button playPauseButton;

	private Slider playSlider;
	private Tooltip playSliderTip;

	private Label time;

	private Button volumeMuteButton;

	private Slider volumeSlider;
	private Tooltip volumeSliderTip;

	private boolean playSkip;

	private WritableImage writableImage;

	public boolean initSize;

	static {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "VLC");
	}

	public VLCPlayer() {

		this.setStyle("-fx-background-color:black;");
		this.setAlignment(Pos.BOTTOM_CENTER);

//		canvas = new Canvas();
//
//		pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
		canvas = new ImageView();
		pixelFormat = PixelFormat.getByteBgraInstance();

		StackPane pane = new StackPane(canvas);
		pane.setMinSize(0, 0);
		VBox.setVgrow(pane, Priority.ALWAYS);

		this.getChildren().add(pane);

		mediaPlayerComponent = new DirectMediaPlayerComponent(new DirectBufferFormatCallback()) {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				Platform.runLater(() -> {
					ObservableList<String> styleClass = playPauseButton.getStyleClass();
					styleClass.remove("play");
					styleClass.add("pause");
				});
			}

			@Override
			public void paused(MediaPlayer mediaPlayer) {
				Platform.runLater(() -> {
					ObservableList<String> styleClass = playPauseButton.getStyleClass();
					styleClass.remove("pause");
					styleClass.add("play");
				});
			}
		};
		mediaPlayerComponent.getMediaPlayer().setVolume(50);

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		double duration = 1000.0 / FPS;
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), nextFrameHandler));

		playPauseButton = new Button();
		playPauseButton.getStyleClass().add("play");
		playPauseButton.getStyleClass().add("img-btn");
		playPauseButton.setOnAction(e -> {
			if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
				pauseTimer();
			} else {
				playTimer();
			}
		});

		time = new Label("00:00:00");
		playSlider = new Slider();
		playSlider.setTooltip(playSliderTip = new Tooltip());
		playSlider.setOnMouseDragged(e -> {
			playSkip = true;
		});
		playSlider.setOnMouseDragReleased(e -> {
			mediaPlayerComponent.getMediaPlayer().setTime((long) ((playSlider.getValue() / 100f) * mediaPlayerComponent.getMediaPlayer().getLength()));
			playSkip = false;
		});
		playSlider.setOnMousePressed(e -> {
			playSkip = true;
		});
		playSlider.setOnMouseReleased(e -> {
			mediaPlayerComponent.getMediaPlayer().setTime((long) ((playSlider.getValue() / 100f) * mediaPlayerComponent.getMediaPlayer().getLength()));
			playSkip = false;
		});
		playSlider.valueProperty().addListener((b, o, n) -> {
			if (n.floatValue() >= 100) {
				stop();
			}
			long time = mediaPlayerComponent.getMediaPlayer().getTime();
			String curr = formatTime(time);
			playSliderTip.setText(curr);
			this.time.setText(curr);
		});
		HBox.setHgrow(playSlider, Priority.ALWAYS);

		volumeMuteButton = new Button();
		volumeMuteButton.getStyleClass().add("volume");
		volumeMuteButton.getStyleClass().add("img-btn");
		volumeMuteButton.setOnAction(e -> {
			ObservableList<String> styleClass = volumeMuteButton.getStyleClass();
			if (styleClass.contains("volume")) {
				styleClass.remove("volume");
				styleClass.add("mute");
				mediaPlayerComponent.getMediaPlayer().mute(true);
			} else {
				styleClass.remove("mute");
				styleClass.add("volume");
				mediaPlayerComponent.getMediaPlayer().mute(false);
			}
		});

		volumeSlider = new Slider();
		volumeSlider.setTooltip(volumeSliderTip = new Tooltip("50"));
		volumeSlider.setValue(50);
		volumeSlider.valueProperty().addListener((b, o, n) -> {
			mediaPlayerComponent.getMediaPlayer().setVolume(n.intValue());
			volumeSliderTip.setText(String.valueOf(n.intValue()));
		});
		controls = new HBox(10);
		controls.setPadding(new Insets(10));
		controls.setAlignment(Pos.CENTER_LEFT);
		controls.setStyle("-fx-background-color:#f0f0f0");
		controls.getChildren().addAll(playPauseButton, playSlider, time, volumeMuteButton, volumeSlider);
		this.getChildren().add(controls);
		this.widthProperty().addListener((b, o, n) -> {
			if (!initSize) {
				return;
			}
			if (o.doubleValue() > n.doubleValue()) {
				// 让被撑开的父容器还原
				double maxW = GUIState.getScene().getWidth() - 280;
				double maxH = GUIState.getScene().getHeight() - 205;
				if (n.doubleValue() > maxW || getHeight() > maxH) {
					canvas.setFitWidth(16);
					canvas.setFitHeight(9);
					this.layoutChildren();
					return;
				} else {
					double width = n.doubleValue();
					double height = getHeight() - controls.getHeight();
					resizeCanvas(width, height);
				}
			} else {
				double width = n.doubleValue();
				double height = getHeight() - controls.getHeight();
				resizeCanvas(width, height);
			}
		});
		this.heightProperty().addListener((b, o, n) -> {
			if (!initSize) {
				return;
			}
			if (o.doubleValue() > n.doubleValue()) {
				// 让被撑开的父容器还原
				double maxW = GUIState.getScene().getWidth() - 280;
				double maxH = GUIState.getScene().getHeight() - 205;
				if (getWidth() > maxW || n.doubleValue() > maxH) {
					canvas.setFitWidth(16);
					canvas.setFitHeight(9);
					this.layoutChildren();
					return;
				} else {
					double width = getWidth();
					double height = n.doubleValue() - controls.getHeight();
					resizeCanvas(width, height);
				}
			} else {
				double width = getWidth();
				double height = n.doubleValue() - controls.getHeight();
				resizeCanvas(width, height);
			}
		});
	}

	private void resizeCanvas(double width, double height) {
		double sourceWidth = writableImage.getWidth();
		double sourceHeight = writableImage.getHeight();
		double rateW = width / sourceWidth;
		double rateH = height / sourceHeight;
		if (rateW > rateH) {
			width = height * sourceWidth / sourceHeight;
		} else {
			height = width * sourceHeight / sourceWidth;
		}
		canvas.setFitHeight(height);
		canvas.setFitWidth(width);
	}

	public void loadVideo(String videoPath) {
		mediaPlayerComponent.getMediaPlayer().prepareMedia(videoPath);
//		默认播放
//		startTimer();
	}

	public final void stop() {
		pauseTimer();

		mediaPlayerComponent.getMediaPlayer().setTime(0);

		ObservableList<String> styleClass = playPauseButton.getStyleClass();
		styleClass.remove("pause");
		styleClass.add("play");
		playSlider.setValue(0);
	}

	/**
	 * Callback to get the buffer format to use for video playback.
	 */
	private class DirectBufferFormatCallback implements BufferFormatCallback {

		@Override
		public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
			double width = VLCPlayer.this.getWidth();
			double height = VLCPlayer.this.getHeight() - VLCPlayer.this.controls.getHeight();
			double rateW = width / sourceWidth;
			double rateH = height / sourceHeight;
			if (rateW > rateH) {
				width = height * sourceWidth / sourceHeight;
			} else {
				height = width * sourceHeight / sourceWidth;
			}
			writableImage = new WritableImage(sourceWidth, sourceHeight);
			canvas.setImage(writableImage);
			canvas.setFitWidth(width);
			canvas.setFitHeight(height);
			initSize = true;
			return new RV32BufferFormat(sourceWidth, sourceHeight);
		}
	}

	/**
	 *
	 */
	protected final void renderFrame() {
		Memory[] nativeBuffers = mediaPlayerComponent.getMediaPlayer().lock();
		if (nativeBuffers != null) {
			// FIXME there may be more efficient ways to do this...
			// Since this is now being called by a specific rendering time, independent of the native video callbacks being
			// invoked, some more defensive conditional checks are needed
			Memory nativeBuffer = nativeBuffers[0];
			if (nativeBuffer != null) {
				ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
				BufferFormat bufferFormat = ((DefaultDirectMediaPlayer) mediaPlayerComponent.getMediaPlayer()).getBufferFormat();
				if (bufferFormat.getWidth() > 0 && bufferFormat.getHeight() > 0) {
					getPW().setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
				}
			}
		}
		playSlider.setValue(mediaPlayerComponent.getMediaPlayer().getPosition() * 100);
		mediaPlayerComponent.getMediaPlayer().unlock();
	}

	private PixelWriter getPW() {
		if (pixelWriter == null) {
			pixelWriter = writableImage.getPixelWriter();
		}
		return pixelWriter;
	}

	private void startTimer() {
		timeline.playFromStart();
		mediaPlayerComponent.getMediaPlayer().start();
	}

	/**
	 *
	 */
	public void playTimer() {
		timeline.play();
		mediaPlayerComponent.getMediaPlayer().play();
	}

	/**
	 *
	 */
	public void pauseTimer() {
		timeline.pause();
		mediaPlayerComponent.getMediaPlayer().pause();
	}

	/**
	 *
	 */
	public void stopTimer() {
		timeline.stop();
		mediaPlayerComponent.getMediaPlayer().stop();
		mediaPlayerComponent.getMediaPlayer().release();
	}

	public static String formatTime(long value) {
		value /= 1000;
		int hours = (int) value / 3600;
		int remainder = (int) value - hours * 3600;
		int minutes = remainder / 60;
		remainder = remainder - minutes * 60;
		int seconds = remainder;
		return String.format("%d:%02d:%02d", hours, minutes, seconds);
	}

	@Override
	public void distroy() {
		stopTimer();
	}
}
