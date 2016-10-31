package com.mountain.registry;

import com.mountain.model.SpecUrl;
import com.mountain.zookeeper.ZookeeperIoiClient;

import java.util.List;

/**
 * 注册接口
 */
public interface Registry {

    //注册中心的根
    public String getRoot();

    //注册服务
    public void register(SpecUrl url);

    //取消注册
    public void unregister(SpecUrl url);

    //订阅服务
    public void subscribe(SpecUrl url, NotifyListener listener);

    //取消订阅
    public void unsubscribe(SpecUrl url, NotifyListener listener);

    //查询
    public List<SpecUrl> query(SpecUrl url);

    public ZookeeperIoiClient getZookeeperClient();

}
