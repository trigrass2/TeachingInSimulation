package com.cas.sim.tis.view.control.imp.vlc;

import java.nio.ByteBuffer;

import com.cas.sim.tis.view.control.IDistory;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VLCPlayer extends BorderPane implements IDistory {
	/**
	*
	*/
	private static final double FPS = 60.0;

	/**
	 * Set this to <code>true</code> to resize the display to the dimensions of the video, otherwise it will use {@link #WIDTH} and {@link #height}.
	 */
	private static final boolean useSourceSize = true;

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

	static {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "VLC");
	}

	public VLCPlayer() {

		this.setStyle("-fx-background-color:black;");

//		canvas = new Canvas();
//
//		pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
		pixelFormat = PixelFormat.getByteBgraInstance();

		canvas = new ImageView();

		this.setCenter(canvas);

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
		this.setBottom(controls);
		this.widthProperty().addListener((b, o, n) -> {
			if (writableImage == null) {
				return;
			}
			double zoom = n.doubleValue() / o.doubleValue();
			if (zoom > 1) {
				canvas.setFitWidth(writableImage.getWidth() * zoom);
			} else {
				canvas.setFitWidth(writableImage.getWidth());
			}
		});
		this.heightProperty().addListener((b, o, n) -> {
			if (writableImage == null) {
				return;
			}
			double zoom = n.doubleValue() / o.doubleValue();
			if (zoom > 1) {
				canvas.setFitHeight(writableImage.getHeight() * zoom);
			} else {
				canvas.setFitHeight(writableImage.getHeight());
			}
		});
	}

	public void loadVideo(String videoPath) {
		mediaPlayerComponent.getMediaPlayer().prepareMedia(videoPath);
//		默认播放
		startTimer();
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
			int width = (int) VLCPlayer.this.getWidth();
			int height = (int) (VLCPlayer.this.getHeight() - VLCPlayer.this.controls.getHeight());
			if (useSourceSize) {
				float rateW = width * 1f / sourceWidth;
				float rateH = height * 1f / sourceHeight;
				if (width >= sourceWidth && height >= sourceHeight) {
					width = sourceWidth;
					height = sourceHeight;
				} else if (rateW < rateH) {
					height = (int) (width * sourceHeight * 1f / sourceWidth);
				} else {
					width = (int) (height * sourceWidth * 1f / sourceHeight);
				}
			}
			writableImage = new WritableImage(width, height);
			canvas.setImage(writableImage);
			canvas.setFitWidth(width);
			canvas.setFitHeight(height);
			return new RV32BufferFormat(width, height);
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
