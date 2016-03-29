package com.zookeeper.curator;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Curator是Netflix开源的一套ZooKeeper客户端框架，用它来操作zookeeper更加简单方便
 */
public class CuratorHelper {

    static CuratorFramework zkclient=null;
    static {

        String zkhost="127.0.0.1:2181";//zk的host
        RetryPolicy rp=new ExponentialBackoffRetry(1000, 3);//重试机制
        Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost)
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000)
                .retryPolicy(rp);
        //builder.namespace(nameSpace);
        CuratorFramework zclient = builder.build();
        zkclient=zclient;
        zkclient.start();
    }

    public static void main(String[] args)throws Exception {
        CuratorHelper ct=new  CuratorHelper();

        ct.checkExist("/zk");
        //ct.getListChildren("/zk/bb");
        //ct.upload("/jianli/123.txt", "D:\\123.txt");
        ct.createrOrUpdate("/zk/cc334/zzz","c");
        //ct.delete("/qinb/bb");
        ct.read("/zk/cc334/zzz");

        Thread.sleep(10*1000l);
        System.out.println("read again");
        try {
            ct.read("/zk/cc334/zzz");
        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }

        System.out.println("read again again");
        Thread.sleep(10*1000l);
        ct.read("/zk/cc334/zzz");

        Thread.sleep(10*1000l);
        //zkclient.close();

    }

    /**
     * 创建或更新一个节点
     * @param path 路径
     * @param content 内容
     */
    public void createrOrUpdate(String path,String content)throws Exception{

        zkclient.newNamespaceAwareEnsurePath(path).ensure(zkclient.getZookeeperClient());
        zkclient.setData().forPath(path,content.getBytes());
        System.out.println("添加成功！！！");

    }

    /**
     * 删除zk节点
     * @param path 删除节点的路径
     */
    public void delete(String path)throws Exception{
        zkclient.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        System.out.println("删除成功!");
    }

    /**
     * 判断路径是否存在
     * @param path
     */
    public void checkExist(String path)throws Exception{

        if(zkclient.checkExists().forPath(path)==null){
            System.out.println("路径不存在!");
        }else{
            System.out.println("路径已经存在!");
        }
    }

    /**
     * 读取的路径
     * @param path
     */
    public void read(String path)throws Exception{

        String data=new String(zkclient.getData().forPath(path),"gbk");

        System.out.println("读取的数据:"+data);

    }

    /**
     * @param path 路径
     * 获取某个节点下的所有子文件
     */
    public void getListChildren(String path)throws Exception{

        List<String> paths=zkclient.getChildren().forPath(path);
        for(String p:paths){
            System.out.println(p);
        }
    }

    /**
     * @param zkPath zk上的路径
     * @param localpath 本地上的文件路径
     */
    public void upload(String zkPath,String localpath)throws Exception{

        createrOrUpdate(zkPath, "");//创建路径
        byte[] bs=FileUtils.readFileToByteArray(new File(localpath));
        zkclient.setData().forPath(zkPath, bs);
        System.out.println("上传文件成功！");
    }
}