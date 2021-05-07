package jp.small_java_world.dummydatafactory.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.RandomStringGenerator;

public class RandomDataUtil {
	static Set<Date> randomDateCheckMap = new HashSet<Date>();
	static Set<Timestamp> randomTimestampCheckMap = new HashSet<Timestamp>();
	static Set<Long> randomLongCheckMap = new HashSet<Long>();
	static Set<Integer> randomIntCheckMap = new HashSet<Integer>();
	static Set<Short> randomShortCheckMap = new HashSet<Short>();
	static Set<String> randomStringCheckMap = new HashSet<String>();

	static final int MAX_RETRY = 10;

	public static String generateRandomString(final int length) {
		return generateRandomString(length, '0', 'z', (char) 0);
	}

	public static String generateRandomHexString(final int length) {
		return generateRandomString(length, '0', 'F', '1');
	}

	public static String generateRandomNumberString(final int length) {
		return generateRandomString(length, '0', '9', '1');
	}

	public static String generateRandomString(final int length, final char start, final char end,
			final char firstStart) {
		int retryCount = 0;
		String firstStr = "", result = "";

		while (true) {
			// firstStartがasciiコードの0でなければ先頭の1文字目と2文字目以降の生成を別に行う。
			if (firstStart != 0) {
				firstStr = generateRandomLetterOrDigit(firstStart, end, 1);
				result = length == 1 ? firstStr : firstStr + generateRandomLetterOrDigit(start, end, length - 1);
			} else {
				result = generateRandomLetterOrDigit(start, end, length);
			}

			if (!randomStringCheckMap.contains(result)) {
				randomStringCheckMap.add(result);
				return result;
			}

			if (retryCount++ > MAX_RETRY) {
				return result;
			}
		}
	}

	private static String generateRandomLetterOrDigit(char start, char end, final int length) {
		return new RandomStringGenerator.Builder().withinRange(start, end).filteredBy(Character::isLetterOrDigit)
				.build().generate(length);
	}

	public static Date generateRandomDate() {
		int retryCount = 0;
		
		while (true) {
			var calendar = Calendar.getInstance();
			boolean isAdd = RandomUtils.nextBoolean();
			calendar.add(Calendar.DATE, isAdd ? RandomUtils.nextInt(1, 365) : -1 * RandomUtils.nextInt(1, 365));

			if (!randomDateCheckMap.contains(calendar.getTime())) {
				randomDateCheckMap.add(calendar.getTime());
				return calendar.getTime();
			}
			
			if (retryCount++ > MAX_RETRY) {
				return calendar.getTime();
			}
		}
	}

	public static Timestamp generateRandomTimestamp() {
		int retryCount = 0;
		
		while (true) {
			var result = new Timestamp(generateRandomDate().getTime());

			if (!randomTimestampCheckMap.contains(result)) {
				randomTimestampCheckMap.add(result);
				return result;
			}
			
			if (retryCount++ > MAX_RETRY) {
				return result;
			}
		}
	}

	public static Integer generateRandomInt() {
		int retryCount = 0;
		
		while (true) {
			Integer result = RandomUtils.nextInt();
			if (!randomIntCheckMap.contains(result)) {
				randomIntCheckMap.add(result);
				return result;
			}
			
			if (retryCount++ > MAX_RETRY) {
				return result;
			}
		}
	}

	public static Long generateRandomLong() {
		int retryCount = 0;
		
		while (true) {
			Long result = RandomUtils.nextLong();
			if (!randomLongCheckMap.contains(result)) {
				randomLongCheckMap.add(result);
				return result;
			}
			
			if (retryCount++ > MAX_RETRY) {
				return result;
			}
		}
	}

	public static Float generateRandomFloat() {
		var randInt1 = generateRandomInt();
		var randInt2 = generateRandomInt();
		return (float) (randInt1 / randInt2);
	}

	public static boolean generateRandomBool() {
		return RandomUtils.nextBoolean();
	}

	public static Short generateRandomShort() {
		int retryCount = 0;
		
		while (true) {
			Short result = (short) RandomUtils.nextInt(0, Short.MAX_VALUE);
			if (!randomShortCheckMap.contains(result)) {
				randomShortCheckMap.add(result);
				return result;
			}
			
			if (retryCount++ > MAX_RETRY) {
				return result;
			}
		}
	}
}