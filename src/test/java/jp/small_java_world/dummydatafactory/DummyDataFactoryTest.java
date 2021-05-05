package jp.small_java_world.dummydatafactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import jp.small_java_world.dummydatafactory.entity.DummyEntity;
import jp.small_java_world.dummydatafactory.util.SqlAnalyzer;
import jp.small_java_world.dummydatafactory.util.SqlFileUtil;
import jp.small_java_world.dummydatafactory.util.TestUtil;

class DummyDataFactoryTest {
	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testGenerateDummyInstance(boolean isEntity) throws Exception {
		var integerMemberField = TestUtil.getDeclaredField(DummyEntity.class, "integerMember");
		var shortMemberMemberField = TestUtil.getDeclaredField(DummyEntity.class, "shortMember");

		try (var randomValueGeneratorMock = Mockito.mockStatic(RandomValueGenerator.class);
				var sqlFileUtilMock = Mockito.mockStatic(SqlFileUtil.class);
				var sqlAnalyzerMock = Mockito.mockStatic(SqlAnalyzer.class)) {

			SqlColumnData integerMemberSqlColumnData = isEntity ? new SqlColumnData() : null;
			SqlColumnData shortMemberSqlColumnData = isEntity ? new SqlColumnData() : null;

			if (isEntity) {
				// SqlFileUtil.getSqlContent("dummyEntity")�̐U�镑�����`
				sqlFileUtilMock.when(() -> {
					SqlFileUtil.getSqlContent(DummyEntity.class.getSimpleName());
				}).thenReturn("dummy create sql");

				// SqlAnalyzer.getSqlColumnDataMap("dummy create sql")�̐U�镑�����`
				sqlAnalyzerMock.when(() -> {
					SqlAnalyzer.getSqlColumnDataMap("dummy create sql");
				}).thenReturn(
						Map.of("integerMember", integerMemberSqlColumnData, "shortMember", shortMemberSqlColumnData));
			}

			// DummyEntity��integerMember�ɑ΂���_�~�[�f�[�^�̐������̐U�镑�����`
			randomValueGeneratorMock.when(() -> {
				RandomValueGenerator.generateRandomValue(integerMemberField.getType(), integerMemberSqlColumnData);
			}).thenReturn(100);

			// DummyEntity��shortMember�ɑ΂���_�~�[�f�[�^�̐������̐U�镑�����`
			randomValueGeneratorMock.when(() -> {
				RandomValueGenerator.generateRandomValue(shortMemberMemberField.getType(), shortMemberSqlColumnData);
			}).thenReturn((short) 101);

			// DummyDataFactory.generateDummyInstance(DummyEntity.class, isEntity)���Ăяo���Ēl�̌���
			var result = DummyDataFactory.generateDummyInstance(DummyEntity.class, isEntity);
			assertThat(result.getIntegerMember()).isEqualTo(100);
			assertThat(result.getShortMember()).isEqualTo((short) 101);

			// ���b�N��verify
			// SqlFileUtil.getSqlContent��SqlAnalyzer.getSqlColumnDataMap��isEntity=true�̂Ƃ��̂݌Ăяo�����B
			sqlFileUtilMock.verify(() -> SqlFileUtil.getSqlContent(DummyEntity.class.getSimpleName()),
					times(isEntity ? 1 : 0));
			sqlAnalyzerMock.verify(() -> SqlAnalyzer.getSqlColumnDataMap("dummy create sql"), times(isEntity ? 1 : 0));

			randomValueGeneratorMock.verify(() -> RandomValueGenerator.generateRandomValue(integerMemberField.getType(),
					integerMemberSqlColumnData), times(1));
			randomValueGeneratorMock.verify(() -> RandomValueGenerator
					.generateRandomValue(shortMemberMemberField.getType(), shortMemberSqlColumnData), times(1));

		}
	}
}
