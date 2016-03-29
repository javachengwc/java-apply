package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *序列化工具
 */
public class SerializeUtil {


    private static final Logger LOGGER= LoggerFactory.getLogger(SerializeUtil.class);

    private SerializeUtil(){

    }
    /**
     * 将一个对象输出到tmp目录下的文件中,返回文件名.
     * @param obj
     * @return
     */
    public static String saveToFile(Object obj) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        String fileName = "/tmp/serialize_" + System.currentTimeMillis() + ".obj";
        System.out.println(fileName);
        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (FileNotFoundException e) {
            LOGGER.error("文件创建错误。", e);
        } catch (IOException e) {
            LOGGER.error("文件写入错误，请检查权限。", e);
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    LOGGER.error("对象流关闭错误。", e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.error("文件流关闭错误。", e);
                }
            }
        }

        return fileName;
    }


    /**
     * 从文件中读取并转换为对象
     * @param fileName
     * @return
     */
    public static Object readFromFile(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object result = null;
        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            result = ois.readObject();
        } catch (FileNotFoundException e) {
            LOGGER.error("文件不存在", e);
        } catch (IOException e) {
            LOGGER.error("文件读取错误，请检查权限。", e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("不能从序列号文件中读取对象，请检查文件是否损坏", e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    LOGGER.error("对象流关闭错误。", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("文件流关闭错误。", e);
                }
            }
        }

        return result;
    }

}
