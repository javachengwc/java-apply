package com.designptn.template;

public class Football extends Game {

    public void endPlay() {
        System.out.println("football game finish");
    }

    public void initialize() {
        System.out.println("football game initialize");
    }

    public void startPlay() {
        System.out.println("football game start");
    }
}