package jp.small_java_world.dummydatafactory.util;

public class SystemUtil {
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("windows");
	}
}
