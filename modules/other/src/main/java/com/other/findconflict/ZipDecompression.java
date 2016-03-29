package com.other.findconflict;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipDecompression {

    public static void decompression(String zipFile, String destination) throws IOException {
        ZipFile zip;
        try {
            zip = new ZipFile(zipFile);
        } catch (IOException e) {
            System.out.println("error name " + zipFile);
            throw e;
        }
        Enumeration en = zip.entries();
        ZipEntry entry = null;
        byte[] buffer = new byte[8192];
        int length = -1;
        InputStream input = null;
        BufferedOutputStream bos = null;
        File file = null;

        while (en.hasMoreElements()) {
            entry = (ZipEntry) en.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            input = zip.getInputStream(entry);
            file = new File(destination, entry.getName());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            bos = new BufferedOutputStream(new FileOutputStream(file));

            while (true) {
                length = input.read(buffer);
                if (length == -1)
                    break;
                bos.write(buffer, 0, length);
            }
            bos.close();
            input.close();
        }
        zip.close();
    }
}
