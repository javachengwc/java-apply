package com.designptn.strategy;

//vip价格策略，八折
public class VipPriceStrategy implements PriceStrategy {

    public double calPrice(double price) {
        return price* 0.8;
    }
}