package com.designptn.factory;

public class HpFactory implements PcFactory {

    public Mouse productMouse() {
        return new HpMouse();
    }

    public Keyboard productKeyboard() {
        return new HpKeyboard();
    }
}
