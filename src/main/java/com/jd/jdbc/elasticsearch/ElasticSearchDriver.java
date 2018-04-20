package com.jd.jdbc.elasticsearch;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author wanghong12
 * @date 2018-4-20
 */
public class ElasticSearchDriver implements Driver {

    static {
        try {
            DriverManager.registerDriver(new ElasticSearchDriver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ElasticSearchDriver() {}

    @Override
    public Connection connect(String url, Properties info) {
        return new ElasticSearchConnection(url);
    }

    @Override
    public boolean acceptsURL(String url) {
        return true;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }
}
