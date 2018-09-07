package com.designptn.observer;

import java.util.ArrayList;
import java.util.List;

//股市
public class MarketPublisher  implements Publisher {

    private List<Observer> observerList= new ArrayList<Observer>();

    //大盘指数
    private int marketPoint;

    public void attachSub(Observer observer) {
        observerList.add(observer);
    }

    //大盘指数变化
    public void change(int point) {
        int prePoint = marketPoint;
        marketPoint=marketPoint+point;
        System.out.println("大盘指数变化,由"+prePoint+"点变成"+marketPoint+"点");
        publish();
    }

    //发布变化事件
    public void publish() {
        System.out.println("发布大盘指数变化事件");
        for(Observer observer:observerList) {
            observer.fresh(marketPoint);
        }
    }
}
