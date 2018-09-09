package com.mybatis.pseudocode.mybatis.cache;


import org.apache.ibatis.reflection.ArrayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CacheKey implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1166825526560210L;
    private static final int DEFAULT_MULTIPLYER = 37;
    private static final int DEFAULT_HASHCODE = 17;
    private final int multiplier;
    private int hashcode;
    private long checksum;
    private int count;
    private transient List<Object> updateList;

    public CacheKey()
    {
        this.hashcode = 17;
        this.multiplier = 37;
        this.count = 0;
        this.updateList = new ArrayList();
    }

    public CacheKey(Object[] objects) {
        this();
        updateAll(objects);
    }

    public int getUpdateCount() {
        return this.updateList.size();
    }

    public void update(Object object) {
        int baseHashCode = object == null ? 1 : ArrayUtil.hashCode(object);

        this.count += 1;
        this.checksum += baseHashCode;
        baseHashCode *= this.count;

        this.hashcode = (this.multiplier * this.hashcode + baseHashCode);

        this.updateList.add(object);
    }

    public void updateAll(Object[] objects) {
        for (Object o : objects)
            update(o);
    }

    public boolean equals(Object object)
    {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CacheKey)) {
            return false;
        }

        CacheKey cacheKey = (CacheKey)object;

        if (this.hashcode != cacheKey.hashcode) {
            return false;
        }
        if (this.checksum != cacheKey.checksum) {
            return false;
        }
        if (this.count != cacheKey.count) {
            return false;
        }

        for (int i = 0; i < this.updateList.size(); i++) {
            Object thisObject = this.updateList.get(i);
            Object thatObject = cacheKey.updateList.get(i);
            if (!ArrayUtil.equals(thisObject, thatObject)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode()
    {
        return this.hashcode;
    }

    public String toString()
    {
        StringBuilder returnValue = new StringBuilder().append(this.hashcode).append(':').append(this.checksum);
        for (Iterator localIterator = this.updateList.iterator(); localIterator.hasNext(); ) { Object object = localIterator.next();
            returnValue.append(':').append(ArrayUtil.toString(object));
        }
        return returnValue.toString();
    }

    public CacheKey clone() throws CloneNotSupportedException
    {
        CacheKey clonedCacheKey = (CacheKey)super.clone();
        clonedCacheKey.updateList = new ArrayList(this.updateList);
        return clonedCacheKey;
    }
}
