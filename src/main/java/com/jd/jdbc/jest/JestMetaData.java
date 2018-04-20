package com.jd.jdbc.jest;

import io.searchbox.client.JestResult;

import java.sql.ResultSetMetaData;
import java.sql.Types;

/**
 * @author wanghong12
 * @date 2018-4-20
 */
public class JestMetaData implements ResultSetMetaData {
    public static final String COLUMN_LABEL = "jest_result";
    public static final String COLUMN_NAME = "jest_result";

    public JestMetaData(JestResult jestResult) {

    }

    @Override
    public int getColumnType(int column) {
        /*返回Types.OTHER，java会通过getObject方法获取*/
        return Types.OTHER;
    }

    @Override
    public String getColumnClassName(int column) {
        return "java.lang.Object";
    }

    @Override
    public String getColumnLabel(int column) {
        return COLUMN_LABEL;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAME;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public boolean isAutoIncrement(int column) {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) {
        return false;
    }

    @Override
    public boolean isSearchable(int column) {
        return false;
    }

    @Override
    public boolean isCurrency(int column) {
        return false;
    }

    @Override
    public int isNullable(int column) {
        return 0;
    }

    @Override
    public boolean isSigned(int column) {
        return false;
    }

    @Override
    public int getColumnDisplaySize(int column) {
        return 0;
    }

    @Override
    public String getSchemaName(int column) {
        return null;
    }

    @Override
    public int getPrecision(int column) {
        return 0;
    }

    @Override
    public int getScale(int column) {
        return 0;
    }

    @Override
    public String getTableName(int column) {
        return null;
    }

    @Override
    public String getCatalogName(int column) {
        return null;
    }

    @Override
    public String getColumnTypeName(int column) {
        return null;
    }

    @Override
    public boolean isReadOnly(int column) {
        return false;
    }

    @Override
    public boolean isWritable(int column) {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
