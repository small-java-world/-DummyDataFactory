package jp.small_java_world.dummydatafactory.util;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryUtil {
	private static final Logger logger = LoggerFactory.getLogger(DirectoryUtil.class);

	public static String getPath(String dirName) {
		logger.debug("getPath dirName={} start", dirName);

		URL url = DirectoryUtil.class.getClassLoader().getResource(".");

		if (url == null) {
			return null;
		}

		StringBuilder upperPathPrefix = new StringBuilder(".." + File.separator);
		File targetDirFile = null;

		var rootPath = url.getPath();
		int counter = 0;
		while (true) {
			String currentTargetPath = rootPath + upperPathPrefix + dirName;
			targetDirFile = new File(currentTargetPath);
			if (targetDirFile.exists()) {
				logger.debug("{} exist!!", currentTargetPath);
				break;
			}

			if (counter > 5) {
				logger.error("getPath fail");
				return null;
			}

			upperPathPrefix.append(".." + File.separator);
			counter++;
		}

		return targetDirFile.getPath();
	}
}
