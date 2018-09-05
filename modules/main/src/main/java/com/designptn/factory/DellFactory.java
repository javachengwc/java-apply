package com.designptn.factory;

public class DellFactory implements PcFactory {

    public Mouse productMouse() {
        return new DellMouse();
    }

    public Keyboard productKeyboard() {
        return new DellKeyboard();
    }
}
