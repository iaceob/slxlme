package com.jfinal.ext.plugin.slxlme.tool;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Slxlme 文件操作, 读取存储 sql 的配置文件
 */
public class SlxlmeFile {


    public static File[] getSqlXmlFileList(String endsWith) {
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");
            List<File> lf = new ArrayList<File>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File[] fs = SlxlmeFile.getSqlXmlFileList(url.getPath(), endsWith);
                if (fs == null || fs.length == 0) continue;
                lf.addAll(Arrays.asList(fs));
            }
            return lf.toArray(new File[lf.size()]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static File[] getSqlXmlFileList(String path, final String endsWith) {
        return new File(path).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(endsWith);
            }
        });
    }

}
