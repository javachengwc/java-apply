package com.designptn.facade;

//医院
public class Hospital {

    private String name;

    public Hospital(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //挂号
    public void reg(Patient patient) {
        System.out.println("病人"+patient.getName()+"挂号");
    }

    //划价
    public void pricing(Patient patient) {
        System.out.println("给病人"+patient.getName()+"划价");
    }

    //缴费
    public void pay(Patient patient) {
        System.out.println("病人"+patient.getName()+"缴费");
    }

    //取药
    public void takeMedicine(Patient patient) {
        System.out.println("病人"+patient.getName()+"取药");
    }
}
