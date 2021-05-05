package jp.small_java_world.dummydatafactory.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import jp.small_java_world.dummydatafactory.RandomValueGenerator;
import jp.small_java_world.dummydatafactory.config.CommonConfig;
import jp.small_java_world.dummydatafactory.entity.DummyEntity;

class SqlFileUtilTest {

	@ParameterizedTest
	@ValueSource(strings = { "sqlEndKeyword", "" })
	void testGetSqlContent(String sqlEndKeyword) throws IOException {
		try (var commonConfigMock = Mockito.mockStatic(CommonConfig.class);
				var directoryUtilMock = Mockito.mockStatic(DirectoryUtil.class);
				var filesMock = Mockito.mockStatic(Files.class)) {

			commonConfigMock.when(() -> {
				CommonConfig.getSqlDirName();
			}).thenReturn("sqlDir");

			directoryUtilMock.when(() -> {
				DirectoryUtil.getPath("sqlDir");
			}).thenReturn("sqlDirPath");

			commonConfigMock.when(() -> {
				CommonConfig.getSqlFilePattern();
			}).thenReturn("create_$tableName.sql");

			var path = Path.of("sqlDirPath" + File.separator + "create_hoge_hoge.sql");
			filesMock.when(() -> {
				Files.exists(path);
			}).thenReturn(true);

			filesMock.when(() -> {
				Files.readString(path);
			}).thenReturn("sqlDummyContent" + sqlEndKeyword);

			commonConfigMock.when(() -> {
				CommonConfig.getSqlEndKeyword();
			}).thenReturn(sqlEndKeyword);

			assertEquals("sqlDummyContent", SqlFileUtil.getSqlContent("hogeHoge"));
		}

	}

}
