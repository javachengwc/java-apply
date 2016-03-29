package com.manageplat.util;

import com.util.PropertiesLoader;
import com.util.StreamUtil;
import org.apache.commons.lang.StringUtils;
//import org.csource.common.MyException;
//import org.csource.fastdfs.ClientGlobal;
//import org.csource.fastdfs.StorageClient;
//import org.csource.fastdfs.TrackerGroup;
//import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * fastdfs文件管理类.
 */
public class FastDfsManager {
//
//    private final static Logger logger = LoggerFactory.getLogger(FastDfsManager.class);
//
//    private static FastDfsManager inst = new FastDfsManager();
//
//    private TrackerGroup trackerGroup = null;
//
//    private boolean load=false;
//
//    public static FastDfsManager getInstance()
//    {
//        return inst;
//    }
//
//    private FastDfsManager()
//    {
//        init();
//    }
//
//
//    public void init(){
//
//        logger.info("FastDfsServiceImpl init start");
//
//        if(trackerGroup==null){
//            try{
//
//                Properties prop = PropertiesLoader.load("fastdfs.properties");
//                if(prop!=null) {
//                    InetSocketAddress[] inetSocketAddressArray = new InetSocketAddress[2];
//                    InetSocketAddress ad1 = new InetSocketAddress( prop.getProperty("fastdfs.trackerServer1.ip"), Integer.parseInt(prop.getProperty("fastdfs.trackerServer1.port")));
//                    InetSocketAddress ad2 = new InetSocketAddress( prop.getProperty("fastdfs.trackerServer2.ip"), Integer.parseInt(prop.getProperty("fastdfs.trackerServer2.port")));
//                    inetSocketAddressArray[0] = ad1;
//                    inetSocketAddressArray[1] = ad2;
//                    trackerGroup = new TrackerGroup(inetSocketAddressArray);
//
//                    ClientGlobal.setG_tracker_group(trackerGroup);
//                    // 连接超时的时限，单位为毫秒
//                    ClientGlobal.setG_connect_timeout(Integer.parseInt(prop.getProperty("fastdfs.connect.timeout")));
//                    // 网络超时的时限，单位为毫秒
//                    ClientGlobal.setG_network_timeout(Integer.parseInt(prop.getProperty("fastdfs.network.timeout")));
//                    // 字符集
//                    ClientGlobal.setG_charset("UTF-8");
//                    ClientGlobal.setG_anti_steal_token(false);
//                    ClientGlobal.setG_secret_key(null);
//
//                    load = true;
//                }else
//                {
//                    logger.error("加载fastdfs.properties失败");
//                }
//            }catch (Exception e){
//                logger.error("初始化trackerGroup失败，",e);
//
//            }
//        }
//        logger.info("FastDfsServiceImpl load:"+load);
//        logger.info("FastDfsServiceImpl init end");
//    }
//
//    public String[] uploadFile(File file) {
//
//        if(load) {
//
//            return uploadFile(file, file.getName());
//        }
//        return null;
//    }
//
//    public String[] uploadFile(File file, String name) {
//
//        if(load) {
//            byte[] fileBuff = getFileBuffer(file);
//            String fileExtName = getFileExtName(name);
//            return send(fileBuff, fileExtName);
//        }
//        return null;
//    }
//
//    public byte[] downLoadFile(String path)
//    {
//        if(StringUtils.isBlank(path))
//        {
//            return new byte [0];
//        }
//        byte[] rt=null;
//        if(path.startsWith("group"))
//        {
//
//            int index = path.indexOf("/");
//            String group = path.substring(0,index);
//            String remotePath = path.substring(index+1);
//            rt= downLoadDfsFile(group,remotePath);
//
//        }else
//        {
//            rt= downLoadLocalFile(path);
//        }
//
//        if(rt==null)
//        {
//            rt = new byte[0];
//        }
//        return rt;
//    }
//
//
//    public byte[] downLoadLocalFile(String path)
//    {
//
//        File file = new File(path);
//        if(!file.exists())
//        {
//            return new byte[0];
//        }
//
//        FileInputStream stream = null;
//        try {
//            stream = new FileInputStream(file);
//            return StreamUtil.inputStream2Byte(stream);
//        }catch(Exception e)
//        {
//            logger.error("downLoadLocalFile error,",e);
//            return new  byte[0];
//        }finally
//        {
//            if(stream!=null)
//            {
//                try {
//                    stream.close();
//                }catch(Exception e)
//                {
//
//                }
//
//            }
//        }
//    }
//
//    public byte[] downLoadDfsFile(String groupName, String remoteFileName) {
//        org.csource.fastdfs.StorageClient client;
//        TrackerServer trackerServer = null;
//        byte[] byteArray = null;
//        try {
//            trackerServer = trackerGroup.getConnection();
//            client = new StorageClient(trackerServer, null);
//            byteArray = client.download_file(groupName, remoteFileName);
//        } catch (MyException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (trackerServer != null) {
//                try {
//                    trackerServer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return byteArray;
//    }
//
//    private String[] send(byte[] fileBuff, String fileExtName) {
//        String[] upPath = null;
//        org.csource.fastdfs.StorageClient client;
//        TrackerServer trackerServer = null;
//        try {
//            trackerServer = trackerGroup.getConnection();
//            client = new StorageClient(trackerServer, null);
//            upPath = client.upload_file(fileBuff, fileExtName, null);
//        } catch (Exception e) {
//            logger.error("###error={}", e.getMessage());
//        } finally {
//            if (trackerServer != null) {
//                try {
//                    trackerServer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return upPath;
//    }
//
//    private String getFileExtName(String name) {
//        String extName = null;
//        if (name != null && name.contains("/")) {
//            extName = name.substring(name.lastIndexOf("/") + 1);
//        }
//        return extName;
//    }
//
//    private byte[] getFileBuffer(File file) {
//        byte[] fileByte = null;
//        try {
//            FileInputStream fis = new FileInputStream(file);
//            fileByte = new byte[fis.available()];
//            fis.read(fileByte);
//            fis.close();
//        } catch (FileNotFoundException e) {
//            logger.error("###error={}", e);
//        } catch (IOException e) {
//            logger.error("###error={}", e);
//        }
//        return fileByte;
//    }

}
