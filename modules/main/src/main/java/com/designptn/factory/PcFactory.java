package com.designptn.factory;

//pc工厂
public interface PcFactory {

    //生产鼠标
    public Mouse productMouse();

    //生产键盘
    public Keyboard productKeyboard();
}
