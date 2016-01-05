package com.jfinal.ext.plugin.slxlme;

import com.jfinal.plugin.IPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SlxlmePlugin implements IPlugin {

    private static final Logger log = LoggerFactory.getLogger(SlxlmePlugin.class);

    /**
     * 存放 sql 的 xml 文件后缀名
     */
    private String suffix;
    /**
     * sql 容器标签
     */
    private String tagContainer;
    /**
     * 容器标识, 此标识作为取 sql 的命名空间
     */
    private String markContainer;
    /**
     * 容器中每个 sql 的节点, 作为每个 sql 的名称
     */
    private String markSql;

    /**
     * Sql 变量, 如果存在取出 Sql 统一替换的标识符
     */

    private Map<String, String> sqlVar;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTagContainer() {
        return tagContainer;
    }

    public void setTagContainer(String tagContainer) {
        this.tagContainer = tagContainer;
    }

    public String getMarkContainer() {
        return markContainer;
    }

    public void setMarkContainer(String markContainer) {
        this.markContainer = markContainer;
    }

    public String getMarkSql() {
        return markSql;
    }

    public void setMarkSql(String markSql) {
        this.markSql = markSql;
    }

    public Map<String, String> getSqlVar() {
        return sqlVar;
    }

    public void setSqlVar(Map<String, String> sqlVar) {
        this.sqlVar = sqlVar;
    }


    public SlxlmePlugin(String suffix, String tagContainer, String markContainer,
                        String markSql, Map<String, String> sqlVar) {
        this.suffix = suffix;
        this.tagContainer = tagContainer;
        this.markContainer = markContainer;
        this.markSql = markSql;
        this.sqlVar = sqlVar;
    }

    @Override
    public boolean start() {
        try {
            return SqlKit.init(this.getSuffix(), this.getTagContainer(), this.getMarkContainer(),
                    this.getMarkSql(), this.getSqlVar());
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
