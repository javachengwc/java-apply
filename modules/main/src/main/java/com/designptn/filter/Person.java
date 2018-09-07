package com.designptn.filter;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Person {

    private String name;

    //国家
    private String country;

    //年龄
    private int age;

    //性别 0--女 1--男
    private int gender;

    public Person() {

    }

    public Person(String name,String country,int age,int gender) {
        this.name=name;
        this.country=country;
        this.age=age;
        this.gender=gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
