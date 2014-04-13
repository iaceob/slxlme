package com.jfinal.ext.plugin.slxlme;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * 合并多个xml文件(操蛋版)
 * @author iaceob (iaceob@gmail.com)
 *
 */
public class SqlXmlMerge {

    private static Logger log = Logger.getLogger("xmlmerge");
    
    /**
     * 获取xml文件信息 并合并 返回合并后的xml文档
     * @param expression 合并节点
     * @param files 文件集合
     * @return
     * @throws Exception
     */
    public static Document merge(String expression, File... files) {
        if ("".equals(expression)) {
            log.log(Level.WARNING, "合并节点不能为空");
            return null;
        }
        if (files.length == 0 || files == null) {
            log.log(Level.WARNING, "未找到文件");
            return null;
        }
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression compiledExpression = null;
        try {
            compiledExpression = xpath.compile(expression);
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        Document doc = null;
        try {
            doc = merge(compiledExpression, files);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 合并xml文件
     * @param expression 合并节点
     * @param files 文件集合
     * @return
     * @throws Exception
     */
    private static Document merge(XPathExpression expression, File... files) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document base = docBuilder.parse(files[0]);

        Node results = (Node) expression.evaluate(base, XPathConstants.NODE);
        if (results == null) {
            log.info( "合并节点错误");
        }
//        if (results == null) {
//            throw new IOException(files[0] + ": expression does not evaluate to node");
//        }

        for (int i = 1; i < files.length; i++) {
            Document merge = docBuilder.parse(files[i]);
            Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);
            while (nextResults.hasChildNodes()) {
                Node kid = nextResults.getFirstChild();
                nextResults.removeChild(kid);
                kid = base.importNode(kid, true);
                results.appendChild(kid);
            }
        }
        return base;
    }
    
    /**
     * 获取目录下的所有文件
     * @param folderPath 文件目录路径
     * @param endsWith 文件匹配尾
     * @return
     */
    public static File[] getFileList(String folderPath, String endsWith) {
        final String ew = endsWith;
       return  new File(folderPath).listFiles(new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(ew)) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 打印xml
     * @param doc xml文档
     * @throws Exception
     */
    private static void printXml(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        Result result = new StreamResult(System.out);
        transformer.transform(source, result);
    }
    
    public static void main(String[] args) throws Exception {
        // proper error/exception handling omitted for brevity
        File[] files = getFileList(SqlXmlMerge.class.getClassLoader().getResource("").getFile(), ".xml");
        // Document doc = merge("/sqlManage", files);
        Document doc = merge("/sqlManage", files);
        printXml(doc);
    }
}
