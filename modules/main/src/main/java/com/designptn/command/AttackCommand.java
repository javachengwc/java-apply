package com.designptn.command;

//进攻命令
public class AttackCommand implements Command {

    private Receiver receiver;

    public AttackCommand(Receiver receiver) {
        this.receiver=receiver;
    }

    public  void  execute() {
        System.out.println("AttackCommand execute start..........");
        receiver.doAttack();
    }
}
