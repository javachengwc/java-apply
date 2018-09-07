package com.designptn.command;

//防御命令
public class DefendCommand  implements Command {

    private Receiver receiver;

    public DefendCommand(Receiver receiver) {
        this.receiver=receiver;
    }

    public  void  execute() {
        System.out.println("DefendCommand execute start..........");
        receiver.doDefend();
    }
}
