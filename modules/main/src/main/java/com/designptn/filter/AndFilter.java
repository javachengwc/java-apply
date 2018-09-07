package com.designptn.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndFilter implements IFilter {

    private List<IFilter> filterList = new ArrayList<IFilter>();

    public AndFilter(List<IFilter> filterList) {
        this.filterList=filterList;
    }

    public  AndFilter(IFilter ... filters) {
        if(filters!=null && filters.length>0) {
            this.filterList = Arrays.asList(filters);
        }
    }

    public List<Person> filter(List<Person> list) {
        if(list==null) {
            return null;
        }
        List<Person> rtList = new ArrayList<Person>(list);
        for(IFilter filter:filterList) {
            rtList =filter.filter(rtList);
        }
        return rtList;
    }
}
