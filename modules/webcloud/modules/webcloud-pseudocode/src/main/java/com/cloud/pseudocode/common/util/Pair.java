package com.cloud.pseudocode.common.util;

import java.io.Serializable;

public class Pair<E1,E2> implements Serializable {

    private static final long serialVersionUID = 2L;

    private E1 mFirst;
    private E2 mSecond;
    public Pair(E1 first, E2 second) {
        mFirst = first;
        mSecond = second;
    }

    public E1 first() {
        return mFirst;
    }

    public E2 second() {
        return mSecond;
    }

    public void setFirst(E1 first) {
        mFirst = first;
    }

    public void setSecond(E2 second) {
        mSecond = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Pair other = (Pair)obj;
        return HashCode.equalObjects(mFirst, other.mFirst)
                && HashCode.equalObjects(mSecond, other.mSecond);
    }

    @Override
    public int hashCode() {
        HashCode h = new HashCode();
        h.addValue(mFirst);
        h.addValue(mSecond);
        return h.hashCode();
    }

}
