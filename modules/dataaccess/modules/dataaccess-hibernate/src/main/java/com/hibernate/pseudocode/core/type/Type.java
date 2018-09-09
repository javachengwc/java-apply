package com.hibernate.pseudocode.core.type;


import java.io.Serializable;

public abstract interface Type extends Serializable {

    public abstract boolean isAssociationType();

    public abstract boolean isCollectionType();

    public abstract boolean isComponentType();

    public abstract boolean isEntityType();

    public abstract boolean isAnyType();

    //...
}

