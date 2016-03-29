package com.xml.simple;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

@Root
public class Main {

    @Element
    private String text;

    @Attribute
    private int index;

    @Element()
    private boolean flag;

    @Element(required = false)
    private Integer num;

    @ElementList(required = false)
    private List<String> slist = new ArrayList<String>();

    public Main() {
    }

    public Main(String text, int index) {
        this.text = text;
        this.index = index;
    }

    public String getMessage() {
        return text;
    }

    public int getId() {
        return index;
    }

    public static void main(String[] args) throws Exception {
        Serializer serializer = new Persister();
        Main example = new Main("Example message", 123);
        File result = new File("E:/tmp/example.xml");
        serializer.write(example, result);
        Main _obj = serializer.read(Main.class, result);
        System.out.println(_obj.getMessage());
    }
}