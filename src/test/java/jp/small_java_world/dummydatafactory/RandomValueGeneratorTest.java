package jp.small_java_world.dummydatafactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import jp.small_java_world.dummydatafactory.entity.RandomValueGeneratorTargetDto;
import jp.small_java_world.dummydatafactory.util.TestUtil;

class RandomValueGeneratorTest {
	private enum GenerateRandomValueTestType {
		STRING("memberString", "dummyString"), 
		INTEGER("memberInteger", 1), 
		PRIMITIVE_INT("memberInt", 2),
		LONG("memberLong", 3L),
		PRIMITIVE_LONG("memberLong", 4L),
		FLOAT("memberFloat", 5f),
		PRIMITIVE_FLOAT("memberfloat", 6f),
		SHORT("memberShort", (short) 7),
		PRIMITIVE_SHORT("membershort", (short) 8),
		BOOLEAN("memberBoolean", true),
		PRIMITIVE_BOOLEAN("memberboolean", false),
		DATE("memberDate", Calendar.getInstance().getTime()),
		TIMESTAMP("memberTimestamp", new Timestamp(Calendar.getInstance().getTime().getTime()));

		private final String targetMemberName;
		private final Object mockResult;

		private GenerateRandomValueTestType(final String targetMemberName, Object mockResult) {
			this.targetMemberName = targetMemberName;
			this.mockResult = mockResult;
		}
	}

	@ParameterizedTest
	@EnumSource(GenerateRandomValueTestType.class)
	void testGenerateRandomValue(GenerateRandomValueTestType testType) throws NoSuchFieldException {
		var targetField = TestUtil.getDeclaredField(RandomValueGeneratorTargetDto.class, testType.targetMemberName);

		try (var randomDataUtilMock = Mockito.mockStatic(RandomDataUtil.class)) {
			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomString(RandomValueGenerator.DAFAULT_DATA_SIZE);
			}).thenReturn(GenerateRandomValueTestType.STRING.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomInt();
			}).thenReturn(
					testType == GenerateRandomValueTestType.INTEGER ? GenerateRandomValueTestType.INTEGER.mockResult
							: GenerateRandomValueTestType.PRIMITIVE_INT.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomLong();
			}).thenReturn(testType == GenerateRandomValueTestType.LONG ? GenerateRandomValueTestType.LONG.mockResult
					: GenerateRandomValueTestType.PRIMITIVE_LONG.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomFloat();
			}).thenReturn(testType == GenerateRandomValueTestType.FLOAT ? GenerateRandomValueTestType.FLOAT.mockResult
					: GenerateRandomValueTestType.PRIMITIVE_FLOAT.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomShort();
			}).thenReturn(testType == GenerateRandomValueTestType.SHORT ? GenerateRandomValueTestType.SHORT.mockResult
					: GenerateRandomValueTestType.PRIMITIVE_SHORT.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomBool();
			}).thenReturn(
					testType == GenerateRandomValueTestType.BOOLEAN ? GenerateRandomValueTestType.BOOLEAN.mockResult
							: GenerateRandomValueTestType.PRIMITIVE_BOOLEAN.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomDate();
			}).thenReturn(GenerateRandomValueTestType.DATE.mockResult);

			randomDataUtilMock.when(() -> {
				RandomDataUtil.generateRandomTimestamp();
			}).thenReturn(GenerateRandomValueTestType.TIMESTAMP.mockResult);

			var result = RandomValueGenerator.generateRandomValue(targetField.getType(), null);
			assertEquals(testType.mockResult, result);
		}
	}
}
