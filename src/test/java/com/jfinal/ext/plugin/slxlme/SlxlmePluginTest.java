package com.jfinal.ext.plugin.slxlme;

import com.jfinal.ext.plugin.slxlme.function.IncludeFunction;
import org.junit.Test;

/**
 * Created by cox on 2016/1/5.
 */
public class SlxlmePluginTest {


    @Test
    public void testPlugin() {
        String suffix = ".sql.xml",
                tagContainer = "container",
                tagSql = "sql",
                markContainer = "name",
                markSql = "id";
        SlxlmePlugin sp = new SlxlmePlugin(null, suffix, tagContainer, tagSql, markContainer, markSql);
        sp.regVar("tb_prefix", "tb_");
        sp.regFun("include", new IncludeFunction());
        sp.start();
    }

}