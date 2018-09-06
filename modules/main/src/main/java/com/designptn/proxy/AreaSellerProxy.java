package com.designptn.proxy;

//区域销售代理
public class AreaSellerProxy implements Seller{

    private CompanySeller companySeller;

    public AreaSellerProxy(CompanySeller companySeller) {
        this.companySeller=companySeller;
    }

    public void sell() {
        System.out.println("sell direct from areaSellerProxy,");
        companySeller.sell();
    }
}
