package com.mybatis.pseudocode.mybatis.builder.annotation;


import com.mybatis.pseudocode.mybatis.annotations.*;
import com.mybatis.pseudocode.mybatis.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MapperAnnotationBuilder {

    private final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet();
    private final Set<Class<? extends Annotation>> sqlProviderAnnotationTypes = new HashSet();
    private final Configuration configuration;
    private final Class<?> type;

    public MapperAnnotationBuilder(Configuration configuration, Class<?> type) {
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
        //this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.configuration = configuration;
        this.type = type;

        this.sqlAnnotationTypes.add(Select.class);
        this.sqlAnnotationTypes.add(Insert.class);
        this.sqlAnnotationTypes.add(Update.class);
        this.sqlAnnotationTypes.add(Delete.class);

        this.sqlProviderAnnotationTypes.add(SelectProvider.class);
//        this.sqlProviderAnnotationTypes.add(InsertProvider.class);
//        this.sqlProviderAnnotationTypes.add(UpdateProvider.class);
//        this.sqlProviderAnnotationTypes.add(DeleteProvider.class);
    }

    public void parse() {
        String resource = this.type.toString();
        if (!this.configuration.isResourceLoaded(resource)) {
            //loadXmlResource();
            this.configuration.addLoadedResource(resource);
            //parseCache();
            //parseCacheRef();
            Method[] methods = this.type.getMethods();
            for (Method method : methods) {
                try {
                    if (!method.isBridge())
                        parseStatement(method);
                } catch (Exception e) {
                   //...
                }
            }
        }
        parsePendingMethods();
    }

    private void parsePendingMethods() {
        Collection incompleteMethods = this.configuration.getIncompleteMethods();
        synchronized (incompleteMethods) {
            Iterator iter = incompleteMethods.iterator();
            while (iter.hasNext())
                try {
                    ((MethodResolver) iter.next()).resolve();
                    iter.remove();
                } catch (Exception localIncompleteElementException) {
                }
        }
    }

    public void parseStatement(Method method) {

    }
}
