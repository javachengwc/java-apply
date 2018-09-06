package com.designptn.strategy;

//价格策略
public interface PriceStrategy {

    //根据原价返回一个最终的价格
    public double calPrice(double price);
}
