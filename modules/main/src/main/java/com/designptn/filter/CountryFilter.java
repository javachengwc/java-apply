package com.designptn.filter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

//国别过滤器
public class CountryFilter implements IFilter {

    private String country;

    public CountryFilter(String country) {
        this.country=country;
    }

    public List<Person> filter(List<Person> list) {
        if(list==null) {
            return null;
        }
        List<Person> rtList = new ArrayList<Person>();
        for(Person person:list) {
            String personCountry= person.getCountry();
            if(StringUtils.isNotBlank(personCountry) && personCountry.equalsIgnoreCase(country)) {
                rtList.add(person);
            }
        }
        return rtList;
    }
}
