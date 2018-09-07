package com.designptn.filter;

import java.util.List;

//过滤器
public interface IFilter {

    public List<Person> filter(List<Person> list);
}
