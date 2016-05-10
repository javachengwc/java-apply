package com.other.findconflict;

import java.io.File;
import java.io.IOException;

/**
 * 比较两个目录下有哪些相同的类
 */
public class JarDiffMain {

    /**
     * 输入要比较的两个目录，目录下包含jar，结果输出到控制台
     */
    public static void main(String[] args) throws IOException {

        args = new String[2];
        args[0]="E:\\dependency-a";
        args[1]="E:\\dependency-b";

        if (args.length != 2) {
            System.out.println("please input two compare dirs");
            System.exit(1);
        }
        String firstInputPath = args[0];
        String secondInputPath = args[1];

        File file = new File(firstInputPath);
        File[] listFiles = file.listFiles();
        for (File oneFile : listFiles) {
            if (oneFile.isFile() && oneFile.getAbsolutePath().endsWith(".jar")) {
                String jarName = oneFile.getName();
                ZipDecompression.decompression(oneFile.getAbsolutePath(), firstInputPath + "/tmp/"
                        + jarName); // 解压
                ClassNameFinder.find(jarName, firstInputPath + "/tmp/" + jarName, 1); // 查找
            }
        }

        file = new File(secondInputPath);
        listFiles = file.listFiles();
        for (File oneFile : listFiles) {
            if (oneFile.isFile() && oneFile.getAbsolutePath().endsWith(".jar")) {
                String jarName = oneFile.getName();
                ZipDecompression.decompression(oneFile.getAbsolutePath(), secondInputPath + "/tmp/"
                        + jarName);
                ClassNameFinder.find(jarName, secondInputPath + "/tmp/" + jarName, 2);
            }
        }

        ClassNameFinder.compare();
    }
}