package com.other.findconflict;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 搜索和比较相同的类
 */
public class ClassNameFinder {

    private static Map<String, List<ClassInfosDO>> firstMap  = new HashMap<String, List<ClassInfosDO>>();
    private static Map<String, List<ClassInfosDO>> secondMap = new HashMap<String, List<ClassInfosDO>>();

    public static void find(String jarName, String path, int order) {

        List<String> allClassName = new ArrayList<String>();
        //根据路径取得全类
        getAllClassName(path, allClassName, path.length() + 1);

        if (allClassName != null) {
            for (String one : allClassName) {
                ClassInfosDO classInfosDO = new ClassInfosDO(one, jarName);
                List<ClassInfosDO> list = getOrderMap(order).get(one);
                if (list == null) {
                    List<ClassInfosDO> arrayList = new ArrayList<ClassInfosDO>();
                    arrayList.add(classInfosDO);
                    getOrderMap(order).put(one, arrayList);
                } else {
                    list.add(classInfosDO);
                }
            }
        }

    }

    public static void compare() {
        Set<String> keySet = firstMap.keySet();
        List<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);
        Collections.sort(keyList);

        Map<String, List<String>> jarViewResult = new TreeMap<String, List<String>>();
        List<String> classViewResult = new ArrayList<String>();

        for (String fullClassName : keyList) {
            List<ClassInfosDO> list = secondMap.get(fullClassName);
            if (list != null) {
                //目录1
                StringBuffer input1SB = new StringBuffer();
                List<ClassInfosDO> value = firstMap.get(fullClassName);
                for (ClassInfosDO oneClassInfosDO : value) {
                    input1SB.append(" " + oneClassInfosDO.getJarName() + ",");
                }

                //目录2
                StringBuffer input2SB = new StringBuffer();
                for (ClassInfosDO oneClassInfosDO : list) {
                    input2SB.append(" " + oneClassInfosDO.getJarName() + ",");
                }

                classViewResult.add(fullClassName + "\tInput1: " + input1SB.toString()
                        + "\t input2: " + input2SB.toString());

                String jarKey = input1SB.toString() + " vs " + input2SB.toString();
                if (jarViewResult.get(jarKey) != null) {
                    jarViewResult.get(jarKey).add(fullClassName);
                } else {
                    List<String> classes = new ArrayList<String>();
                    classes.add(fullClassName);
                    jarViewResult.put(jarKey, classes);
                }
            }
        }

        writeResult(jarViewResult, classViewResult);
    }

    /**
     * 从jar的纬度和class的纬度分别输出结果
     */
    private static void writeResult(Map<String, List<String>> jarViewResult,
                                    List<String> classViewResult) {
        System.out.println("View From jar start.....");
        System.out.println("Total conflict jar num is " + jarViewResult.size());
        Set<Entry<String, List<String>>> entrySet = jarViewResult.entrySet();
        for (Entry<String, List<String>> one : entrySet) {
            String key = one.getKey();
            System.out.println("conflict jar name is" + key);
            for (String oneClassName : one.getValue()) {
                System.out.println("\t\t" + oneClassName);
            }
            System.out.println();
        }

        System.out.println("View From jar end!!!!");

        System.out.println("View From class start.....");
        System.out.println("Total conflict class num is " + classViewResult.size());
        for (String abc : classViewResult) {
            System.out.println(abc);
        }
        System.out.println("View From class end!!!!");
    }

    public static Map<String, List<ClassInfosDO>> getOrderMap(int order) {
        if (order == 1) {
            return firstMap;
        } else {
            return secondMap;
        }
    }

    public static void getAllClassName(String dir, List<String> allClassName, int length) {
        File file = new File(dir);
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return;
        }

        for (File oneFile : listFiles) {
            String absolutePath = oneFile.getAbsolutePath();
            if (oneFile.isDirectory()) {
                getAllClassName(absolutePath, allClassName, length);
            } else {
                if (absolutePath.endsWith(".class")) {
                    allClassName.add(absolutePath.substring(length));
                }
            }
        }

    }
}