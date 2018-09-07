package com.designptn.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterMain {

    public static void main(String [] args ) {

        Person a = new Person("a","china",9,1);
        Person b = new Person("b","china",20,0);
        Person c = new Person("c","other",30,1);
        Person d = new Person("d","china",30,0);
        Person e = new Person("e","china",20,1);
        Person f = new Person("f","china",50,0);
        List<Person> list = new ArrayList<Person>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);

        IFilter ageFilter = new AgeFilter(10,30);
        IFilter countryFilter = new CountryFilter("china");
        IFilter genderFilter= new GenderFilter(1);

        IFilter andFilter = new AndFilter(ageFilter,countryFilter,genderFilter);

        List<Person> rtList = andFilter.filter(list);

        int rtCnt = rtList==null?0:rtList.size();
        System.out.println("after filter ,rest count="+rtCnt);
        for(Person person:rtList) {
            System.out.println(person);
        }
    }
}
