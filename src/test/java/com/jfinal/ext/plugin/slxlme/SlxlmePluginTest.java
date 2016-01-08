package com.jfinal.ext.plugin.slxlme;

import com.jfinal.ext.plugin.slxlme.function.IncludeFunction;
import org.junit.Test;

/**
 * Created by cox on 2016/1/5.
 */
public class SlxlmePluginTest {


    @Test
    public void testPlugin() {
        SlxlmePlugin sp = new SlxlmePlugin();
        sp.regVar("tb_prefix", "tb_");
        sp.regVar("table.id", "id");
        sp.regFun("include", new IncludeFunction());
        sp.start();

        System.out.println(SqlKit.getSql("Test1.getTable4"));
    }

}