package com.jfinal.ext.plugin.slxlme.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sql 变量替换
 * 将从 xml 文件中取出的 sql 中的预定义变量替换成初始化 Slxlme 中提供的值
 */
public class SlxlmeReplace {

    private static final Logger log = LoggerFactory.getLogger(SlxlmeReplace.class);
    private static final String REGEX_VAR = "[${]+(?<var>.*?)}";
    private static final String REGEX_FUN_KEY_VAL = "#\\{(?<key>.*?)\\(('|\")(?<val>.*?)('|\")\\)}";
    // private static final String REGEX_FUN_ALL = "(?<all>#.*?})";

    public static String replaceVar(String sql, String var, String val) {
        try {
            Pattern p = Pattern.compile(REGEX_VAR);
            Matcher m = p.matcher(sql);
            while (m.find()) {
                String sv = m.group("var");
                if (!sv.equals(var)) continue;
                // var = var.replaceAll("\\.", ".");
                sql = sql.replaceAll("\\$\\{" + var + "}", val);
            }
            return sql;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return sql;
        }
    }

    /**
     * 获取 sql 中所有的函数列表
     * @param sql sql
     * @return List
     */
    public static List<Map<String, String>> getReplaceFun(String sql) {
        List<Map<String, String>> keys = new ArrayList<Map<String, String>>();
        Pattern p = Pattern.compile(REGEX_FUN_KEY_VAL);
        Matcher m = p.matcher(sql);
        while (m.find()) {
            Map<String, String> kv = new HashMap<String, String>();
            kv.put(m.group("key"), m.group("val"));
            keys.add(kv);
        }
        return keys;
    }

    /**
     * 根据数量生成 in 的占位符数量
     * @param number 数量
     * @return String
     */
    public static String genPlaceholder(Integer number) {
        StringBuilder sb = new StringBuilder();
        for (;(number--)>0;)
            sb.append("?").append((number>0 ? "," : ""));
        return sb.toString();
    }

}
