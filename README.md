##Slxlme
重造轮子之 sql in xml
jfinal 存放sql于xml文件中进行sql管理

作用是将sql写到xml文件中便于sql的管理
其中xml文件可以分为多个文件 避免一个文件存储时 因为太多而不便于维护
多个xml结构需相同 
xml 的默认节点配置如下
<pre>&lt;xlslme&gt;
  &lt;container name="a"&gt;
      &lt;sql id="bb"&gt;&lt;/sql&gt;
  &lt;/container&gt;
  ....
&lt;xlsme&gt;</pre>
xml的存放路径默认在 src 目录下
xml文件后缀默认采用 .sql.xml 
节点名称可以更改 属性也可以更改  但是更改后 在config中配置扩展的时候需要和更改的节点名 属性名相同
存放sql的sql标签更改后无需在这里配置
获取sql使用  SqlKit.getSql("a.bb");

在 jfinal 加入配置
me.add(new SlxlmePlugin(arg0, arg1, ....));
相关配置在 SlxlmePlugin 中有说明

