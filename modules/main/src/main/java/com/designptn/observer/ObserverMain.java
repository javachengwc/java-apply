package com.designptn.observer;

public class ObserverMain {

    public static void main(String [] args) {
        //发布者
        Publisher publisher=new MarketPublisher();

        //订阅者,也是观察者
        Observer stockerObserver = new StockerObserver();
        Observer stockholderObserver = new StockholderObserver();

        //订阅
        publisher.attachSub(stockerObserver);
        publisher.attachSub(stockholderObserver);

        //发布者业务变动触发事件发布
        publisher.change(10);

    }
}
