package com.designptn.strategy;

public class Buyer {

    //消费总额
    private Double totalAmount = 0D;

    //当前消费金额
    private Double amount = 0D;

    //计算价格的策略，初始为原价计价策略
    private PriceStrategy priceStrategy = new OriginalPriceStrategy();

    //买东西
    public void buy(Double amount) {
        this.amount = amount;
        totalAmount += amount;
        if (totalAmount > 100000) {
            priceStrategy = new SuperVipPriceStrategy();
        } else if (totalAmount > 20000) {
            priceStrategy = new VipPriceStrategy();
        }
    }

    //算价
    public Double calPrice() {
        return priceStrategy.calPrice(amount);
    }
}
