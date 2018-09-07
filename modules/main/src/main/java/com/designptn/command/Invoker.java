package com.designptn.command;

//指挥官(调用命令的角色)
public class Invoker {

    public void action(Command command) {
        command.execute();
    }
}
