package jp.small_java_world.dummydatafactory.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.sf.jsqlparser.JSQLParserException;

class SqlAnalyzerTest {

	@Test
	void testGetSqlColumnDataMap() throws JSQLParserException {
		var result = SqlAnalyzer.getSqlColumnDataMap("CREATE TABLE todo( " + " id integer not null,"
				+ " title character varying(32)," + " content character varying(100),"
				+ "limit_time timestamp with time zone," + "regist_date date" + ");");

		assertThat(result).hasSize(5);
		assertThat(result).containsKeys("id", "content", "title", "limitTime", "registDate");

		String targetKey = "id";
		assertThat(result.get(targetKey).getColumnName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getColumnCamelCaseName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getJavaType()).isEqualTo("Integer");
		assertThat(result.get(targetKey).getDbDataType()).isEqualTo("integer");
		assertThat(result.get(targetKey).getDbDataSize()).isNull();

		targetKey = "title";
		assertThat(result.get(targetKey).getColumnName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getColumnCamelCaseName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getJavaType()).isEqualTo("String");
		assertThat(result.get(targetKey).getDbDataType()).isEqualTo("character varying");
		assertThat(result.get(targetKey).getDbDataSize()).isEqualTo(32);

		targetKey = "content";
		assertThat(result.get(targetKey).getColumnName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getColumnCamelCaseName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getJavaType()).isEqualTo("String");
		assertThat(result.get(targetKey).getDbDataType()).isEqualTo("character varying");
		assertThat(result.get(targetKey).getDbDataSize()).isEqualTo(100);

		targetKey = "limitTime";
		assertThat(result.get(targetKey).getColumnName()).isEqualTo("limit_time");
		assertThat(result.get(targetKey).getColumnCamelCaseName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getJavaType()).isEqualTo("Timestamp");
		assertThat(result.get(targetKey).getDbDataType()).isEqualTo("timestamp with time zone");
		assertThat(result.get(targetKey).getDbDataSize()).isNull();

		targetKey = "registDate";
		assertThat(result.get(targetKey).getColumnName()).isEqualTo("regist_date");
		assertThat(result.get(targetKey).getColumnCamelCaseName()).isEqualTo(targetKey);
		assertThat(result.get(targetKey).getJavaType()).isEqualTo("Date");
		assertThat(result.get(targetKey).getDbDataType()).isEqualTo("date");
		assertThat(result.get(targetKey).getDbDataSize()).isNull();
	}

}
