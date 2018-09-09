package com.mybatis.pseudocode.mybatis.cursor;

import java.io.Closeable;

public abstract interface Cursor<T> extends Closeable, Iterable<T>
{
    public abstract boolean isOpen();

    public abstract boolean isConsumed();

    public abstract int getCurrentIndex();
}
