package com.hibernate.pseudocode.core;


import com.hibernate.pseudocode.core.type.Type;

public abstract interface SQLQuery extends Query
{
    public abstract SQLQuery addEntity(String param);

    public abstract SQLQuery addEntity(String param1, String param2);

    //...

    public abstract SQLQuery addEntity(Class paramClass);

    public abstract SQLQuery addEntity(String param, Class paramClass);


    public abstract SQLQuery addJoin(String param1, String param2);


    public abstract SQLQuery addScalar(String param, Type type);

    public abstract SQLQuery addScalar(String param);

    public abstract SQLQuery setResultSetMapping(String param);

    public abstract SQLQuery addSynchronizedQuerySpace(String param);
}
