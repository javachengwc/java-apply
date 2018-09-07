package com.designptn.bridge;

public class Black implements Color {

    public void paint(String shapeName) {
        System.out.println("此"+shapeName+"的颜色是黑颜色");
    }
}
