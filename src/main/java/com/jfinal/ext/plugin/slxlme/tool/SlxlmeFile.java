package com.jfinal.ext.plugin.slxlme.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by cox on 2016/1/5.
 */
public class SlxlmeFile {

    private static final Logger log = LoggerFactory.getLogger(SlxlmeFile.class);

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
