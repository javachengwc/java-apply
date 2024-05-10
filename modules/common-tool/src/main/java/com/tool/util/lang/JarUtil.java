package com.tool.util.lang;

import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 操作JAR文件的类
 */
public class JarUtil {

    public static void readJARList(String fileName) throws IOException {
        JarFile jarFile = new JarFile(fileName);
        Enumeration en = jarFile.entries();
        while (en.hasMoreElements()) { // 遍历显示JAR文件中的内容信息
            process(en.nextElement());
        }
    }

    private static void process(Object obj) {
        JarEntry entry = (JarEntry) obj;
        String name = entry.getName();
        long size = entry.getSize();
        long compressedSize = entry.getCompressedSize();// 压缩后的大小
        System.out.println(name + "\t" + size + "\t" + compressedSize);
    }

    public static void readJARFile(String jarFileName, String fileName) throws IOException {
        JarFile jarFile = new JarFile(jarFileName);
        Enumeration<JarEntry> e =jarFile.entries();
        //获得JAR文件中的单个文件的JAR实体
        JarEntry entry = jarFile.getJarEntry(fileName);
        InputStream input = jarFile.getInputStream(entry);// 根据实体创建输入流
        readFile(input);// 调用方法获得文件信息
        jarFile.close();// 关闭JAR文件对象流
    }

    public static void readFile(InputStream input) throws IOException {
        InputStreamReader in = new InputStreamReader(input);// 创建输入读流
        BufferedReader reader = new BufferedReader(in);// 创建缓冲读流
        String line;
        System.out.println("-----------------------");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    public static void unJar(File jarFile, File toDir) throws IOException {
        JarFile jar = new JarFile(jarFile);
        try {
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    InputStream in = jar.getInputStream(entry);
                    try {
                        File file = new File(toDir, entry.getName());
                        if (!file.getParentFile().mkdirs()) {
                            if (!file.getParentFile().isDirectory()) {
                                throw new IOException("make dir failed,dir="+ file.getParentFile().toString());
                            }
                        }
                        OutputStream out = new FileOutputStream(file);
                        try {
                            byte[] buffer = new byte[8192];
                            int i;
                            while ((i = in.read(buffer)) != -1) {
                                out.write(buffer, 0, i);
                            }
                        } finally {
                            out.close();
                        }
                    } finally {
                        in.close();
                    }
                }
            }
        } finally {
            jar.close();
        }
    }

    public static byte[] inputStream2byteArray(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        try {
            while((i = is.read()) != -1) {
                baos.write(i);
            }
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public static  byte[] readJarEntry(String jarFileName, String fileName) throws IOException {
        JarFile jarFile = new JarFile(jarFileName);
        JarEntry entry = jarFile.getJarEntry(fileName);
        InputStream input = jarFile.getInputStream(entry);// 根据实体创建输入流
        return inputStream2byteArray(input);
    }

    public static void extract(String jarFilePath, String toPath) throws IOException {
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> jarFileEntries = jarFile.entries();
            while (jarFileEntries.hasMoreElements()) {
                JarEntry jarEntry = jarFileEntries.nextElement();
                File file = new File(toPath + File.separator + jarEntry.getName());
                if (jarEntry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                InputStream inputStream=jarFile.getInputStream(jarEntry);
                Files.copy(inputStream, file.toPath());
            }
        }
    }

    public static void main(String args[]) throws IOException {
        System.out.println("----------输入jar文件全路径");
        Scanner scan = new Scanner(System.in);
        String jarFileName = scan.next();// 获得键盘输入的值
        readJARList(jarFileName);
        System.out.println("----------查看jar中的文件");
        String fileName = scan.next();
        readJARFile(jarFileName, fileName);
        extract(jarFileName,"D:/aa/");
    }
}