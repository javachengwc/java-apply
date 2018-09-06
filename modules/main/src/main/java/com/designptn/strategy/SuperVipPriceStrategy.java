package com.designptn.strategy;

//超级vip价格策略
public class SuperVipPriceStrategy implements PriceStrategy {

    public double calPrice(double price) {
        return price* 0.6;
    }
}