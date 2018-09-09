package com.mybatis.pseudocode.mybatis.session;

public class RowBounds
{
    public static final int NO_ROW_OFFSET = 0;
    public static final int NO_ROW_LIMIT = 2147483647;
    public static final RowBounds DEFAULT = new RowBounds();
    private final int offset;
    private final int limit;

    public RowBounds()
    {
        this.offset = 0;
        this.limit = 2147483647;
    }

    public RowBounds(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLimit() {
        return this.limit;
    }
}
