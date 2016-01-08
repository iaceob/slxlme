#Slxlme Plugin
将 SQL 语句通过 XML 文件进行管理.

##启用:

### jfinal
```java
plugins.add(new SlxlmePlugin());
```
### 其他
```java
new SlxlmePlugin().start();
```

## 使用

### 配置说明
这些配置均在实例化 Slxlme 插件时的构造函数中设置.
所有参数均有默认值, 因此运行可以不做任何配置, 一下是默认配置
var          |comment                              | default
---          |---                                  | ---
path         |xml 文件位置                          | classpath
suffix       |存放 sql 的 xml 文件后缀名              | .sql.xml
tagContainer |sql 容器标签                          | container
tagSql       |存储 sql 语句标签                      | sql
markContainer|容器标识, 此标识作为取 sql 的命名空间      | name
markSql      |容器中每个 sql 的节点, 作为每个 sql 的名称 | id

### 格式
如下是默认示例:
```xml
<?xml version="1.0" encoding="utf-8"?>
<slxlme>
    <container name="Tpl">
        <sql id="testSql1">
            select * from table1;
        </sql>
    </container>
</slxlme>
```
### Slxlme 变量
变量的使用方式;
```java
SlxlmePlugin plugin = new SlxlmePlugin();
plugin.regVar("tb_prefix", "tb_");
```
在 xml 中使用 ${tb_prefix} 获取
```xml
<sql id="testSql1">
select * from ${tb_prefix}table1
</sql>
```
使用 SqlKit 获取的内容
```sql
select * from tb_table1
```
### Slxlme 函数
####函数使用
Slxlme 自带 include 函数, 使用方式如下
```java
SlxlmePlugin plugin = new SlxlmePlugin();
plugin.regFun("include", new IncludeFunction());
```
在 xml 中使用 #{include('namespace.sqlName')}
```xml
<?xml version="1.0" encoding="utf-8"?>
<slxlme>
    <container name="Tpl">
        <sql id="table1Fields">
            id, name, num
        </sql>
        <sql id="testSql1">
            select #{include('Tpl.table1Fields')} from table1;
        </sql>
    </container>
</slxlme>
```
最终获取的内容是:
```sql
select id, name, num from table1;
```
>若使用的数据库是 MySQL , 使用视图的情况下, 因为 MySQL 不支持视图中存在子查询, 可以通过 include 函数来弥补
#### 自定义函数
自定义函数, 实现 `com.jfinal.ext.plugin.slxlme.function.api.SlxlmeFunction` 接口, 如自带的 `IncludeFunction`
接口信息如下:
```java
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

```
自定义函数只需在 plugin 中注册即可.
>注意: include 随为自带函数, 不过并不是默认加载, 如需使用, 仍须先注册

### SqlKit
Slxlme 使用 SqlKit 来获取存放于 xml 中的 sql 语句.
Sql 的获取格式是 `container[name].sql#id` 中的内容, 如 上述文档 xml 中默认的档案, sql 的 key 就是  `Tpl.testSql1`
共提供了 8 中方式
```text
SqlKit.getSql
SqlKit.getSqls
SqlKit.getSqlIn
SqlKit.getSqlsIn
SqlKit.formatSql
SqlKit.formatSqls
SqlKit.formatSqlIn
SqlKit.formatSqlsIn
```
**`SqlKit.getSql` **
>直接获取某个 key 的 sql

**`SqlKit.getSqls` **
>这个方法, 目前在 Jfinal 中用于分页, 因为 Jfinal 的分页查询 `select` 和 `from` 为分开的两条语句, 使用 `getSqls` 返回的是 `String[]` 0 是 `select` 1 是 `from`
>其原理是使用 # 对 sql 字符串分割, 因此, xml 中的 sql 需要有 # , 如:
>```sql
>select * # from table1;
>```

**`SqlKit.getSqlIn`**
>多数数据库操作, 并非使用的拼接字符串的方式, 而是使用占位符的方式来替换, 如 jfinal 中使用 `?`
>这样如果在一条语句需要使用 `in` 来查询, 那么占位符的数量就不确定, 不能直接在 sql 中写死, 因此就有了这个方法, 使用这个方法的时候, 传入需要替换的数量, 然后自动生成占位符的数量.

**`SqlKit.getSqlsIn`**
>分页与 `in` 占位符的结合使用

**`SqlKit.formatSql`**
>java 中的占位符替换, 若有需求可以使用这种方式, 使用方式如:
>```sql
>select * from {0} where id=?
>```
>或者更多个, {0}  {1} {2}
>方式类似于 `properites` 文件中的占位符

**`Other`**
剩下的方法是占位符于 分页 和 `in` 占位符混用

##Licensed
MIT

---
更多示例见项目的测试中.
