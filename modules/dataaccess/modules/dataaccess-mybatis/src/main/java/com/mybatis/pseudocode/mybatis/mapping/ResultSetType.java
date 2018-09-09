package com.mybatis.pseudocode.mybatis.mapping;

public enum ResultSetType
{
    FORWARD_ONLY(1003),
    SCROLL_INSENSITIVE(1004),
    SCROLL_SENSITIVE(1005);

    private final int value;

    private ResultSetType(int value) { this.value = value; }

    public int getValue()
    {
        return this.value;
    }
}
