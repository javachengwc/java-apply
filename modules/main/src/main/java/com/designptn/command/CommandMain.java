package com.designptn.command;

/**
 *命令模式是通过命令发送者和命令执行者的解耦来完成对命令的具体控制的。
 *命令模式是对功能方法的抽象，并不是对对象的抽象。
 *命令模式是将功能提升到对象来操作，以便对多个功能进行一系列的处理以及封装。
 */
public class CommandMain {

    public static void main(String [] args ) {

        //指挥官
        Invoker invoker =new Invoker();

        //军队
        Receiver army=new ArmyReceiver();

        Command command = new AttackCommand(army);
        //指挥官发起进攻命令
        System.out.println("指挥官发起进攻命令");
        invoker.action(command);

        command= new DefendCommand(army);
        //指挥官发起防御命令
        System.out.println("指挥官发起防御命令");
        invoker.action(command);
    }
}
