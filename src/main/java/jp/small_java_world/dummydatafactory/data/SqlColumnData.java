package jp.small_java_world.dummydatafactory.data;

public class SqlColumnData {
	String columnName;
	String columnCamelCaseName;
	String javaType;
	String dbDataType;
	Integer dbDataSize;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnCamelCaseName() {
		return columnCamelCaseName;
	}

	public void setColumnCamelCaseName(String columnCamelCaseName) {
		this.columnCamelCaseName = columnCamelCaseName;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getDbDataType() {
		return dbDataType;
	}

	public void setDbDataType(String dbDataType) {
		this.dbDataType = dbDataType;
	}

	public Integer getDbDataSize() {
		return dbDataSize;
	}

	public void setDbDataSize(Integer dbDataSize) {
		this.dbDataSize = dbDataSize;
	}

}
