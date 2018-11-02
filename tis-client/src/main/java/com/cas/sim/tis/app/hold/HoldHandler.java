package com.cas.sim.tis.app.hold;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;

public interface HoldHandler extends Savable {
	/**
	 * 设置
	 * @param spatial
	 */
	void setSpatial(Spatial spatial);

	void initialize();

	/**
	 * @param up true: 鼠标滚轮向上滚动
	 */
	void rotate(boolean up);

	/**
	 * 物体跟随鼠标移动
	 */
	void move();

	/**
	 * 物体放置在默认的平面中（CompPlane）
	 * @return 
	 */
	boolean putDown();

	/**
	 * 物体放置在指定的物品中
	 * @return 
	 */
	boolean putDownOn(Spatial base);

	/**
	 * 丢弃所拿的物品
	 */
	void discard();

	/**
	 * 
	 * @return
	 */
	PickAllow getPickAllow();
	
	@Override
	default void write(JmeExporter ex) {
//		nothing to write
	}

	@Override
	default void read(JmeImporter im) {
//		nothing to read
	}

	<T> T getData();

}