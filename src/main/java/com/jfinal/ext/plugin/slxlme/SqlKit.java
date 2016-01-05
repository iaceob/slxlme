package com.jfinal.ext.plugin.slxlme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * sql 工具类
 * @author iaceob (iaceob@gmail.com)
 *
 */
public class SqlKit {

    private static final Logger log = LoggerFactory.getLogger(SqlKit.class);

    private static Map<String, String> sqlMap;
    private static Map<String, String> sqlVar;

    public static Boolean init(String suffix, String tagContainer, String markContainer,
                               String markSql, Map<String, String> sqlVar) throws Exception {
        File[] fs = SlxlmeFile.getSqlXmlFileList(suffix);
        SlxlmeBuilder sb = new SlxlmeBuilder();
        SqlKit.sqlMap = sb.parse(fs, tagContainer);
        SqlKit.sqlVar = sqlVar;

        log.debug("SqlKit init");
        return true;
    }

    public static Boolean clear() {
        SqlKit.sqlMap.clear();
        SqlKit.sqlVar.clear();
        return true;
    }
}
