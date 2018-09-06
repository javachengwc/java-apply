package com.designptn.state;

//电梯门开启状态下
public class OpenningState extends LiftState {

    public void close() {
        System.out.println("OpenningState close(),电梯门关闭 success");
        super.context.setLiftState(LiftContext.closingState);
    }

    public void open() {
        System.out.println("OpenningState open(),电梯门已是开启状态，不需要再开启");
    }

    public void run() {
        System.out.println("OpenningState run(),电梯门开启状态下，不能上行或下行");
    }
    public void stop() {
        System.out.println("OpenningState stop(),电梯门开启状态下是没运行的，不用停止");
    }
}