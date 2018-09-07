package com.designptn.observer;

//股东
public class StockholderObserver implements Observer {

    public void fresh(int curPoint) {
        System.out.println("StockholderObserver fresh curPoint="+curPoint);
        System.out.println("股东收到大盘变化事情，当前指数为:"+curPoint);
    }
}