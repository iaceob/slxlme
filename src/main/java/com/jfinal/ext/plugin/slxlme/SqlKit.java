package com.jfinal.ext.plugin.slxlme;

import com.jfinal.ext.plugin.slxlme.tool.SlxlmeReplace;

import java.text.MessageFormat;
import java.util.Map;

/**
 * sql 提取工具类
 *
 * @author iaceob (iaceob@gmail.com)
 */
public class SqlKit {

    private static final String MARK_PAGE = "#";
    private static final String MARK_IN = "##";

    private static Map<String, String> sqlMap;


    public static String getSql(String key) {
        return sqlMap.get(key);
    }

    public static String[] getSqls(String key, String markPage) {
        String sql = SqlKit.getSql(key);
        if (sql == null || "".equals(sql)) return null;
        return sql.split(markPage);
    }

    public static String[] getSqls(String key) {
        return SqlKit.getSqls(key, MARK_PAGE);
    }

    public static String getSqlIn(String key, Integer formatNumber, String markIn) {
        String sql = SqlKit.getSql(key);
        if (sql == null || "".equals(sql)) return null;
        return sql.replaceAll(markIn, " in(" + SlxlmeReplace.genPlaceholder(formatNumber) + ") ");
    }

    public static String getSqlIn(String key, Integer formatNumber) {
        return SqlKit.getSqlIn(key, formatNumber, MARK_IN);
    }

    public static String[] getSqlsIn(String key, Integer formatNumber, String markPage, String markIn) {
        String sql = SqlKit.getSqlIn(key, formatNumber, markIn);
        if (sql == null || "".equals(sql)) return null;
        return sql.split(markPage);
    }

    public static String[] getSqlsIn(String key, Integer formatNumber) {
        return SqlKit.getSqlsIn(key, formatNumber, MARK_PAGE, MARK_IN);
    }

    public static String formatSql(String key, Object... args) {
        String sql = SqlKit.getSql(key);
        if (sql == null || "".equals(sql)) return null;
        return MessageFormat.format(sql, args);
    }

    public static String formatSqlIn(String key, Integer formatNumber, String markIn, Object... args) {
        String sql = SqlKit.getSqlIn(key, formatNumber, markIn);
        if (sql == null || "".equals(sql)) return null;
        return MessageFormat.format(sql, args);
    }

    public static String formatSqlIn(String key, Integer formatNumber, Object... args) {
        return SqlKit.formatSqlIn(key, formatNumber, MARK_IN, args);
    }

    public static String[] formatSqls(String key, String markPage, Object... args) {
        String sql = SqlKit.formatSql(key, args);
        if (sql == null || "".equals(sql)) return null;
        return sql.split(markPage);
    }

    public static String[] formatSqls(String key, Object... args) {
        return SqlKit.formatSqls(key, MARK_PAGE, args);
    }

    public static String[] formatSqlsIn(String key, Integer formatNumber, String markPage, String markIn, Object... args) {
        String sql = SqlKit.getSqlIn(key, formatNumber, markIn);
        if (sql == null || "".equals(sql)) return null;
        sql = MessageFormat.format(sql, args);
        return sql.split(markPage);
    }

    public static String[] formatSqlsIn(String key, Integer formatNumber, Object... args) {
        return SqlKit.formatSqlsIn(key, formatNumber, MARK_PAGE, MARK_IN, args);
    }


    public static Boolean init(Map<String, String> sqlMap) {
        SqlKit.sqlMap = sqlMap;
        return true;
    }

    public static Boolean clear() {
        SqlKit.sqlMap.clear();
        return true;
    }

    public static Map<String, String> sqlMap() {
        return sqlMap;
    }
}
