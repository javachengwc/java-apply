package com.database.tool.vo;

public class Field {

    private String field;

    private String title;

    private double width = 100;

    public Field() {

    }

    public Field(String field) {
        this.field = field;
        this.title = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
