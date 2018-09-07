package com.designptn.observer;

//发布者
public interface Publisher {

    public void change(int point);

    //添加订阅
    public void attachSub(Observer observer);

    //发布事件
    public void publish();
}
