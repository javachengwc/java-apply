package com.mybatis.pseudocode.mybatis.cursor.defaults;


import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.resultset.DefaultResultSetHandler;
import com.mybatis.pseudocode.mybatis.executor.resultset.ResultSetWrapper;
import com.mybatis.pseudocode.mybatis.mapping.ResultMap;
import com.mybatis.pseudocode.mybatis.session.ResultContext;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DefaultCursor<T> implements Cursor<T>
{
    private final DefaultResultSetHandler resultSetHandler;
    private final ResultMap resultMap;
    private final ResultSetWrapper rsw;
    private final RowBounds rowBounds;
    private final ObjectWrapperResultHandler<T> objectWrapperResultHandler = new ObjectWrapperResultHandler();

    private final DefaultCursor<T>.CursorIterator cursorIterator = new CursorIterator();
    private boolean iteratorRetrieved;
    private CursorStatus status = CursorStatus.CREATED;
    private int indexWithRowBound = -1;

    public DefaultCursor(DefaultResultSetHandler resultSetHandler, ResultMap resultMap, ResultSetWrapper rsw, RowBounds rowBounds)
    {
        this.resultSetHandler = resultSetHandler;
        this.resultMap = resultMap;
        this.rsw = rsw;
        this.rowBounds = rowBounds;
    }

    public boolean isOpen()
    {
        return this.status == CursorStatus.OPEN;
    }

    public boolean isConsumed()
    {
        return this.status == CursorStatus.CONSUMED;
    }

    public int getCurrentIndex()
    {
        return this.rowBounds.getOffset() + this.cursorIterator.iteratorIndex;
    }

    public Iterator<T> iterator()
    {
        if (this.iteratorRetrieved) {
            throw new IllegalStateException("Cannot open more than one iterator on a Cursor");
        }
        this.iteratorRetrieved = true;
        return this.cursorIterator;
    }

    public void close()
    {
        if (isClosed()) {
            return;
        }

        ResultSet rs = this.rsw.getResultSet();
        try {
            if (rs != null) {
                rs.close();
                Statement statement = rs.getStatement();
                if (statement != null) {
                    statement.close();
                }
            }
            this.status = CursorStatus.CLOSED;
        }
        catch (SQLException localSQLException) {
        }
    }

    protected T fetchNextUsingRowBound() {
        T result = fetchNextObjectFromDatabase();
        while ((result != null) && (this.indexWithRowBound < this.rowBounds.getOffset())) {
            result = fetchNextObjectFromDatabase();
        }
        return result;
    }

    protected T fetchNextObjectFromDatabase() {
        if (isClosed()) {
            return null;
        }
        try
        {
            this.status = CursorStatus.OPEN;
            this.resultSetHandler.handleRowValues(this.rsw, this.resultMap, this.objectWrapperResultHandler, RowBounds.DEFAULT, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        T next = this.objectWrapperResultHandler.result;
        if (next != null) {
            this.indexWithRowBound += 1;
        }

        if ((next == null) || (getReadItemsCount() == this.rowBounds.getOffset() + this.rowBounds.getLimit())) {
            close();
            this.status = CursorStatus.CONSUMED;
        }
        //ObjectWrapperResultHandler.access$202(this.objectWrapperResultHandler, null);

        return next;
    }

    private boolean isClosed() {
        return (this.status == CursorStatus.CLOSED) || (this.status == CursorStatus.CONSUMED);
    }

    private int getReadItemsCount() {
        return this.indexWithRowBound + 1;
    }

    private class CursorIterator implements Iterator<T>
    {
        T object;
        int iteratorIndex = -1;

        private CursorIterator() {
        }
        public boolean hasNext() { if (this.object == null) {
            this.object = DefaultCursor.this.fetchNextUsingRowBound();
        }
            return this.object != null;
        }

        public T next()
        {
            T next = this.object;

            if (next == null) {
                next = DefaultCursor.this.fetchNextUsingRowBound();
            }

            if (next != null) {
                this.object = null;
                this.iteratorIndex += 1;
                return next;
            }
           throw new NoSuchElementException();

        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove element from Cursor");
        }
    }

    private static class ObjectWrapperResultHandler<T> implements ResultHandler<T>
    {
        private T result;

        public void handleResult(ResultContext<? extends T> context)
        {
            this.result = context.getResultObject();
            context.stop();
        }
    }

    private static enum CursorStatus
    {
        CREATED,

        OPEN,

        CLOSED,

        CONSUMED;
    }
}
