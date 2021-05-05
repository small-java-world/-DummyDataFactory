package jp.small_java_world.dummydatafactory.config;

import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class ColumnTypeConfig {
	private static final Logger logger = LoggerFactory.getLogger(ColumnTypeConfig.class);

	@SuppressWarnings({ "rawtypes", "unused" })
	private static Map COLUMN_TYPE_MAP;

	static {
		InputStream inputStream = ColumnTypeConfig.class.getResourceAsStream("/columnType.yml");

		Yaml yaml = new Yaml();
		COLUMN_TYPE_MAP = yaml.loadAs(inputStream, Map.class);
	}

	public static String getJavaType(String dbDataType) {
		if(COLUMN_TYPE_MAP.containsKey(dbDataType)) {
			logger.debug("dbDataType={} javaType={}", dbDataType, COLUMN_TYPE_MAP.get(dbDataType));
			return COLUMN_TYPE_MAP.get(dbDataType).toString();
		}
		else {
			logger.error("dbDataType={} javaType is not defined", dbDataType);
		}
		
		return null;
	}
}
