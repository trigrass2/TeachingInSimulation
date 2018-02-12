package com.cas.circuit.util;

/**
 * @功能 UDKConsts：意思为：Spatial：UserData's Key<br>
 *     表示在模型的UserData属性中存放的自定义的数据。<br>
 *     注：UserData可存放的数据类型有：基本数据类型+savable接口类型<br>
 *     注：每一个常量必须有注释<br>
 * @作者 ScOrPiO
 * @创建日期 2015年12月29日
 * @修改人 ScOrPiO
 */
public final class UDKConsts {
	/**
	 * 
	 */
	private UDKConsts() {
	}

	/**
	 * 可批处理<br>
	 * 对应的udv默认不可批处理,即为false
	 */
	public static final String Boolean_BATCHABLE = "batchable";
	/**
	 * 模型对鼠标可见<br>
	 * udv默认为true
	 */
	public static final String Boolean_TO_MOUSE_VISIBLE = "ToMouseVisible";
	/**
	 * 对应的业务逻辑对象
	 */
	public static final String OBJECT = "object";
	/**
	 * 物品，名称
	 */
	public static final String OBJECT_NAME = "objectName";
	/**
	 * 物品，描述
	 */
	public static final String OBJECT_DESC = "objectDesc";
	/**
	 * 物品，图片
	 */
	public static final String OBJECT_PIC = "picDir";
	/**
	 * 对应的业务逻辑对象
	 */
	public static final String NEXT_ITEM = "nextItem";
	/**
	 * 对应的业务逻辑对象
	 */
	public static final String PREV_ITEM = "prevItem";
	/**
	 * 对应的业务逻辑对象个数
	 */
	public static final String INTEGER_OBJECT_NUMBER = "objectNumber";
	/**
	 * 手持模型时，模型的位置
	 */
	public static final String HOLDING_LOCATION = "holdingLocation";
	/**
	 * 手持模型时，模型的旋转角度
	 */
	public static final String HOLDING_ROTATION = "holdingRotation";
	/**
	 * 手持模型时，模型的缩放大小
	 */
	public static final String HOLDING_SCALE = "holdingScale";
	/**
	 * 被克隆对象
	 */
	public static final String ORIG_TARGET = "origTarget";
	/**
	 * 用于比较，ID
	 */
	public static final String COMPARE_NAME = "compareName";
	/**
	 * 用于比较，规格
	 */
	public static final String COMPARE_SPECIFICATION = "compareSpecification";

	/**
	 * 模型动画所表示的动作的名称，
	 */
	public static final String ACTION_NAME = "actionName";
}
