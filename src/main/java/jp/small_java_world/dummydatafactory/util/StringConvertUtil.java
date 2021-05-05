package jp.small_java_world.dummydatafactory.util;

import org.apache.commons.lang3.StringUtils;

public class StringConvertUtil {
	public static String toSnakeCaseCase(String snakeCase) {
		StringBuilder stringBuilder = new StringBuilder(snakeCase.length() + 10);

		if (StringUtils.isEmpty(snakeCase)) {
			return snakeCase;
		}

		String firstUpperCase = firstLowerCase(snakeCase);
		String[] terms = StringUtils.splitPreserveAllTokens(firstUpperCase, "_", -1);

		stringBuilder.append(terms[0]);

		for (int i = 1, len = terms.length; i < len; i++) {
			stringBuilder.append(firstUpperCase(terms[i]));
		}
		return stringBuilder.toString();
	}

	public static String toCamelCase(String camelCase) {
		if (StringUtils.isEmpty(camelCase)) {
			return camelCase;
		}
		StringBuilder stringBuilder = new StringBuilder(camelCase.length() + 10);
		String firstLowerCase = firstLowerCase(camelCase);
		char[] buf = firstLowerCase.toCharArray();

		stringBuilder.append(buf[0]);
		for (int i = 1; i < buf.length; i++) {
			if ('A' <= buf[i] && buf[i] <= 'Z') {
				stringBuilder.append('_');
				stringBuilder.append((char) (buf[i] + 0x20));
			} else {
				stringBuilder.append(buf[i]);
			}
		}

		return stringBuilder.toString();
	}

	private static String firstLowerCase(String target) {
		if (StringUtils.isEmpty(target)) {
			return target;
		} else {
			return target.substring(0, 1).toLowerCase().concat(target.substring(1));
		}
	}

	public static String firstUpperCase(String target) {
		if (StringUtils.isEmpty(target)) {
			return target;
		} else {
			return target.substring(0, 1).toUpperCase().concat(target.substring(1));
		}
	}
}
