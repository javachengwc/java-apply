package com.designptn.facade;

/**
 *  外观模式也称为门面模式
 *  病人到医院看病，需要到不同部门挂号，划价、缴费、取药，非常不便
 *  解决这种不便的方法便是引进门面模式，设置一个接待员，由接待员负责代为挂号、划价、缴费、取药等。
 *  这个接待员就是门面模式的体现，病人只接触接待员，由接待员与各个部门打交道。
 */
public class FacadeMain {

    public static void main(String args []) {
        Patient p= new Patient("小刚");
        Hospital h=new Hospital("美好医院");
        HosFronterFacade fronterFacade = new HosFronterFacade("小美",h);
        fronterFacade.recept(p);
        fronterFacade.guideSeeIll();
    }
}
