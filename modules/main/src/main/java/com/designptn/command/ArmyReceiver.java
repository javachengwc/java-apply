package com.designptn.command;

//军队作为命令的接收者
public class ArmyReceiver implements  Receiver {

    //进攻
    public void  doAttack() {
        System.out.println("Army attack start............ ");
    }

    //防御
    public void  doDefend() {
        System.out.println("Army defend start............ ");
    }

}
