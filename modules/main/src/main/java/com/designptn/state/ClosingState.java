package com.designptn.state;

//电梯门关闭状态下
public class ClosingState extends LiftState {

    public void close() {
        System.out.println("ClosingState close(),电梯门已是关闭状态，不需要再关闭");
    }

    @Override
    public void open() {
        System.out.println("ClosingState open(),电梯门打开 success");
        super.context.setLiftState(LiftContext.openningState);
    }

    @Override
    public void run() {
        System.out.println("ClosingState run(),电梯运行,或上或下 success");
        super.context.setLiftState(LiftContext.runningState);
    }

    @Override
    public void stop() {
        System.out.println("ClosingState stop(),电梯停止 success");
        super.context.setLiftState(LiftContext.stoppingState);
    }
}