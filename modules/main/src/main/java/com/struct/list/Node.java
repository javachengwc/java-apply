package com.struct.list;

import lombok.Data;

@Data
public class Node {

    protected Object data;

    protected Node prev;

    protected Node next;

    public Node() {

    }


    public Node(Object data) {
        this.data=data;
    }
}
