package jp.small_java_world.dummydatafactory.util;

import java.lang.reflect.Field;

public class ReflectUtil {
	public static void setStaticFieldValue(Class<?> targetClass, String fieldName, Object value) throws Exception {
		Field targetField = getDeclaredField(targetClass, fieldName);
		targetField.setAccessible(true);
		targetField.set(null, value);
	}

	public static void setFieldValue(Object targetObject, String fieldName, Object setValue) throws Exception {
		Field targetField = getDeclaredField(targetObject.getClass(), fieldName);
		targetField.setAccessible(true);
		targetField.set(targetObject, setValue);
	}

	public static Object getFieldValue(Object targetObject, String fieldName) throws Exception {
		Field targetField = getDeclaredField(targetObject.getClass(), fieldName);
		targetField.setAccessible(true);
		return targetField.get(targetObject);
	}

	public static Field getDeclaredField(Class<?> originalTargetClass, String fieldName) throws NoSuchFieldException {
		Class<?> targetClass = originalTargetClass;
		Field targetField = null;

		while (targetClass != null) {
			try {
				targetField = targetClass.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				targetClass = targetClass.getSuperclass();
			}
		}
		return targetField;
	}
}
