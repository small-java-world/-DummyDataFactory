package jp.small_java_world.dummydatafactory.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.sf.jsqlparser.JSQLParserException;

class SqlAnalyzerTest {

	@Test
	void testGetSqlColumnDataMap() throws JSQLParserException {
		var result = SqlAnalyzer.getSqlColumnDataMap("CREATE TABLE todo( " 
				+ "	id integer not null,"
				+ " title character varying(32),"
				+ " content character varying(100),"
				+ "limit_time timestamp with time zone,"
				+ "regist_date date"
				+ ");");
		
		assertThat(result).hasSize(5);
		assertThat(result).containsKeys("id", "content", "title", "limitTime", "registDate");
		
		String targetKey = "id";
		assertThat(result.get(targetKey)).extracting("columnName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("columnCamelCaseName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("javaType").isEqualTo("Integer");
		assertThat(result.get(targetKey)).extracting("dbDataType").isEqualTo("integer");
		assertThat(result.get(targetKey)).extracting("dbDataSize").isNull();
		
		targetKey = "title";
		assertThat(result.get(targetKey)).extracting("columnName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("columnCamelCaseName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("javaType").isEqualTo("String");
		assertThat(result.get(targetKey)).extracting("dbDataType").isEqualTo("character varying");
		assertThat(result.get(targetKey)).extracting("dbDataSize").isEqualTo(32);
		
		targetKey = "content";
		assertThat(result.get(targetKey)).extracting("columnName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("columnCamelCaseName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("javaType").isEqualTo("String");
		assertThat(result.get(targetKey)).extracting("dbDataType").isEqualTo("character varying");
		assertThat(result.get(targetKey)).extracting("dbDataSize").isEqualTo(100);
		
		targetKey = "limitTime";
		assertThat(result.get(targetKey)).extracting("columnName").isEqualTo("limit_time");
		assertThat(result.get(targetKey)).extracting("columnCamelCaseName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("javaType").isEqualTo("Timestamp");
		assertThat(result.get(targetKey)).extracting("dbDataType").isEqualTo("timestamp with time zone");
		assertThat(result.get(targetKey)).extracting("dbDataSize").isNull();
		
		targetKey = "registDate";
		assertThat(result.get(targetKey)).extracting("columnName").isEqualTo("regist_date");
		assertThat(result.get(targetKey)).extracting("columnCamelCaseName").isEqualTo(targetKey);
		assertThat(result.get(targetKey)).extracting("javaType").isEqualTo("Date");
		assertThat(result.get(targetKey)).extracting("dbDataType").isEqualTo("date");
		assertThat(result.get(targetKey)).extracting("dbDataSize").isNull();
	}

}
