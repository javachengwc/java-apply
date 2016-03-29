package com.model;

import org.simpleframework.xml.*;

@Root
public class Address {
    @Element(required = true)
    private String code;
    @Element
    private String street;
    @Element
    private boolean isopen;

    public Address() {
    }

    public Address(String code, String street, boolean isopen) {
        this.code = code;
        this.street = street;
        this.isopen = isopen;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public boolean isIsopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }
}
