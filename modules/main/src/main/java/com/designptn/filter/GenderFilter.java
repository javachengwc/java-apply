package com.designptn.filter;

import java.util.ArrayList;
import java.util.List;

//性别过滤器
public class GenderFilter implements IFilter {

    private int gender;

    public GenderFilter(int gender) {
        this.gender=gender;
    }

    public List<Person> filter(List<Person> list) {
        if(list==null) {
            return null;
        }
        List<Person> rtList = new ArrayList<Person>();
        for(Person person:list) {
            int personGender= person.getGender();
            if(personGender==gender) {
                rtList.add(person);
            }
        }
        return rtList;
    }
}
