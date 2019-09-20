package com.struct.list;

public class SelfLinkedList {

    //头节点
    private Node head;

    //尾节点
    private Node tail;

    //节点数
    private int size=0;


    //链头新增节点
    public void addHead(Object data){
        Node node = new Node(data);
        if(size == 0){
            head = node;
            tail = node;
        }else{
            node.next = head;
            head.prev=node;
            head = node;
        }
        size++;
    }

    //链尾新增节点
    public void addTail(Object data){
        Node node = new Node(data);
        if(size == 0){
            head = node;
            tail = node;
        }else{
            tail.next = node;
            node.prev=tail;
            tail = node;
        }
        size++;
    }

    //删除头部节点
    public boolean deleteHead(){
        if(size == 0){
            return false;
        }
        head = head.next;
        if(head==null) {
            tail=null;
        }else {
            head.prev=null;
        }
        size--;
        return true;
    }

    //删除尾部节点
    public boolean deleteTail(){
        if(size == 0){
            return false;
        }
        tail = tail.prev;
        if(tail==null) {
            head=null;
        }else {
            tail.next=null;
        }
        size--;
        return true;
    }

    //是否为空
    public boolean isEmpty(){
        return size ==0;
    }
    //节点个数
    public int getSize(){
        return size;
    }

    //显示节点信息
    public void display(){
        if(size >0){
            Node node = head;
            StringBuffer buf = new StringBuffer("[");
            while(node!=null){
                buf.append(node.data);
                node = node.next;
                if(node!=null) {
                    buf.append(",");
                }
            }
            buf.append("]");
            System.out.print(buf.toString());
        }else{
            //无节点
            System.out.println("[]");
        }
    }

}

