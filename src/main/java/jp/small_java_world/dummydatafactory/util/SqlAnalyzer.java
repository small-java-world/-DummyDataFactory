package jp.small_java_world.dummydatafactory.util;

import java.util.HashMap;
import java.util.Map;

import jp.small_java_world.dummydatafactory.SqlColumnData;
import jp.small_java_world.dummydatafactory.config.ColumnTypeConfig;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class SqlAnalyzer {
	public static Map<String, SqlColumnData> getSqlColumnDataMap(String sqlContent) throws JSQLParserException {
		Map<String, SqlColumnData> result = new HashMap<>();
		CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(sqlContent);

		for (var columnDefinition : createTable.getColumnDefinitions()) {
			SqlColumnData sqlColumnData = new SqlColumnData();
			
			var javaType = ColumnTypeConfig.getJavaType(columnDefinition.getColDataType().getDataType());
			sqlColumnData.setJavaType(javaType);
			sqlColumnData.setDbDataType(columnDefinition.getColDataType().getDataType());
			sqlColumnData.setColumnName(columnDefinition.getColumnName());
			sqlColumnData.setColumnCamelCaseName(StringConvertUtil.toSnakeCaseCase(columnDefinition.getColumnName()));
			
			var argumentsStringList = columnDefinition.getColDataType().getArgumentsStringList();
			if (argumentsStringList != null) {
				sqlColumnData.setDbDataSize(Integer.parseInt(argumentsStringList.get(0)));
			}
			
			result.put(sqlColumnData.getColumnCamelCaseName(), sqlColumnData);
		}
		return result;
	}
}
