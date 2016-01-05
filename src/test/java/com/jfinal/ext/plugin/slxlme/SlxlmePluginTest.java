package com.jfinal.ext.plugin.slxlme;

import org.junit.Test;

import java.util.Map;

/**
 * Created by cox on 2016/1/5.
 */
public class SlxlmePluginTest {


    @Test
    public void testPlugin() {
        String suffix = ".sql.xml",
                tagContainer = "container",
                markContainer = "name",
                markSql = "id";
        Map<String, String> sqlVar = null;
        SlxlmePlugin sp = new SlxlmePlugin(suffix, tagContainer, markContainer, markSql, sqlVar);
        sp.start();
    }

}