package com.spring.pseudocode.beans.factory;

import com.spring.pseudocode.beans.BeansException;

public interface ObjectFactory <T> {
    T getObject() throws BeansException;
}
