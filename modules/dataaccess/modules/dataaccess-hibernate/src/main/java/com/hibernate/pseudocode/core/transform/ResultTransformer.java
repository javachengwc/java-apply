package com.hibernate.pseudocode.core.transform;

import java.io.Serializable;
import java.util.List;

public abstract interface ResultTransformer extends Serializable
{
    public abstract Object transformTuple(Object[] paramArrayOfObject, String[] paramArrayOfString);

    public abstract List transformList(List paramList);
}
