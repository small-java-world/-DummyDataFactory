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

	public static String generateRandomString(final int length) {
		final char start = '0', end = 'z';

		while (true) {
			String result = generateRandomLetterOrDigit(start, end, length);
			if (!randomStringCheckMap.contains(result)) {
				randomStringCheckMap.add(result);
				return result;
			}
		}
	}

	public static String generateRandomHexString(final int length) {
		final char start = '0', end = 'F';

		while (true) {
			String first = generateRandomLetterOrDigit('1', '9', 1);
			String result = generateRandomLetterOrDigit(start, end, length - 1);
			if (!randomStringCheckMap.contains(first + result)) {
				randomStringCheckMap.add(first + result);
				return first + result;
			}
		}
	}

	public static String generateRandomNumberString(final int length) {
		final char start = '0', end = '9';

		while (true) {
			String first = generateRandomLetterOrDigit('1', end, 1);
			String result = generateRandomLetterOrDigit(start, end, length - 1);
			if (!randomStringCheckMap.contains(first + result)) {
				randomStringCheckMap.add(first + result);
				return first + result;
			}
		}
	}

	private static String generateRandomLetterOrDigit(char start, char end, final int length) {
		return new RandomStringGenerator.Builder().withinRange(start, end).filteredBy(Character::isLetterOrDigit)
				.build().generate(length);
	}

	public static Date generateRandomDate() {
		while (true) {
			var calendar = Calendar.getInstance();
			boolean isAdd = RandomUtils.nextBoolean();
			calendar.add(Calendar.DATE, isAdd ? RandomUtils.nextInt(1, 365) : -1 * RandomUtils.nextInt(1, 365));

			if (!randomDateCheckMap.contains(calendar.getTime())) {
				randomDateCheckMap.add(calendar.getTime());
				return calendar.getTime();
			}
		}
	}

	public static Timestamp generateRandomTimestamp() {
		while (true) {
			var result = new Timestamp(generateRandomDate().getTime());

			if (!randomTimestampCheckMap.contains(result)) {
				randomTimestampCheckMap.add(result);
				return result;
			}
		}
	}

	public static Integer generateRandomInt() {
		while (true) {
			Integer result = RandomUtils.nextInt();
			if (!randomIntCheckMap.contains(result)) {
				randomIntCheckMap.add(result);
				return result;
			}
		}
	}

	public static Long generateRandomLong() {
		while (true) {
			Long result = RandomUtils.nextLong();
			if (!randomLongCheckMap.contains(result)) {
				randomLongCheckMap.add(result);
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
		while (true) {
			Short result = (short) RandomUtils.nextInt(0, Short.MAX_VALUE);
			if (!randomShortCheckMap.contains(result)) {
				randomShortCheckMap.add(result);
				return result;
			}
		}
	}

}
