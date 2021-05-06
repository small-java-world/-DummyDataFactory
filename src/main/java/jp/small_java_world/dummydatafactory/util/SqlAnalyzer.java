package jp.small_java_world.dummydatafactory.util;

import java.util.HashMap;
import java.util.Map;

import jp.small_java_world.dummydatafactory.config.ColumnTypeConfig;
import jp.small_java_world.dummydatafactory.data.SqlColumnData;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class SqlAnalyzer {
	public static Map<String, SqlColumnData> getSqlColumnDataMap(String sqlContent) throws JSQLParserException {
		//�L�[:�J�����ɑΉ�����Java�̃N���X�̃����o���A�l:�J�����ɑΉ�����SqlColumnData
		Map<String, SqlColumnData> result = new HashMap<>();
		
		//create table��sqlContent�����
		CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(sqlContent);

		//��͌��ʂ���columnDefinitions�����o���B
		for (var columnDefinition : createTable.getColumnDefinitions()) {
			SqlColumnData sqlColumnData = new SqlColumnData();

			//javaType��columnType.yml�ɒ�`���Ă���ݒ�ŕϊ����ăZ�b�g
			var javaType = ColumnTypeConfig.getJavaType(columnDefinition.getColDataType().getDataType());
			sqlColumnData.setJavaType(javaType);
			sqlColumnData.setDbDataType(columnDefinition.getColDataType().getDataType());
			sqlColumnData.setColumnName(columnDefinition.getColumnName());
		
			//Java�̃N���X�̃����o���̓e�[�u���̃J���������L�������P�[�X�ɕϊ����ăZ�b�g
			sqlColumnData.setColumnCamelCaseName(StringConvertUtil.toSnakeCaseCase(columnDefinition.getColumnName()));

			//�J�����T�C�Y���Z�b�g
			var argumentsStringList = columnDefinition.getColDataType().getArgumentsStringList();
			if (argumentsStringList != null) {
				sqlColumnData.setDbDataSize(Integer.parseInt(argumentsStringList.get(0)));
			}

			result.put(sqlColumnData.getColumnCamelCaseName(), sqlColumnData);
		}
		return result;
	}
}
