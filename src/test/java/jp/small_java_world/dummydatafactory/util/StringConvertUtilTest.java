package jp.small_java_world.dummydatafactory.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringConvertUtilTest {
	@ParameterizedTest
	@CsvSource(value = {"aaa,aaa", "aa_b,aaB", "aaa_b_bb_ccc,aaaBBbCcc"})
	void testToSnakeCaseCase(String input, String expected) {
		assertEquals(expected, StringConvertUtil.toSnakeCaseCase(input));
	}
	
	@ParameterizedTest
	@CsvSource(value = {"aaa,aaa", "aaB,aa_b,", "aaaBBbCcc,aaa_b_bb_ccc"})
	void testToCamelCase(String input, String expected) {
		assertEquals(expected, StringConvertUtil.toCamelCase(input));
	}
}
