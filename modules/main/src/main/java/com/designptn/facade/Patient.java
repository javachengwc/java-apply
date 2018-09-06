package com.designptn.facade;

//病人
public class Patient {

    private String name;

    public Patient(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void seeIll(Hospital hos) {
        System.out.println(name+"去"+hos.getName()+"看病");
    }
}
