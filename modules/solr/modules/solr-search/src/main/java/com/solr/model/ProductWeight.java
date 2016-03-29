package com.solr.model;

public class ProductWeight {
	
	private String name;

	private String field;
	//权重值
	private int weight;
	//权重基数
    private int baseWeight;
    //字段类型
    private int fieldType;
    //权重得分计算字段
    private String fielfBf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBaseWeight() {
        return baseWeight;
    }

    public void setBaseWeight(int baseWeight) {
        this.baseWeight = baseWeight;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public String getFielfBf() {
        return fielfBf;
    }

    public void setFielfBf(String fielfBf) {
        this.fielfBf = fielfBf;
    }
}
