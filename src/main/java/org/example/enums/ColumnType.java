package org.example.enums;

public enum ColumnType {
    STRING("String", "VARCHAR", "255"),
    INTEGER("Integer", "INTEGER", null),
    BOOLEAN("Boolean", "BOOLEAN", null),
    DATE("LocalDate", "DATE", null),
    MONEY("BigDecimal", "NUMERIC", "15,2");

    private final String javaType;
    private final String sqlType;
    private final String defaultSize;

    ColumnType(String javaType, String sqlType, String defaultSize) {
        this.javaType = javaType;
        this.sqlType = sqlType;
        this.defaultSize = defaultSize;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public String getDefaultSize() {
        return defaultSize;
    }
}
