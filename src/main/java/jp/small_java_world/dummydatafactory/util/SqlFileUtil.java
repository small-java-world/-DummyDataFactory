package jp.small_java_world.dummydatafactory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.small_java_world.dummydatafactory.config.CommonConfig;

public class SqlFileUtil {
	private static final Logger logger = LoggerFactory.getLogger(SqlFileUtil.class);

	public static String getSqlContent(String targetClassSimpleName) throws IOException {
		var sqlFileDir = DirectoryUtil.getPath(CommonConfig.getSqlDirName());
		var tableName = StringConvertUtil.toCamelCase(targetClassSimpleName).toLowerCase();
		var createSqlFileName = CommonConfig.getSqlFilePattern().replace("$tableName", tableName);
		var createSqlFilePath = Path.of(sqlFileDir + File.separator + createSqlFileName);

		var sqlEndKeyword = CommonConfig.getSqlEndKeyword();
		if (Files.exists(createSqlFilePath)) {
			var sqlContent = Files.readString(createSqlFilePath);
			if (StringUtils.isNotEmpty(sqlEndKeyword) && sqlContent.contains(sqlEndKeyword)) {
				sqlContent = sqlContent.substring(0, sqlContent.indexOf(sqlEndKeyword));
			}

			logger.debug("getSqlContent return value {}", sqlContent);
			return sqlContent;
		} else {
			logger.error("not exist createSqlFile={}", createSqlFilePath);
			return null;
		}
	}

}
