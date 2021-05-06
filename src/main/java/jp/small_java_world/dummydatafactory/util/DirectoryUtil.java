package jp.small_java_world.dummydatafactory.util;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryUtil {
	private static final Logger logger = LoggerFactory.getLogger(DirectoryUtil.class);

	/**
	 * dirNameに階層を指定する場合は/を指定してください。
	 * 
	 * @param dirName
	 * @return 存在するdirNameのプロジェクト/bin/main/からの相対パス
	 */
	public static String getPath(String dirName) {
		logger.debug("getPath dirName={} start", dirName);

		URL url = DirectoryUtil.class.getClassLoader().getResource(".");

		if (url == null) {
			return null;
		}

		StringBuilder upperPathPrefix = new StringBuilder(".." + File.separator);

		var rootPath = url.getPath();
		
		//Windowsの場合は/c:のようなパスになるので、Path.ofなどがうまく動作しないので先頭の/を除去
		if(SystemUtil.isWindows() && rootPath.startsWith("/")) {
			rootPath = rootPath.substring(1, rootPath.length());
		}
		
		String currentTargetPath = null;
		int counter = 0;
		while (true) {
			currentTargetPath = rootPath + upperPathPrefix + dirName.replace("/", File.separator);
			if (Files.exists(Path.of(currentTargetPath))) {
				break;
			}

			if (counter > 5) {
				logger.error("getPath fail");
				return null;
			}

			upperPathPrefix.append(".." + File.separator);
			counter++;
		}

		logger.debug("getPath result={}", currentTargetPath);
		return currentTargetPath;
	}
}
