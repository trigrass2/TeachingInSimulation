package com.cas.sim.tis.view.control.imp.vlc;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.view.control.IDistory;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

@Component
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
	private final Canvas canvas;

	/**
	 * Pixel writer to update the canvas.
	 */
	private final PixelWriter pixelWriter;

	/**
	 * Pixel format.
	 */
	private final WritablePixelFormat<ByteBuffer> pixelFormat;

	/**
	 * The vlcj direct rendering media player component.
	 */
	private final DirectMediaPlayerComponent mediaPlayerComponent;

	/**
	*
	*/
	private final Timeline timeline;

	/**
	*
	*/
	private final EventHandler<ActionEvent> nextFrameHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent t) {
			if (playSkip) {
				return;
			}
			renderFrame();
		}
	};

	private static HBox controls;

	private final Button playPauseButton;

	private final Slider playSlider;
	private final Tooltip playSliderTip;

	private final Label time;

	private final Button volumeMuteButton;

	private final Slider volumeSlider;
	private final Tooltip volumeSliderTip;

	private boolean playSkip;

	public VLCPlayer() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "VLC");

		this.setStyle("-fx-background-color:black;");

		canvas = new Canvas();

		pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
		pixelFormat = PixelFormat.getByteBgraInstance();

		this.setCenter(canvas);

		mediaPlayerComponent = new DirectMediaPlayerComponent(new DirectBufferFormatCallback());
		mediaPlayerComponent.getMediaPlayer().setVolume(50);

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		double duration = 1000.0 / FPS;
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), nextFrameHandler));

		playPauseButton = new Button();
		playPauseButton.getStyleClass().add("play");
		playPauseButton.getStyleClass().add("img-btn");
		playPauseButton.setOnAction(e -> {
			ObservableList<String> styleClass = playPauseButton.getStyleClass();
			if (styleClass.contains("play")) {
				styleClass.remove("play");
				styleClass.add("pause");
				startTimer();
			} else {
				styleClass.remove("pause");
				styleClass.add("play");
				pauseTimer();
			}
		});

		time = new Label("00:00:00");
		playSlider = new Slider();
		playSlider.setTooltip(playSliderTip = new Tooltip());
		playSlider.setOnMouseDragged(e -> {
			playSkip = true;
		});
		playSlider.setOnMouseDragReleased(e -> {
			System.out.println(playSlider.getValue());
			mediaPlayerComponent.getMediaPlayer().setPosition((float) (playSlider.getValue() / 100f));
			playSkip = false;
		});
		playSlider.setOnMousePressed(e -> {
			playSkip = true;
		});
		playSlider.setOnMouseReleased(e -> {
			System.out.println(playSlider.getValue());
			mediaPlayerComponent.getMediaPlayer().setPosition((float) (playSlider.getValue() / 100f));
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
		volumeSlider.setTooltip(volumeSliderTip = new Tooltip());
		volumeSlider.setValue(50);
		volumeSlider.valueProperty().addListener((b, o, n) -> {
			mediaPlayerComponent.getMediaPlayer().setVolume(n.intValue());
			volumeSliderTip.setText(String.valueOf(n));
		});

		controls = new HBox(10);
		controls.setPadding(new Insets(10));
		controls.setAlignment(Pos.CENTER_LEFT);
		controls.setStyle("-fx-background-color:#f0f0f0");
		controls.getChildren().addAll(playPauseButton, playSlider, time, volumeMuteButton, volumeSlider);
		this.setBottom(controls);
	}

	public void loadVideo(String videoPath) {
		mediaPlayerComponent.getMediaPlayer().prepareMedia(videoPath);
	}

	public final void stop() {
		stopTimer();

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
			int height = (int) (VLCPlayer.this.getHeight() - VLCPlayer.controls.getHeight());
			if (useSourceSize) {
				float rateW = width * 1f / sourceWidth;
				float rateH = height * 1f / sourceHeight;
				if (width >= sourceHeight && height >= sourceWidth) {
					width = sourceWidth;
					height = sourceHeight;
				} else if (rateW < rateH) {
					height = (int) (width * sourceHeight * 1f / sourceWidth);
				} else {
					width = (int) (height * sourceWidth * 1f / sourceHeight);
				}
			}
			canvas.setWidth(width);
			canvas.setHeight(height);
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
					pixelWriter.setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
				}
			}
		}
		playSlider.setValue(mediaPlayerComponent.getMediaPlayer().getPosition() * 100);
		mediaPlayerComponent.getMediaPlayer().unlock();
	}

	/**
	 *
	 */
	public void startTimer() {
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
		stop();
	}
}
