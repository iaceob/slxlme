package com.jfinal.ext.plugin.slxlme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * sql 工具类
 *
 * @author iaceob (iaceob@gmail.com)
 */
public class SqlKit {

    private static final Logger log = LoggerFactory.getLogger(SqlKit.class);

    private static Map<String, String> sqlMap;

    public static Boolean init(Map<String, String> sqlMap) {
        SqlKit.sqlMap = sqlMap;
        return true;
    }

    public static Boolean clear() {
        SqlKit.sqlMap.clear();
        return true;
    }
}
