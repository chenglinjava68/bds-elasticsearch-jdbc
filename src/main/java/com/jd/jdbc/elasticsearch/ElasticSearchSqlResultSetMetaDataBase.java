package com.jd.jdbc.elasticsearch;

import com.google.common.collect.Lists;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghong12
 * @date 2018-4-20
 */
public class ElasticSearchSqlResultSetMetaDataBase implements ResultSetMetaData {
    List<String> columns = Lists.newArrayList();
    public ElasticSearchSqlResultSetMetaDataBase(List<String> headers){
        this.columns = headers;
    }
    @Override
    public int getColumnType(int column) {
        return Types.LONGVARCHAR;
    }
    @Override
    public String getColumnClassName(int column) {
        return "java.lang.String";
    }
    @Override
    public String getColumnLabel(int column) {
        return columns.get(column-1);
    }

    @Override
    public String getColumnName(int column) {
        return columns.get(column-1);
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnTypeName(int column) {
        return null;
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
