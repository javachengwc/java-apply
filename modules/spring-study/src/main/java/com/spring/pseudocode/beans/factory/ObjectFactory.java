package com.spring.pseudocode.beans.factory;

public interface ObjectFactory <T> {
    T getObject() throws org.springframework.beans.BeansException;
}
