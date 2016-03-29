package com.model;

import org.simpleframework.xml.*;

import java.math.BigDecimal;

@Root
public class Item {
    @Attribute
    private Long id;
    @Element(required = true)
    private int num;
    @Element(required = false)
    private BigDecimal price;

    public Item() {
    }

    public Item(int num, Long id) {
        this.num = num;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
