package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;


public class InstanceLockUtil {

    private static Logger logger = LoggerFactory.getLogger(InstanceLockUtil.class);
    static FileLock lock = null;
    static String filePath = System.getProperty("user.home") + File.separator + ".lock";

    /**
     * 同进程中线程锁，同服务器中进程锁
     */
    public static Boolean isInstanceLock(String fileName) {

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            File flagFile = new File(file, fileName + ".lock");
            if (!flagFile.exists()) {
                file.createNewFile();
            }
            lock = new FileOutputStream(flagFile).getChannel().tryLock();
            if (lock == null) {
                return false;
            }
        } catch (IOException e) {
            logger.info("锁定失败！", e);
        }

        return true;
    }

    public static void main(String... args) {

        Boolean flag = InstanceLockUtil.isInstanceLock("abc");
        if (!flag) {
            logger.error("abc文件资源未释放,跳过执行");
            return;
        }

    }
}
