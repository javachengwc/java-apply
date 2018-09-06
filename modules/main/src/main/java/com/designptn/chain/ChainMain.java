package com.designptn.chain;

public class ChainMain {

    private static AbstractLogger getChainLogger(){
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.DEBUG);
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.INFO);
        consoleLogger.setNextLogger(fileLogger);
        return consoleLogger;
    }

    public static void main(String[] args) {
        AbstractLogger loggerChain = getChainLogger();

        loggerChain.log(AbstractLogger.DEBUG,"this is first log");
        loggerChain.log(AbstractLogger.INFO,"this is second log");
        loggerChain.log(AbstractLogger.ERROR,"this is three log");
    }
}
