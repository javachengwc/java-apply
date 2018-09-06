package com.designptn.state;

public class StateMain {

    public static void main(String[] args) {

        LiftContext lift = new LiftContext();
        lift.init();

        //电梯门开启
        lift.open();

        //电梯门关闭
        lift.close();

        //电梯运行
        lift.run();

        lift.open();

        //电梯停止
        lift.stop();
    }
}
