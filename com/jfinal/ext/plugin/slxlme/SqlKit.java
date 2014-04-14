package com.jfinal.ext.plugin.slxlme;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jfinal.log.Logger;

/**
 * sql 工具类
 * @author iaceob (iaceob@gmail.com)
 *
 */
public class SqlKit {

    protected static final Logger log = Logger.getLogger(SqlKit.class);
    
    private static Map<String, String> sqlMap;


    
    /**
     * 遍历xml节点将sql写入到map中
     * @param allNode 所有sql节点
     */
     private static void setSqlMap(NodeList allNode, String containerMark, String sqlMark) {
         Element element;
         // 对符合条件的所有节点进行遍历
         for (int i=allNode.getLength(); (i--)>0;) {
             // 获得一个元素
             element = (Element) allNode.item(i);
             // 此元素有子节点，获取所有子节点
             NodeList nl = element.getChildNodes();
             // 遍历所有子节点
             for (int j=nl.getLength(); (j--)>0;) {
                 // 若子节点的名称不为#text，则输出，#text为反／标签
                 if (!nl.item(j).getNodeName().equals("#text")) {
                     sqlMap.put(
                                 element.getAttribute(containerMark) + "." + 
                                 nl.item(j).getAttributes().getNamedItem(sqlMark).getTextContent(), 
                                 nl.item(j).getTextContent()
                                 );
                 }
             }
         }
     }
     
    
     /**
      * 初始化sql
      * @param folderPath sql xml 文件目录地址
      * @param fileSuffix 文件后缀
      * @param expression xml 合并节点名
      * @param container sql 容器
      * @param containerMark sql 容器标识
      * @param sqlMark sql 标识
      * @return
      */
    public static boolean init(String folderPath, String fileSuffix, String expression, String container, String containerMark, String sqlMark) {
        sqlMap = new HashMap<String, String>();
        File[] files = SqlXmlMerge.getFileList(folderPath, fileSuffix); 
        if (files.length == 0) {
            log.debug("在目录 " + folderPath + " 中未找到文件名末尾为" + fileSuffix + "的文件");
            return false;
        }
        Document doc = SqlXmlMerge.merge("/" + expression, files);
        NodeList nl = doc.getElementsByTagName(container);
        setSqlMap(nl, containerMark, sqlMark);
        if (sqlMap.isEmpty() || sqlMap == null) {
            return false;
        }
        return true;
    }
    
    
    /**
     * 获取sql 
     * @param key sql 容器节点name . sql节点id
     * @return
     */
    public static String getSql(String key) {
        return getSql(key, "");
    }

    /**
     * 获取 sql 格式化sql中预定义字符以 {0} {1} {2} 格式
     * @param key sql 容器节点name . sql节点id
     * @param arguments 格式化字符
     * @return
     */
    public static String getSql(String key, Object ... arguments) {
        return MessageFormat.format(sqlMap.get(key), arguments);
    }
    

    /**
     * 
     */
    public static void clear() {
        sqlMap.clear();
    }


    
    
}
