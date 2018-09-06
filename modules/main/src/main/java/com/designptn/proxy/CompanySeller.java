package com.designptn.proxy;

//公司销售
public class CompanySeller implements Seller {

    public void sell() {
        System.out.println("sell from companySeller");
    }
}
