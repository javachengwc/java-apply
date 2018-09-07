package com.designptn.bridge;

//形状
public abstract class Shape {

    protected Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw() {
        color.paint(getName());
    }

    public  abstract String getName();
}
