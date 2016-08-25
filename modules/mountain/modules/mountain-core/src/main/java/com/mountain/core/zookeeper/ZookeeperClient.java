package com.mountain.core.zookeeper;

/**
 * zookeeper客户端
 */
public interface ZookeeperClient {

    public void writeData(String path,String value);
}
