package com.designptn.facade;

//医院前台(门面)
public class HosFronterFacade {

    private String name ;

    private Hospital hospital;

    private Patient patient;

    public HosFronterFacade(String name,Hospital hospital) {
        this.name=name;
        this.hospital=hospital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void recept(Patient patient) {
        System.out.println("医院前台"+name+"接待病人"+patient.getName());
        this.patient=patient;
    }

    public void guideSeeIll() {
        if(patient==null) {
            System.out.println("没有接待病人");
        }
        System.out.println("医院前台"+name+"引领病人"+patient.getName()+"看病");
        //挂号
        hospital.reg(patient);
        //划价
        hospital.pricing(patient);
        //缴费
        hospital.pay(patient);
        //取药
        hospital.takeMedicine(patient);
        System.out.println("医院前台"+name+"引领病人"+patient.getName()+"看病完成");
    }
}
