package com.jfinal.ext.plugin.slxlme;

import com.jfinal.kit.JsonKit;
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
 * Created by cox on 2016/1/5.
 */
public class SlxlmeBuilder {

    private static final Logger log = LoggerFactory.getLogger(SlxlmeBuilder.class);

    private Document loadXML(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        // InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(xmlFile);
    }

    private Map<String, String> extraContainer(NodeList containers) {
        Map<String, String> sqlMap = new HashMap<String, String>();
        for (Integer i=containers.getLength(); i-->0;) {
            Element eleContainer = (Element) containers.item(i);
            NodeList nodeSqlsList = eleContainer.getElementsByTagName("sql");
            for (Integer j=nodeSqlsList.getLength(); j-->0;) {
                Element eleSql = (Element) nodeSqlsList.item(j);
                sqlMap.put(eleContainer.getAttribute("name") + "." + eleSql.getAttribute("id"),
                    eleSql.getTextContent());
                log.debug("{}.{}:{}", eleContainer.getAttribute("name"), eleSql.getAttribute("id"), eleSql.getTextContent());
            }
        }
        return sqlMap;
    }


    public Map<String, String> parse(File[] files, String tagContainer) throws Exception {
        if (files == null || files.length == 0)
            throw new RuntimeException("No xml file parse to document.");
        Map<String, String> sqlMap = new HashMap<String, String>();
        for (File f : files) {
            Document slxlme = this.loadXML(f);
            NodeList containers = slxlme.getElementsByTagName(tagContainer);
            sqlMap.putAll(this.extraContainer(containers));
        }
        log.debug("Parse result: {}", sqlMap);
        return sqlMap;
    }

}
