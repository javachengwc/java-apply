package com.tool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *序列化工具
 */
public class SerializeUtil {

    private static final Logger logger= LoggerFactory.getLogger(SerializeUtil.class);

    //序列化(jdk自身)
    public static byte[] serialize(Object object) {
        byte[] result = null;
        if (object == null) {
            return new byte[0];
        }
        try  {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
            if (!(object instanceof Serializable)) {
                String objectClassName =object.getClass().getName();
                logger.error("SerializeUtil serialize error,object is not Serializable,objectClassName={}",objectClassName);
                throw new IllegalArgumentException("SerializeUtil serialize object is not Serializable,objectClassName=" + objectClassName);
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            result =  byteStream.toByteArray();
        } catch (Throwable e) {
            logger.error("SerializeUtil serialize error.",e);
            throw new IllegalArgumentException("SerializeUtil serialize error," ,e);
        }
        return result;
    }

    //反序列化(jdk自身)
    public static Object deserialize(byte[] bytes) {
        Object result = null;
        if (bytes== null || bytes.length<=0) {
            return null;
        }
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
            try {
                result = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                logger.error("SerializeUtil deserialize error,occur ClassNotFoundException");
                throw e;
            }
        } catch (Throwable e) {
            logger.error("SerializeUtil deserialize error,",e);
            throw new IllegalArgumentException("SerializeUtil deserialize error," ,e);
        }
        return result;
    }

    //将一个对象输出到tmp目录下的文件中,返回文件名
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
            logger.error("文件创建错误。", e);
        } catch (IOException e) {
            logger.error("文件写入错误，请检查权限。", e);
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    logger.error("对象流关闭错误。", e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("文件流关闭错误。", e);
                }
            }
        }
        return fileName;
    }

    //从文件中读取并转换为对象
    public static Object readFromFile(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object result = null;
        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            result = ois.readObject();
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        } catch (IOException e) {
            logger.error("文件读取错误，请检查权限。", e);
        } catch (ClassNotFoundException e) {
            logger.error("不能从序列号文件中读取对象，请检查文件是否损坏", e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    logger.error("对象流关闭错误。", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("文件流关闭错误。", e);
                }
            }
        }
        return result;
    }

}
