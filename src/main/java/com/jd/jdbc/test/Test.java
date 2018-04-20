package com.jd.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Test {
    public static void testJDBC() throws Exception{
        Class.forName("com.jd.jdbc.elasticsearch.ElasticSearchDriver");
        Connection conn = DriverManager.getConnection("jdbc:elasticsearch://192.168.70.128:9300");
        PreparedStatement stmt = conn.prepareStatement("select * from bank");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            System.out.println("firstname:"+rs.getString("firstname")+",balance:"+rs.getInt("balance"));
        }
        conn.close();
    }
    public static void main(String[] args) throws Exception{
        testJDBC();
    }
}
