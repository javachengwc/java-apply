package com.designptn.strategy;

//原价策略
public class OriginalPriceStrategy implements PriceStrategy {

    public double calPrice(double price) {
        return price;
    }
}
