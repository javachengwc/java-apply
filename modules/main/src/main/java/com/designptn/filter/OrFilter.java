package com.designptn.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrFilter implements IFilter {

    private List<IFilter> filterList = new ArrayList<IFilter>();

    public OrFilter() {
        this.filterList=filterList;
    }

    public List<Person> filter(List<Person> list) {
        if(list==null) {
            return null;
        }
        Set<Person> resultSet = new HashSet<Person>();

        for(IFilter filter:filterList) {
            List<Person> rtList =filter.filter(list);
            if(rtList!=null) {
                for(Person person:rtList) {
                    resultSet.add(person);
                }
            }
        }
        List<Person> rtList = new ArrayList<Person>(resultSet);
        return rtList;
    }
}
