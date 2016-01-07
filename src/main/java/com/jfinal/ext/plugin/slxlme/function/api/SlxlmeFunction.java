package com.jfinal.ext.plugin.slxlme.function.api;

import java.util.Map;

/**
 * Slxlme 函数接口, 自定义的函数通过此接口实现
 */
public interface SlxlmeFunction {

    /**
     * 执行函数
     * @param funKey 当前函数注册的 key 名称
     * @param funVal 当前函数的参数值
     * @param sqlName 当前 sql 的名称
     * @param sql 当前 sql 内容
     * @param sqlMap 所有 sql
     * @return String
     */
    String run(String funKey, String funVal, String sqlName, String sql, Map<String, String> sqlMap);

}
