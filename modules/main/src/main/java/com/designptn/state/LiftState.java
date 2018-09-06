package com.designptn.state;

//状态策略的电梯
public abstract class LiftState{

    //电梯上下文
    protected LiftContext context;

    public void setContext(LiftContext _context){
        this.context = _context;
    }

    //电梯开门
    public abstract void open();

    //电梯关门
    public abstract void close();

    //电梯运行，或上或下
    public abstract void run();

    //电梯停下来
    public abstract void stop();
}
