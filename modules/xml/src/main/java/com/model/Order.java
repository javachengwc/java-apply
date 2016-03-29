package com.model;

import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Root
public class Order {
    @Attribute(required = true)
    private Long id;
    @Element(required = true)
    private Date cdate;
    @ElementList(required = false, type = Item.class)
    private List<Item> itemList = new ArrayList<Item>(0);

    public Order() {
    }

    public Order(Long id, Date cdate) {
        this.id = id;
        this.cdate = cdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
