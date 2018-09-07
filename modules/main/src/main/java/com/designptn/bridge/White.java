package com.designptn.bridge;

public class White implements Color {

    public void paint(String shapeName) {
        System.out.println("此"+shapeName+"的颜色是白颜色");
    }
}
