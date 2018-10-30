package com.cas.sim.tis.app.state;

import java.util.Timer;
import java.util.TimerTask;

import com.cas.sim.tis.circuit.ISpeaker;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

public class AudioState extends BaseState implements ISpeaker {

	private AudioNode meter_buzzer_short;
	private AudioNode meter_buzzer_continuity;

	private int buzzerFlags;
	public static final int BUZZER_SHORT = 0x1;
	public static final int BUZZER_CONTINUITY = 0x2;

	@Override
	protected void initializeLocal() {
//		万用表按钮急促声
		meter_buzzer_short = new AudioNode(assetManager, "ogg/BUZZER_SHORT.wav", AudioData.DataType.Buffer);
		meter_buzzer_short.setPositional(false);
		meter_buzzer_continuity = new AudioNode(assetManager, "ogg/BUZZER_CONTINUITY.wav", AudioData.DataType.Buffer);
		meter_buzzer_continuity.setPositional(false);

//		addMapping("POINT", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
//		addMapping("CONTNIUE", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
//		addListener((ActionListener) (name, isPressed, tpf) -> {
//			if ("POINT".equals(name) && isPressed) {
//				buzzer_short();
//			} else if ("CONTNIUE".equals(name)) {
//				if (isPressed) {
//					buzzer_continuity();
//				} else {
//					buzzer_stop();
//				}
//			}
//		}, "CONTNIUE", "POINT");
	}

	@Override
	public void buzzer_short() {
		buzzerFlags |= BUZZER_SHORT;
	}

	@Override
	public void buzzer_shortDelay() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				buzzerFlags |= BUZZER_SHORT;
			}
		}, 130);
	}

	@Override
	public void buzzer_continuity() {
		buzzerFlags |= BUZZER_CONTINUITY;
	}

	@Override
	public void buzzer_stop() {
		buzzerFlags &= ~BUZZER_CONTINUITY;
	}

	@Override
	public void update(float tpf) {
		if ((buzzerFlags & BUZZER_SHORT) != 0) {
			meter_buzzer_short.playInstance();
			// 只播放一次
			buzzerFlags &= ~BUZZER_SHORT;
		}

		if ((buzzerFlags & BUZZER_CONTINUITY) != 0) {
			// 持续播放
			meter_buzzer_continuity.play();
		} else {
			meter_buzzer_continuity.stop();
		}
	}
}
