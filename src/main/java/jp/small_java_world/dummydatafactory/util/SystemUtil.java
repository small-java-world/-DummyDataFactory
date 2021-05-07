package jp.small_java_world.dummydatafactory.util;

import java.nio.file.Path;

public class SystemUtil {
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("windows");
	}
	
	public static Path getPath(String dirPath) {
		return Path.of(dirPath);
	}
}
