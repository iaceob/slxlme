package com.jfinal.ext.plugin.slxlme.function;

import com.jfinal.ext.plugin.slxlme.function.api.SlxlmeFunction;

import java.util.Map;

/**
 * sql 包含函数
 */
public class IncludeFunction implements SlxlmeFunction {

    private String replaceFun(String sql, String funKey, String funVal, String val) {
        return sql.replaceFirst("#\\{" + funKey + "\\(('|\")" + funVal + "('|\")\\)}", val);
    }

    /**
     * 执行函数
     *
     * @param funKey  当前函数注册的 key 名称
     * @param funVal  当前函数的参数值
     * @param sqlName 当前 sql 的名称
     * @param sql     当前 sql 内容
     * @param sqlMap  所有 sql
     * @return String
     */
    @Override
    public String run(String funKey, String funVal, String sqlName, String sql, Map<String, String> sqlMap) {
        return this.replaceFun(sql, funKey, funVal, sqlMap.get(funVal));
    }
}
