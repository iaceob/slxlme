package com.jfinal.ext.plugin.slxlme;

import com.jfinal.ext.plugin.slxlme.function.api.SlxlmeFunction;
import com.jfinal.ext.plugin.slxlme.tool.SlxlmeBuilder;
import com.jfinal.ext.plugin.slxlme.tool.SlxlmeFile;
import com.jfinal.ext.plugin.slxlme.tool.SlxlmeReplace;
import com.jfinal.plugin.IPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlxlmePlugin implements IPlugin {

    private static final Logger log = LoggerFactory.getLogger(SlxlmePlugin.class);

    /**
     * xml 文件位置
     */
    private String path;

    /**
     * 存放 sql 的 xml 文件后缀名
     */
    private String suffix;
    /**
     * sql 容器标签
     */
    private String tagContainer;
    /**
     * 存储 sql 语句标签
     */
    private String tagSql;
    /**
     * 容器标识, 此标识作为取 sql 的命名空间
     */
    private String markContainer;
    /**
     * 容器中每个 sql 的节点, 作为每个 sql 的名称
     */
    private String markSql;

    /**
     * slxlme 变量, 如果存在取出 Sql 统一替换的标识符
     */
    private Map<String, String> sqlVar;

    /**
     * slxlme 函数
     */
    private Map<String, SlxlmeFunction> funs;

    public SlxlmePlugin(String path, String suffix, String tagContainer, String tagSql,
                        String markContainer, String markSql) {
        this.path = path;
        this.suffix = suffix;
        this.tagContainer = tagContainer;
        this.tagSql = tagSql;
        this.markContainer = markContainer;
        this.markSql = markSql;
        this.sqlVar = new HashMap<String, String>();
        this.funs = new HashMap<String, SlxlmeFunction>();
    }

    public SlxlmePlugin(String path, String suffix, String tagContainer, String tagSql) {
        this(path, suffix, tagContainer, tagSql, "name", "id");
    }

    public SlxlmePlugin(String path, String suffix) {
        this(path, suffix, "container", "sql");
    }

    public SlxlmePlugin(String suffix) {
        this(null, suffix);
    }

    public SlxlmePlugin() {
        this(".sql.xml");
    }

    /**
     * 注册变量
     * @param key 变量名
     * @param val 变量值
     * @return SlxlmePlugin
     */
    public SlxlmePlugin regVar(String key, String val) {
        if (key == null || "".equals(key))
            throw new IllegalArgumentException("The varKey can not be null");
        this.sqlVar.put(key, val);
        return this;
    }

    /**
     * 注册函数
     * @param key 函数在 sql 中注册的名称
     * @param fun 函数处理类
     * @return SlxlmePlugin
     */
    public SlxlmePlugin regFun(String key, SlxlmeFunction fun) {
        if (key == null || "".equals(key))
            throw new IllegalArgumentException("The funKey can not be null");
        this.funs.put(key, fun);
        return this;
    }


    /**
     * 调用函数解析
     * @param sql 当前 sql
     * @param sqlMapKey 当前 sql 名
     * @param sqlMap 所有 sql
     * @return String
     */
    private String callFun(String sql, String sqlMapKey, Map<String, String> sqlMap) {
        // 获取当前 sql 语句中所有的函数列表
        List<Map<String, String>> sqlFunKvs = SlxlmeReplace.getReplaceFun(sql);
        if (sqlFunKvs == null || sqlFunKvs.isEmpty()) return sql;

        // 遍历所有函数
        for (String funKey : this.funs.keySet()) {
            // 遍历 sql 中的函数列表
            for (Map<String, String> sqlFunKv : sqlFunKvs) {
                // 获取 sql 中单个函数
                for (String sqlFunKey : sqlFunKv.keySet()) {
                    if (!funKey.equals(sqlFunKey)) continue;
                    // 执行函数
                    sql = this.funs.get(funKey).run(sqlFunKey, sqlFunKv.get(sqlFunKey), sqlMapKey, sql, sqlMap);
                }
            }
        }

        // 检测执行函数后的 sql 是否还存在函数, 如果存在则递归调用函数, 直到最终的 sql 不存在函数
        /*
        因为部分函数正在执行一次后可能从别处引入了新的语句,
        但是该条语句尚未初始化, 导致函数未调用, 因此再次调用函数解析
         */
        sqlFunKvs = SlxlmeReplace.getReplaceFun(sql);
        // 如果不存在函数则无需再次解析
        if (sqlFunKvs == null || sqlFunKvs.isEmpty()) return sql;
        return this.callFun(sql, sqlMapKey, sqlMap);
    }

    @Override
    public boolean start() {
        try {

            File[] fs = this.path == null ? SlxlmeFile.getSqlXmlFileList(this.suffix) : SlxlmeFile.getSqlXmlFileList(this.path, this.suffix);

            if (fs == null || fs.length == 0) return true;
            SlxlmeBuilder sb = new SlxlmeBuilder();
            Map<String, String> sqlMap = sb.parse(fs, this.tagContainer, this.tagSql, this.markContainer, this.markSql);

            // 替换变量
            for (String sqlMapKey : sqlMap.keySet()) {
                String sql = sqlMap.get(sqlMapKey);
                if (sql == null || "".equals(sql)) continue;
                for (String sqlVarKey : this.sqlVar.keySet())
                    sql = SlxlmeReplace.replaceVar(sql, sqlVarKey, this.sqlVar.get(sqlVarKey));
                sqlMap.put(sqlMapKey, sql);
            }

            // 调用函数解析
            for (String sqlMapKey : sqlMap.keySet()) {
                String sql = sqlMap.get(sqlMapKey);
                if (sql == null || "".equals(sql)) continue;
                sqlMap.put(sqlMapKey, this.callFun(sql, sqlMapKey, sqlMap));
            }

            log.debug("Result: {}", sqlMap);
            return SqlKit.init(sqlMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean stop() {
        return SqlKit.clear();
    }
}
