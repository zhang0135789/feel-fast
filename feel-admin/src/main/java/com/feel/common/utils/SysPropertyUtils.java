/**
 * Copyright (C) 2013 北京通宝科技有限公司
 *
 * @version:v1.0.0
 * @author:
 *
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2013-6-25                              v1.0.0        create
 *
 */
package com.feel.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SysPropertyUtils {

    private static final Logger Log = LoggerFactory
            .getLogger(SysPropertyUtils.class);

    private static SysPropertyUtils instance;
    private Map<String, String> properties;
    private DataSource db = null;

    private static final String LOAD_PROPERTIES = "SELECT name,prop_value FROM app_sys_property";
    private static final String UPDATE_PROPERTY = "UPDATE app_sys_property SET prop_value=? WHERE name=?";

    // private static final String INSERT_PROPERTY =
    // "INSERT INTO ofProperty(name, prop_value) VALUES(?,?)";
    // private static final String DELETE_PROPERTY =
    // "DELETE FROM ofProperty WHERE name LIKE ?";

    private SysPropertyUtils() {
        if (properties == null) {
            properties = new ConcurrentHashMap<String, String>();
            db = (DataSource) SpringUtils.getBean("dataSource");
        } else {
            properties.clear();
        }
        try {
            load();
        } catch (SQLException e) {
            Log.error(e.getMessage(), e);
        }
    }

    public static SysPropertyUtils getInstance() {
        if (instance == null) {
            instance = new SysPropertyUtils();
        }
        return instance;
    }

    private void load() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            pstmt = con.prepareStatement(LOAD_PROPERTIES);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String value = rs.getString(2);
                properties.put(name, value);
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (con != null)
                con.close();
        }
    }

    public void setProp(String name, String value) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = db.getConnection();
            pstmt = con.prepareStatement(UPDATE_PROPERTY);
            pstmt.setString(1, value);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            properties.put(name, value);
        } catch (SQLException e) {
            Log.error(e.getMessage(), e);
        } finally {
            if (pstmt != null)
                pstmt.close();
            if (con != null)
                con.close();
        }
    }

    public String getProp(String propName) {
        return properties.get(propName);
    }

    public String getProperty(String name, String defaultValue) {
        String value = properties.get(name);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }
}
