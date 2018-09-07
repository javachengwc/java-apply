package com.designptn.observer;

//炒股者
public class StockerObserver implements Observer {

    public void fresh(int curPoint) {
        System.out.println("StockerObserver fresh curPoint="+curPoint);
        System.out.println("炒股者收到大盘变化事情，当前指数为:"+curPoint);
    }
}
