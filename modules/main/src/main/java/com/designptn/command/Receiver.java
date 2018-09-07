package com.designptn.command;

//命令的接收者
public interface Receiver {

    //进攻
    public void  doAttack();

    //防御
    public void  doDefend();
}
