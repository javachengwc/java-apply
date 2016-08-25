package com.mountain.registry;

import com.mountain.model.SpecUrl;

import java.util.List;

/**
 * 通知监听器
 */
public interface NotifyListener {

    //服务变更通知时触发
    public void notify(List<SpecUrl> urls);

}