package com.designptn.bridge;

public class BridgeMain {

    public static void main(String [] args) {

        Shape circle = new Circle();
        circle.setColor(new Black());

        Shape square = new Square();
        square.setColor(new White());

        circle.draw();
        square.draw();
    }
}
