package jp.small_java_world.dummydatafactory.util;

import java.lang.reflect.Field;

public class TestUtil {
	public static void setStaticFieldValue(Class<?> targetClass, String fieldName, Object setValue)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field targetField = getDeclaredField(targetClass, fieldName);
		targetField.setAccessible(true);
		targetField.set(null, setValue);
	}

	public static Field getDeclaredField(Class<? extends Object> originalTargetClass, String fieldName)
			throws NoSuchFieldException {
		Class<? extends Object> targetClass = originalTargetClass;
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
