package com.jfinal.ext.plugin.slxlme.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 从 xml 中创建 sql map
 */
public class SlxlmeBuilder {

    private static final Logger log = LoggerFactory.getLogger(SlxlmeBuilder.class);

    /**
     * 载入 xml 文件, 将 xml 转为 Document 对象
     * @param xmlFile xml 文件
     * @return Document
     * @throws Exception
     */
    private Document loadXML(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

    /**
     * 提取 container 标签下的所有内容
     * @param containers containers 标签
     * @param tagSql sql 标签
     * @param markContainer container 命名空间
     * @param markSql sql 调用名
     * @return Map
     */
    private Map<String, String> extraContainer(NodeList containers, String tagSql, String markContainer, String markSql) {
        Map<String, String> sqlMap = new HashMap<String, String>();
        // 遍历所有 container 标签
        for (Integer i=containers.getLength(); i-->0;) {
            Element eleContainer = (Element) containers.item(i);
            // 取出 container 下的所有 sql 标签
            NodeList nodeSqlsList = eleContainer.getElementsByTagName(tagSql);
            for (Integer j=nodeSqlsList.getLength(); j-->0;) {
                Element eleSql = (Element) nodeSqlsList.item(j);
                // 将 container[name].sql#id 作为此条 sql 的凭据 放入到 map 中
                sqlMap.put(eleContainer.getAttribute(markContainer) + "." + eleSql.getAttribute(markSql),
                    eleSql.getTextContent());
                log.debug("{}.{}:{}", eleContainer.getAttribute(markContainer), eleSql.getAttribute(markSql), eleSql.getTextContent());
            }
        }
        return sqlMap;
    }


    /**
     * 解析 xml 文件, 提取 sql 文本
     * @param files xml 文件
     * @param tagContainer container 标签
     * @param tagSql sql 标签
     * @param markContainer container 命名空间
     * @param markSql sql 调用名
     * @return Map
     * @throws Exception
     */
    public Map<String, String> parse(File[] files, String tagContainer, String tagSql,
                                     String markContainer, String markSql) throws Exception {
        if (files == null || files.length == 0)
            throw new RuntimeException("No xml file parse to document.");
        Map<String, String> sqlMap = new HashMap<String, String>();
        for (File f : files) {
            Document slxlme = this.loadXML(f);
            NodeList containers = slxlme.getElementsByTagName(tagContainer);
            sqlMap.putAll(this.extraContainer(containers, tagSql, markContainer, markSql));
        }
        return sqlMap;
    }

}
