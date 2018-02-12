package com.cas.circuit.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @功能 全称：UserDataUtil<br>
 * @作者 ScOrPiO
 * @创建日期 2016年1月4日
 * @修改人 ScOrPiO
 */
public class UDUtil {
	private static final Logger log = Logger.getLogger(UDUtil.class.getCanonicalName());

	/**
	 * 获取模型中userdata， 如果出现异常，默认返回true。
	 */
	public static boolean getBooleanDefT(Spatial sp, String udk) {
		return getBoolean(sp, udk, true);
	}

	/**
	 * 获取模型中userdata， 如果出现异常，默认返回false。
	 */
	public static boolean getBooleanDefF(Spatial sp, String udk) {
		return getBoolean(sp, udk, false);
	}

	/**
	 * 获取模型中userdata。
	 */
	private static boolean getBoolean(Spatial sp, String udk, boolean defaultValue) throws RuntimeException {
		if (sp == null) {
			log.log(Level.INFO, "模型不存在。。 返回了一个默认值：【" + defaultValue + "】。若不影响业务逻辑，请忽略。。");
			return defaultValue;
		}
		Object udk_value = sp.getUserData(udk);
		if (udk_value == null) {
			return defaultValue;
		}
		if (!(udk_value instanceof Boolean)) {
			log.log(Level.ALL, "在模型" + sp.getName() + "中以" + udk + "作为键存储的值" + udk.toString() + "并非布尔类型。\r\n请确保所使用的键是正确的");
			return defaultValue;
		}
		return Boolean.TRUE.equals(udk_value);
	}

	public static Float getFloat(Spatial sp, String udk) {
		if (sp == null) {
			return 0f;
		}
		Object udk_value = sp.getUserData(udk);
		if (udk_value == null) {
			return 0f;
		}
		if (!(udk_value instanceof Float)) {
			log.log(Level.ALL, "在模型" + sp.getName() + "中以" + udk + "作为键存储的值" + udk.toString() + "并非浮点类型。\r\n请确保所使用的键是正确的");
			return 0f;
		}
		return (Float) udk_value;
	}

	public static Integer getInteger(Spatial sp, String udk) {
		if (sp == null) {
			return 0;
		}
		Object udk_value = sp.getUserData(udk);
		if (udk_value == null) {
			return 0;
		}
		if (!(udk_value instanceof Integer)) {
			log.log(Level.ALL, "在模型" + sp.getName() + "中以" + udk + "作为键存储的值" + udk.toString() + "并非整型。\r\n请确保所使用的键是正确的");
			return 0;
		}
		return (Integer) udk_value;
	}

	public static String getString(Spatial sp, String udk) {
		if (sp == null) {
			return null;
		}
		Object udk_value = sp.getUserData(udk);
		if (udk_value == null) {
			return null;
		}
		if (!(udk_value instanceof String)) {
			log.log(Level.ALL, "在模型" + sp.getName() + "中以" + udk + "作为键存储的值" + udk.toString() + "并非字符串类型。\r\n请确保所使用的键是正确的");
			return null;
		}
		return (String) udk_value;// StringUtil.transformCharacter((String) udk_value, "ISO-8859-1", System.getProperty("file.encoding"));
	}

	public static boolean setLocalTranslation(Spatial sp, String udk) {
		Vector3f localtionslation = sp.getUserData(udk);
		if (localtionslation != null) {
			sp.setLocalTranslation(localtionslation);
			return true;
		}
		return false;
	}

	public static boolean setLocalRotation(Spatial sp, String udk) {
		Object rotation = sp.getUserData(udk);
		if (rotation != null) {
			if (rotation instanceof Quaternion) {
				sp.setLocalRotation((Quaternion) rotation);
			} else if (rotation instanceof Matrix3f) {
				sp.setLocalRotation((Matrix3f) rotation);
			}
			return true;
		}
		return false;
	}

	public static boolean setLocalScale(Spatial sp, String udk) {
		Object localScale = sp.getUserData(udk);
		if (localScale != null) {
			if (localScale instanceof Float) {
				sp.setLocalScale((Float) localScale);
			} else if (localScale instanceof Vector3f) {
				sp.setLocalScale((Vector3f) localScale);
			} else {
			}
			return true;
		}
		return false;
	}

	public static int loadUD(Spatial target, Map<String, String> data, Class<?>[] clazz) {
		int count = 0;
		String key = null;
		Object value = null;
		Field[] fields = null;

		for (int i = 0; i < clazz.length; i++) {
			fields = clazz[i].getFields();
			for (Field field : fields) {
//				public static final 或 public final static 形式
//				25 = public(1) + static(8) + final(16)
				if (field.getModifiers() == 25) {
					try {
						key = (String) field.get(null);
						value = data.get(key);
						if (value != null) {
							assert value instanceof String;
							target.setUserData(key, parseValue((String) value));
							count++;
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassCastException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		log.log(Level.INFO, target.getUserData(UDKConsts.OBJECT) + "的模型， 应用了" + count + "个userdata， 共" + data.size() + "个");

		return count;
	}

	/**
	 * @param <T>
	 * @param value : 形如“类名称+":" + 参数” 的字符串, "Vector3f:1,1,1"<br>
	 * @return 根据参数得到指定类型的对象
	 */
	public static <T> T parseValue(String value) throws Exception {
		if (value == null) {
			return null;
		}
		if (value.indexOf(":") == -1) { // 默认为String类型
			return (T) value;
		}

		Object realValue = null;
		String[] arr = value.split(":");
		String type = arr[0];
		String param = arr[1];
		try {
			if ("String".equals(type)) {
				realValue = param;
			} else if ("int".equals(type) || "Integer".equals(type)) {
				realValue = Integer.parseInt(param);
			} else if ("long".equalsIgnoreCase(type)) {
				realValue = Long.parseLong(param);
			} else if ("float".equalsIgnoreCase(type)) {
				realValue = Float.parseFloat(param);
			} else if ("double".equalsIgnoreCase(type)) {
				realValue = Double.parseDouble(param);
			} else if ("boolean".equalsIgnoreCase(type)) {
				realValue = Boolean.parseBoolean(param);
			} else if ("Vector3f".equals(type)) {
				String[] arg = param.split(",");
				if (arg.length == 3) {
					realValue = new Vector3f(Float.parseFloat(arg[0]), Float.parseFloat(arg[1]), Float.parseFloat(arg[2]));
				} else {
					log.log(Level.WARN, "参数个数错误，需要3个，给了" + arg.length + "个：" + param);
				}
			} else if ("Quaternion".equals(type)) {
				String[] arg = param.split(",");
				if (arg.length == 4) {
					realValue = new Quaternion(Float.parseFloat(arg[0]), Float.parseFloat(arg[1]), Float.parseFloat(arg[2]), Float.parseFloat(arg[3]));
				} else {
					log.log(Level.WARN, "参数个数错误，需要4个，给了" + arg.length + "个：" + param);
				}
			} else if ("Vector2f".equals(type)) {
				String[] arg = param.split(",");
				if (arg.length == 2) {
					realValue = new Vector2f(Float.parseFloat(arg[0]), Float.parseFloat(arg[1]));
				} else {
					log.log(Level.WARN, "参数个数错误，需要2个，给了" + arg.length + "个：" + param);
				}
			} else {
				log.log(Level.WARN, "未处理的类型" + type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) realValue;
	}

}
