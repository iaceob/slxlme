package com.jfinal.ext.plugin.slxlme;

import com.jfinal.plugin.IPlugin;

/**
 * slxlme plugin 
 * jfianl 扩展
 * 作用是将sql写到xml文件中便于sql的管理
 * 其中xml文件可以分为多个文件 避免一个文件存储时 因为太多而不便于维护
 * 多个xml结构需相同 
 * xml 的默认节点配置如下
 * <xlslme>
 *  <container name="a">
 *      <sql id="bb"></sql>
 *  </container>
 *  ....
 * <xlsme>
 * xml的存放路径默认在 src 目录下
 * xml文件后缀默认采用 .sql.xml 
 * 节点名称可以更改 属性也可以更改  但是更改后 在config中配置扩展的时候需要和更改的节点名 属性名相同
 * 存放sql的sql标签更改后无需在这里配置
 * 获取sql使用  SqlKit.getSql("a.bb");
 * @author iaceob (iaceob@gmail.com)
 * @version 1.0
 *
 */
public class SlxlmePlugin implements IPlugin {

    private String folderPath; // 目录路径
    private String fileSuffix; // 文件后缀
    private String expression; // 合并节点名
    private String container; // sql 容器节点名
    private String containerMark; // sql 容器标识
    private String sqlMark; // sql 标识
    
    /**
     * 初始化配置
     */
    public SlxlmePlugin() {
        this(SlxlmePlugin.class.getClassLoader().getResource("").getFile());
    }
    
    /**
     * 
     * 初始化配置
     * @param folderPath 目录地址
     */
    public SlxlmePlugin(String folderPath) {
        this(folderPath, ".sql.xml");
    }
    
    /**
     * 初始化配置
     * @param folderPath 目录地址
     * @param fileSuffix 文件后缀
     */
    public SlxlmePlugin(String folderPath, String fileSuffix) {
        this(folderPath, fileSuffix, "slxlme", "container");
    }

    /**
     * 初始化配置
     * @param folderPath 目录地址
     * @param fileSuffix 文件后缀
     * @param expression 合并节点名
     * @param container sql 容器节点名
     */
    public SlxlmePlugin(String folderPath, String fileSuffix, String expression, String container) {
        this(folderPath, fileSuffix, expression, container, "name", "id");
    }
    
    /**
     * 初始化配置
     * @param folderPath 目录地址
     * @param fileSuffix 文件后缀
     * @param expression 合并节点名
     * @param container sql 容器节点名
     * @param containerMark sql 容器标识 默认 name container标签的属性 获取container下所有的sql
     * @param sqlMark sql 标识 默认id  sql 标签的属性 获取标签下的数据
     */
    public SlxlmePlugin(String folderPath, String fileSuffix, String expression, String container, String containerMark, String sqlMark) {
        this.folderPath = folderPath;
        this.fileSuffix = fileSuffix;
        this.expression = expression;
        this.container = container;
        this.containerMark = containerMark;
        this.sqlMark = sqlMark;
    }
    
    
    @Override
    public boolean start() {
        // TODO Auto-generated method stub
        return SqlKit.init(this.folderPath, this.fileSuffix, this.expression, this.container, this.containerMark, this.sqlMark);
    }

    @Override
    public boolean stop() {
        // TODO Auto-generated method stub
        SqlKit.clear();
        return true;
    }

}
