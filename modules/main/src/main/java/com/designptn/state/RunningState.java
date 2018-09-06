package com.designptn.state;

//电梯运行状态下
public class RunningState extends LiftState {

    public void close() {
        System.out.println("RunningState close(),电梯正在运行,电梯门已经关闭，不需要再关闭");
    }

    public void open() {
        System.out.println("RunningState open(),电梯正在运行,不能打开");
    }

    public void run() {
        System.out.println("RunningState run(),电梯已是运行状态，不需要再变成运行");
    }

    public void stop() {
        System.out.println("RunningState stop(),电梯停止 success");
        super.context.setLiftState(LiftContext.stoppingState);
    }
}