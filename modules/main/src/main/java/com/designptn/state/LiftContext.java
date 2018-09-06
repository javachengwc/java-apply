package com.designptn.state;

//电梯上下文
public class LiftContext {

    public static LiftState closingState = new ClosingState();

    public static LiftState openningState = new OpenningState();

    public static LiftState runningState = new RunningState();

    public static LiftState stoppingState = new StoppingState();

    //电梯状态 初始关闭
    private LiftState liftState;

    public LiftState getLiftState() {
        return liftState;
    }

    public void setLiftState(LiftState liftState) {
        this.liftState = liftState;
        this.liftState.setContext(this);
    }

    public void init() {
        setLiftState(closingState);
    }

    //电梯开门
    public void open() {
        liftState.open();
    }

    //电梯关门
    public  void close() {
        liftState.close();
    }

    //电梯运行，或上或下
    public  void run() {
        liftState.run();
    }

    //电梯停下来
    public  void stop() {
        liftState.stop();
    }
}
