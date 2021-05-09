package jp.small_java_world.dummydatafactory.util;

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


class RandomDataUtilTest {
	// [ \]^_`�Ȃǂ�ASCII�R�[�h��0����z�Ԃł�LetterOrDigit�łȂ��̂ŁA����炪���O���Ă��邱�Ƃ��m�F����Ƃ��ɗ��p
	final List<Integer> EXCLUDE_LETTER_OR_DIGIT_LIST = List.of(58, 59, 60, 61, 62, 64, 91, 92, 93, 94, 95, 96);

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 10 })
	void testGenerateRandomString(int length) {
		var result1 = RandomDataUtil.generateRandomString(length);
		assertThat(result1).hasSize(length);

		// result1�������R�[�h'0'��'z'�̊ԂɊ܂܂�Ă��āAEXCLUDE_LETTER_OR_DIGIT_LIST�Ɋ܂܂�Ă��Ȃ����Ƃ�����
		assertContainChar(result1, '0', 'z', EXCLUDE_LETTER_OR_DIGIT_LIST);

		var result2 = RandomDataUtil.generateRandomString(length);
		assertThat(result2).hasSize(length);
		assertContainChar(result2, '0', 'z', EXCLUDE_LETTER_OR_DIGIT_LIST);

		// result1��result2����v���Ȃ�����
		assertThat(result2).isNotEqualTo(result1);
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 20 })
	void testGenerateRandomHexString(int length) {
		var result1 = RandomDataUtil.generateRandomHexString(length);
		assertThat(result1).hasSize(length);
		// result1�������R�[�h'0'��'F'�̊ԂɊ܂܂�Ă��āAEXCLUDE_LETTER_OR_DIGIT_LIST�Ɋ܂܂�Ă��Ȃ����Ƃ�����
		assertContainChar(result1, '0', 'F', EXCLUDE_LETTER_OR_DIGIT_LIST);
		// �擪��0�ȊO
		assertContainChar(result1.substring(0, 1), '1', 'F', EXCLUDE_LETTER_OR_DIGIT_LIST);

		var result2 = RandomDataUtil.generateRandomHexString(length);
		assertThat(result2).hasSize(length);
		assertContainChar(result2, '0', 'F', EXCLUDE_LETTER_OR_DIGIT_LIST);
		// �擪��0�ȊO
		assertContainChar(result2.substring(0, 1), '1', 'F', EXCLUDE_LETTER_OR_DIGIT_LIST);

		// result1��result2����v���Ȃ�����
		assertThat(result2).isNotEqualTo(result1);
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 20 })
	void testGenerateRandomNumberString(int length) {
		var result1 = RandomDataUtil.generateRandomNumberString(length);
		assertThat(result1).hasSize(length);
		// result1�������R�[�h'0'��'9'�̊ԂɊ܂܂�Ă��邱�Ƃ�����
		assertContainChar(result1, '0', '9');
		// �擪��0�ȊO�̐���
		assertContainChar(result1.substring(0, 1), '1', '9');

		var result2 = RandomDataUtil.generateRandomNumberString(length);
		assertThat(result2).hasSize(length);
		assertContainChar(result1, '0', '9');
		// �擪��0�ȊO�̐���
		assertContainChar(result2.substring(0, 1), '1', '9');

		// result1��result2����v���Ȃ�����
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
				fail(String.format("%d�͔͈�(%d-%d)�̕����R�[�h�Ɋ܂܂�Ȃ�", current, start, end));
			} else if (excludeList != null && excludeList.contains(current)) {
				fail(String.format("%d��excludeList�Ɋ܂܂�镶���R�[�h", current));
			}
		}
	}

	private enum RandomDateTestType {
		DATE, TIMESTAMP;
	}

	@ParameterizedTest
	@EnumSource(RandomDateTestType.class)
	void testGenerateRandomDate(RandomDateTestType testType) {
		// ���Ғl�̏��=now+365day��10�b�𑫂���Date
		// Calendar.getInstance()���e�X�g�ŌĂяo���^�C�~���O��
		// RandomDataUtil.generateRandomDate()�ŌĂяo���^�C�~���O�Ɏ���������̂�
		// �����Ńe�X�g�����҂�������ɂȂ炸�e�X�g�����s����p�^�[����r�����邽�߂�10�b�̗P�\��݂��Ă��܂��B
		var calendarMax = Calendar.getInstance();
		calendarMax.add(Calendar.DATE, 365);
		calendarMax.add(Calendar.SECOND, 10);
		var maxDate = calendarMax.getTime();

		// ���Ғl�̉���=now+365day��-10�b�𑫂���Date
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
		// calendarMax�̕���result1��薢��
		assertTrue(maxDate.compareTo(result1) > 0);
		// calendarMin�̕���result1���ߋ�
		assertTrue(result1.compareTo(minDate) > 0);

		Date result2 = null;
		if (testType == RandomDateTestType.DATE) {
			result2 = RandomDataUtil.generateRandomDate();
		} else if (testType == RandomDateTestType.TIMESTAMP) {
			result2 = RandomDataUtil.generateRandomTimestamp();
		}
		// calendarMax�̕���result2��薢��
		assertTrue(maxDate.compareTo(result2) > 0);
		// calendarMin�̕���result2���ߋ�
		assertTrue(result2.compareTo(minDate) > 0);

		// result1��result2����v���Ȃ�����
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
		assertThat(result2).isNotEqualTo(result3);
	}

	@Test
	void testGenerateRandomLong() {
		var result1 = RandomDataUtil.generateRandomLong();
		var result2 = RandomDataUtil.generateRandomLong();
		var result3 = RandomDataUtil.generateRandomLong();

		assertTrue(result1 instanceof Long);
		assertThat(result1).isNotEqualTo(result2);
		assertThat(result1).isNotEqualTo(result3);
		assertThat(result2).isNotEqualTo(result3);
	}

	@Test
	void testGenerateRandomShort() {
		var result1 = RandomDataUtil.generateRandomShort();
		var result2 = RandomDataUtil.generateRandomShort();
		var result3 = RandomDataUtil.generateRandomShort();

		assertTrue(result1 instanceof Short);
		assertThat(result1).isNotEqualTo(result2);
		assertThat(result1).isNotEqualTo(result3);
		assertThat(result2).isNotEqualTo(result3);
	}

}
