package com.cas.sim.tis.util;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * @功能 动画（animation）的工具类。
 * @作者 ScOrPiO
 * @创建日期 2016年1月8日
 * @修改人 ScOrPiO
 */
public final class AnimUtil {
	/**
	 * 拆装动画的帧速为25帧每秒
	 */
	public static final float ANIM_FPS = 25.0f;
	protected static int count;

	private AnimUtil() {
	}

	/**
	 * 播放模型中所有动画
	 * @param spatial
	 */
	public static void simplePlay(@NotNull Spatial spatial) {
		simplePlay(spatial, LoopMode.DontLoop, 1);
	}

	public static void simplePlay(@NotNull Spatial spatial, LoopMode mode, float speed) {
		getAnimControlSpatials(spatial).forEach(sp -> {
			AnimControl animControl = sp.getControl(AnimControl.class);
			AnimChannel animChannel;
			if (animControl.getNumChannels() == 0) {
				animChannel = animControl.createChannel();
			} else {
				animChannel = animControl.getChannel(0);
			}
			animControl.getAnimationNames().forEach(animName -> {
				animChannel.setAnim(animName);
				animChannel.setLoopMode(mode);
				animChannel.setSpeed(speed);
			});
		});
	}

	public static void animReset(@NotNull Spatial spatial) {
		getAnimControlSpatials(spatial).forEach(sp -> {
			AnimControl animControl = sp.getControl(AnimControl.class);
			for (int i = 0; i < animControl.getNumChannels(); i++) {
				animControl.getChannel(i).reset(true);
			}
		});
	}

	private static float getMaxAnimLength(Spatial spatial) {
		float maxLen = 0;
		for (Spatial sp : getAnimControlSpatials(spatial)) {
			AnimControl animControl = sp.getControl(AnimControl.class);
			float length = animControl.getAnimationLength(animControl.getAnimationNames().iterator().next());
			maxLen = Math.max(maxLen, length);
		}
		return maxLen;
	}

	/**
	 * @param animatedSpatial
	 */
	public static void cleanAnimControl(Spatial animatedSpatial) {
		AnimControl animControl = animatedSpatial.getControl(AnimControl.class);
		if (animControl != null) {
			animControl.clearChannels();
			animControl.clearListeners();
		}
//		if (animControl != null) {
//			int chanelSize = animControl.getNumChannels();
//			for (int i = 0; i < chanelSize; i++) {
//				animControl.getChannel(i).reset(true);
//			}
//		}
		animatedSpatial.removeControl(AnimControl.class);

		if (animatedSpatial instanceof Node) {
			for (Spatial child : ((Node) animatedSpatial).getChildren()) {
				cleanAnimControl(child);
			}
		}
	}

	/**
	 * 获取模型内所有有动画的节点
	 * @param spatial
	 */
	@NotNull
	public static List<Spatial> getAnimControlSpatials(Spatial spatial) {
		List<Spatial> animControlSpatials = new ArrayList<Spatial>();
		if (spatial.getControl(AnimControl.class) != null) {
			animControlSpatials.add(spatial);
		}
		if (spatial instanceof Node) {
			((Node) spatial).getChildren().forEach(sub -> {
				animControlSpatials.addAll(getAnimControlSpatials(sub));
			});
		}
		return animControlSpatials;
	}

	/**
	 * 打印模型内所有有动画的节点
	 * @param animatedSpatial
	 */
	public static void listAnim(Spatial animatedSpatial) {
		if (animatedSpatial == null) {
			return;
		}
		AnimControl animControl = animatedSpatial.getControl(AnimControl.class);
		if (animControl == null) {
			return;
		}
		animControl.getAnimationNames().forEach(System.out::println);
	}

}
