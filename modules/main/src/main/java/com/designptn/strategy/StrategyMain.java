package com.designptn.strategy;

public class StrategyMain {

    public static void main(String[] args) {
        Buyer buyer = new Buyer();
        double goodsPrice=10000D;
        buyer.buy(goodsPrice);
        double payPrice= buyer.calPrice();
        System.out.println("买家买价格为:"+goodsPrice+"的商品，实际支付价格为:"+payPrice);

        goodsPrice=50000D;
        buyer.buy(goodsPrice);
        payPrice= buyer.calPrice();
        System.out.println("买家买价格为:"+goodsPrice+"的商品，实际支付价格为:"+payPrice);

        goodsPrice=80000D;
        buyer.buy(goodsPrice);
        payPrice= buyer.calPrice();
        System.out.println("买家买价格为:"+goodsPrice+"的商品，实际支付价格为:"+payPrice);
    }
}
