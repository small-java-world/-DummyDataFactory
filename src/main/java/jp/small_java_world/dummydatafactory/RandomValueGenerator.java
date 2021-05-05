package jp.small_java_world.dummydatafactory;

import java.sql.Timestamp;
import java.util.Date;

public class RandomValueGenerator {
	public static final int DAFAULT_DATA_SIZE = 10;

	public static Object generateRandomValue(Class<?> type, SqlColumnData sqlColumnData) {
		if (type.isAssignableFrom(String.class)) {
			return RandomDataUtil
					.generateRandomString(sqlColumnData != null ? sqlColumnData.dbDataSize : DAFAULT_DATA_SIZE);
		} else if (type.isAssignableFrom(Integer.class) || type.getName().equals("int")) {
			return RandomDataUtil.generateRandomInt();
		} else if (type.isAssignableFrom(Long.class) || type.getName().equals("long")) {
			return RandomDataUtil.generateRandomLong();
		} else if (type.isAssignableFrom(Float.class) || type.getName().equals("float")) {
			return RandomDataUtil.generateRandomFloat();
		} else if (type.isAssignableFrom(Short.class) || type.getName().equals("short")) {
			return RandomDataUtil.generateRandomShort();
		} else if (type.isAssignableFrom(Boolean.class) || type.getName().equals("boolean")) {
			return RandomDataUtil.generateRandomBool();
		} else if (type.isAssignableFrom(Date.class)) {
			return RandomDataUtil.generateRandomDate();
		} else if (type.isAssignableFrom(Timestamp.class)) {
			return RandomDataUtil.generateRandomTimestamp();
		}

		return null;
	}
}
