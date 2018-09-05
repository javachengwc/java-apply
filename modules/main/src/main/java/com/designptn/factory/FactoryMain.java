package com.designptn.factory;

public class FactoryMain {

    public static void main(String args []) {
        PcFactory hpFactory =new HpFactory();
        PcFactory dellFactory= new DellFactory();

        Mouse hpMouse = hpFactory.productMouse();
        Mouse dellMouse = dellFactory.productMouse();

        Keyboard hpKeyboard = hpFactory.productKeyboard();
        Keyboard dellKeyboard = dellFactory.productKeyboard();

        System.out.println("hpMouse-->"+hpMouse.desc());
        System.out.println("hpKeyboard-->"+hpKeyboard.desc());

        System.out.println("dellMouse-->"+dellMouse.desc());
        System.out.println("dellKeyboard-->"+dellKeyboard.desc());
    }
}
