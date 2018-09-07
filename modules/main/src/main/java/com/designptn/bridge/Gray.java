package com.designptn.bridge;

public class Gray implements Color {

    public void paint(String shapeName) {
        System.out.println("此"+shapeName+"的颜色是灰颜色");
    }
}