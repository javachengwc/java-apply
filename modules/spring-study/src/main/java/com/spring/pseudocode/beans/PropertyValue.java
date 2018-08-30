package com.spring.pseudocode.beans;

public class PropertyValue {

    private final String name;
    private final Object value;

    private boolean converted = false;
    private Object convertedValue;
    volatile Boolean conversionNecessary;
    volatile transient Object resolvedTokens;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public PropertyValue(PropertyValue original) {
        this.name = original.getName();
        this.value = original.getValue();
        this.converted = original.converted;
        this.convertedValue = original.convertedValue;
        this.conversionNecessary = original.conversionNecessary;
        this.resolvedTokens = original.resolvedTokens;
        //...
    }

    public PropertyValue(PropertyValue original, Object newValue) {
        this.name = original.getName();
        this.value = newValue;
        this.conversionNecessary = original.conversionNecessary;
        this.resolvedTokens = original.resolvedTokens;
        //...
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean isConverted() {
        return converted;
    }

    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    public Object getConvertedValue() {
        return convertedValue;
    }

    public void setConvertedValue(Object convertedValue) {
        this.convertedValue = convertedValue;
    }

    public Boolean getConversionNecessary() {
        return conversionNecessary;
    }

    public void setConversionNecessary(Boolean conversionNecessary) {
        this.conversionNecessary = conversionNecessary;
    }

    public Object getResolvedTokens() {
        return resolvedTokens;
    }

    public void setResolvedTokens(Object resolvedTokens) {
        this.resolvedTokens = resolvedTokens;
    }
}