package com.designptn.proxy;

public class SellMain {

    public static void main(String args []) {
        CompanySeller companySeller = new CompanySeller();
        Seller seller = new AreaSellerProxy(companySeller);
        seller.sell();
    }
}
