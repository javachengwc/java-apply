package com.designptn.chain;

public abstract class AbstractLogger {

    public static int DEBUG = 1;

    public static int INFO = 2;

    public static int ERROR = 3;

    protected int level=DEBUG;

    protected AbstractLogger nextLogger;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public AbstractLogger getNextLogger() {
        return nextLogger;
    }

    public void setNextLogger(AbstractLogger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void log(int level, String message){
        if(this.level <= level){
            write(message);
        }
        if(nextLogger !=null){
            nextLogger.log(level, message);
        }
    }

    public abstract void write(String message);

}