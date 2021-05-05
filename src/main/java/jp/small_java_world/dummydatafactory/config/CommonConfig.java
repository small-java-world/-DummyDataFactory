package jp.small_java_world.dummydatafactory.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonConfig {
	private static final Logger logger = LoggerFactory.getLogger(CommonConfig.class);

	private static final String CONFIG_PROP_FILE = "dummyDataFactorySetting.properties";
	private static final Properties properties;

	static {
		properties = new Properties();
		try {
			InputStream inputStream = CommonConfig.class.getClassLoader().getResourceAsStream(CONFIG_PROP_FILE);
			properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		} catch (IOException e) {
			logger.error("load properties fail {}", CONFIG_PROP_FILE);
		}
	}
	
	public static String getSqlDirName() {
		return (String) properties.get("sqlDirName");
	}
	
	public static String getSqlFilePattern() {
		var result =  (String)properties.get("sqlFilePattern");
		if(StringUtils.isEmpty(result)) {
			logger.error("sqlFilePattern is not defined in dummyDataFactorySetting.properties");
		}
		
		return result;
	}
	
	public static String getSqlEndKeyword() {
		return (String)properties.get("sqlEndKeyword");
	}
}
