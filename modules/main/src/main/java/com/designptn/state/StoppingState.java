package com.designptn.state;

public class StoppingState extends LiftState {

    public void close() {
        System.out.println("StoppingState close(),电梯已停止,电梯门已是关闭状态，不需要再关闭");
    }

    public void open() {
        System.out.println("StoppingState open(),电梯门开启 success");
        super.context.setLiftState(LiftContext.openningState);
    }

    public void run() {
        System.out.println("StoppingState run(),电梯运行,或上或下 success");
        super.context.setLiftState(LiftContext.runningState);
    }

    public void stop() {
        System.out.println("StoppingState stop(),电梯已停止,不需要再停止");
    }
}