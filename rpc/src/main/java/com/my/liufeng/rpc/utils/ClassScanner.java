package com.my.liufeng.rpc.utils;

import com.my.liufeng.rpc.annotation.selector.ClassSelector;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 方法扫描器，扫描指定包下，使用了@MethodStub注解的类的，生成动态代理
 */
public class ClassScanner {
    /**
     * 获取类加载器
     *
     * @return classLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 扫描包名，获取符合条件的类，添加到集合
     */
    public static Set<Class<?>> scan(String[] packages) {
        return scan(packages, null);
    }

    /**
     * 扫描包名，获取符合条件的类，添加到集合
     */
    public static Set<Class<?>> scan(String packageName) {
        return scan(packageName, null);
    }

    public static Set<Class<?>> scan(String[] packages, ClassSelector classSelector) {
        Set<Class<?>> classSet = new HashSet<>();
        for (String packageName : packages) {
            doScan(packageName, classSet, classSelector);
        }
        return classSet;
    }

    /**
     * 扫描包名，获取符合条件的类，添加到集合
     */
    public static Set<Class<?>> scan(String packageName, ClassSelector classSelector) {
        Set<Class<?>> classSet = new HashSet<>();
        doScan(packageName, classSet, classSelector);
        return classSet;
    }

    private static void doScan(String packageName, Set<Class<?>> classSet, ClassSelector classSelector) {
        try {
            Enumeration<URL> resources = getClassLoader().getResources(packageName.replace(".", "/"));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    // 判断是普通文件，还是jar文件
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replace("%20", " ");
                        addClass(classSet, packagePath, packageName, classSelector);
                    } else if ("jar".equals(protocol)) {
                        // 读取jar文件
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                // 遍历
                                Enumeration<JarEntry> entries = jarFile.entries();
                                while (entries.hasMoreElements()) {
                                    JarEntry jarEntry = entries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    // 去除class文件后缀
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet, className, classSelector);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName, ClassSelector classSelector) {
        File[] files = new File(packagePath).listFiles(file -> {
            // 筛选class文件，或者目录
            return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
        });
        // 空的包
        if (CollectionUtil.isEmpty(files)) {
            return;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // class 文件处理
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                doAddClass(classSet, packageName + "." + className, classSelector);
            } else {
                // 目录 -- 递归扫描
                addClass(classSet, file.getPath(), packageName + "." + fileName, classSelector);
            }
        }
    }

    /**
     * 加载类，并添加到指定集合
     */
    private static void doAddClass(Set<Class<?>> classSet, String className, ClassSelector classSelector) {
        try {
            Class<?> clazz = Class.forName(className);
            if (classSelector != null) {
                if (classSelector.select(clazz)) {
                    classSet.add(clazz);
                }
            } else {
                // 添加到集合
                classSet.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found:" + className);
        }
    }


}
