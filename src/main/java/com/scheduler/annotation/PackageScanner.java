package com.scheduler.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 谢俊权
 * @create 2016/7/17 22:24
 */
public class PackageScanner {

    private static final Logger logger = LoggerFactory.getLogger(PackageScanner.class);

    private static final String CLASS_SUFFIX = String.valueOf(".class");

    public static Set<Class<?>> getClasses(){
        return getClasses("");
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(String pack) {

        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader()
                    .getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName   包名
     * @param packagePath   包路径
     * @param classes   返回的所有扫描类
     */
    private static void findAndAddClassesInPackageByFile(
            String packageName,
            String packagePath,
            Set<Class<?>> classes) {

        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isDirectory()) || (file.getName().endsWith(CLASS_SUFFIX));
            }
        });
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                String EMPTY = String.valueOf("");
                String path = EMPTY;
                if (EMPTY.equals(packageName))
                    path = file.getName();
                else
                    path = packageName + "." + file.getName();
                findAndAddClassesInPackageByFile(path, file.getAbsolutePath(), classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - CLASS_SUFFIX.length());
                String classPath = ("".equals(packageName)) ? className : packageName + '.' + className;
                try {
                    Class clazz = Thread.currentThread().getContextClassLoader().loadClass(classPath);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    logger.error("error load class:{}", classPath, e);
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
