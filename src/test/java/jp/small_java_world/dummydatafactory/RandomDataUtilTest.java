package jp.small_java_world.dummydatafactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import jp.small_java_world.dummydatafactory.util.RandomDataUtil;

class RandomDataUtilTest {
	// [ \]^_`などはASCIIコードの0からz間でもLetterOrDigitでないので、これらが除外さていることを確認するときに利用
	final List<Integer> EXCLUDE_LETTER_OR_DIGIT_LIST = List.of(58, 59, 60, 61, 62, 64, 91, 92, 93, 94, 95, 96);

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 10 })
	void testGenerateRandomString(int length) {
		var result1 = RandomDataUtil.generateRandomString(length);
		assertThat(result1).hasSize(length);

		// result1が文字コード'0'と'z'の間に含まれていて、EXCLUDE_LETTER_OR_DIGIT_LISTに含まれていないことを検証
		assertContainChar(result1, '0', 'z', EXCLUDE_LETTER_OR_DIGIT_LIST);

		var result2 = RandomDataUtil.generateRandomString(length);
		assertThat(result2).hasSize(length);
		assertContainChar(result2, '0', 'z', EXCLUDE_LETTER_OR_DIGIT_LIST);

		// result1とresult2が一致しないこと
		assertThat(result2).isNotEqualTo(result1);
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 20 })
	void testGenerateRandomHexString(int length) {
		var result1 = RandomDataUtil.generateRandomHexString(length);
		assertThat(result1).hasSize(length);
		// result1が文字コード'0'と'F'の間に含まれていて、EXCLUDE_LETTER_OR_DIGIT_LISTに含まれていないことを検証
		assertContainChar(result1, '0', 'F', EXCLUDE_LETTER_OR_DIGIT_LIST);
		// 先頭は0以外の数字
		assertContainChar(result1.substring(0, 1), '1', 'F');

		var result2 = RandomDataUtil.generateRandomHexString(length);
		assertThat(result2).hasSize(length);
		assertContainChar(result2, '0', 'F', EXCLUDE_LETTER_OR_DIGIT_LIST);
		// 先頭は0以外の数字
		assertContainChar(result2.substring(0, 1), '1', 'F');

		// result1とresult2が一致しないこと
		assertThat(result2).isNotEqualTo(result1);
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 20 })
	void testGenerateRandomNumberString(int length) {
		var result1 = RandomDataUtil.generateRandomNumberString(length);
		assertThat(result1).hasSize(length);
		// result1が文字コード'0'と'9'の間に含まれていることを検証
		assertContainChar(result1, '0', '9');
		// 先頭は0以外の数字
		assertContainChar(result1.substring(0, 1), '1', '9');

		var result2 = RandomDataUtil.generateRandomNumberString(length);
		assertThat(result2).hasSize(length);
		assertContainChar(result1, '0', '9');
		// 先頭は0以外の数字
		assertContainChar(result2.substring(0, 1), '1', '9');

		// result1とresult2が一致しないこと
		assertThat(result2).isNotEqualTo(result1);
	}

	private void assertContainChar(String target, char startChar, char endChar) {
		assertContainChar(target, startChar, endChar, null);
	}

	private void assertContainChar(String target, char startChar, char endChar, List<Integer> excludeList) {
		int start = (int) startChar;
		int end = (int) endChar;

		char[] charArray = target.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			int current = (int) charArray[i];
			if (current < start || end < current) {
				fail(String.format("%dは範囲(%d-%d)の文字コードに含まれない", current, start, end));
			} else if (excludeList != null && excludeList.contains(current)) {
				fail(String.format("%dはexcludeListに含まれる文字コード", current));
			}
		}
	}

	private enum RandomDateTestType {
		DATE, TIMESTAMP;
	}

	@ParameterizedTest
	@EnumSource(RandomDateTestType.class)
	void testGenerateRandomDate(RandomDateTestType testType) {
		// 期待値の上限=now+365dayに10秒を足したDate
		// Calendar.getInstance()をテストで呼び出すタイミングと
		// RandomDataUtil.generateRandomDate()で呼び出すタイミングに時差があるので
		// 時差でテストが期待した動作にならずテストが失敗するパターンを排除するために10秒の猶予を設けています。
		var calendarMax = Calendar.getInstance();
		calendarMax.add(Calendar.DATE, 365);
		calendarMax.add(Calendar.SECOND, 10);
		var maxDate = calendarMax.getTime();

		// 期待値の下限=now+365dayに-10秒を足したDate
		var calendarMin = Calendar.getInstance();
		calendarMin.add(Calendar.DATE, -365);
		calendarMin.add(Calendar.SECOND, -10);
		var minDate = calendarMin.getTime();

		Date result1 = null;

		if (testType == RandomDateTestType.DATE) {
			result1 = RandomDataUtil.generateRandomDate();
			assertFalse(result1 instanceof Timestamp);
		} else if (testType == RandomDateTestType.TIMESTAMP) {
			result1 = RandomDataUtil.generateRandomTimestamp();
			assertTrue(result1 instanceof Timestamp);
		}
		// calendarMaxの方がresult1より未来
		assertTrue(maxDate.compareTo(result1) > 0);
		// calendarMinの方がresult1より過去
		assertTrue(result1.compareTo(minDate) > 0);

		Date result2 = null;
		if (testType == RandomDateTestType.DATE) {
			result2 = RandomDataUtil.generateRandomDate();
		} else if (testType == RandomDateTestType.TIMESTAMP) {
			result2 = RandomDataUtil.generateRandomTimestamp();
		}
		// calendarMaxの方がresult2より未来
		assertTrue(maxDate.compareTo(result2) > 0);
		// calendarMinの方がresult2より過去
		assertTrue(result2.compareTo(minDate) > 0);

		// result1とresult2が一致しないこと
		assertThat(result2).isNotEqualTo(result1);
	}

	@Test
	void testGenerateRandomInt() {
		var result1 = RandomDataUtil.generateRandomInt();
		var result2 = RandomDataUtil.generateRandomInt();
		var result3 = RandomDataUtil.generateRandomInt();

		assertTrue(result1 instanceof Integer);
		assertThat(result1).isNotEqualTo(result2);
		assertThat(result1).isNotEqualTo(result3);
	}

	@Test
	void testGenerateRandomLong() {
		var result1 = RandomDataUtil.generateRandomLong();
		var result2 = RandomDataUtil.generateRandomLong();
		var result3 = RandomDataUtil.generateRandomLong();

		assertTrue(result1 instanceof Long);
		assertThat(result1).isNotEqualTo(result2);
		assertThat(result1).isNotEqualTo(result3);
	}

	@Test
	void testGenerateRandomShort() {
		var result1 = RandomDataUtil.generateRandomShort();
		var result2 = RandomDataUtil.generateRandomShort();
		var result3 = RandomDataUtil.generateRandomShort();

		assertTrue(result1 instanceof Short);
		assertThat(result1).isNotEqualTo(result2);
		assertThat(result1).isNotEqualTo(result3);
	}

}
