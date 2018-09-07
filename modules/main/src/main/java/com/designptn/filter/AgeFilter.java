package com.designptn.filter;

import java.util.ArrayList;
import java.util.List;

public class AgeFilter implements IFilter {

    private int minAge;

    private int maxAge;

    public AgeFilter(int minAge,int maxAge) {
        this.minAge=minAge;
        this.maxAge=maxAge;
    }

    public List<Person> filter(List<Person> list) {
        if(list==null) {
            return null;
        }
        List<Person> rtList = new ArrayList<Person>();
        for(Person person:list) {
            int personAge= person.getAge();
            if(personAge>=minAge && personAge<=maxAge) {
                rtList.add(person);
            }
        }
        return rtList;
    }
}