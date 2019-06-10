package com.pseudocode.cloud.zuul;


import com.pseudocode.cloud.zuul.filters.FilterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FilterLoader {
    static final FilterLoader INSTANCE = new FilterLoader();
    private static final Logger LOG = LoggerFactory.getLogger(FilterLoader.class);
    private final ConcurrentHashMap<String, Long> filterClassLastModified = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, String> filterClassCode = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, String> filterCheck = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, List<ZuulFilter>> hashFiltersByType = new ConcurrentHashMap();
    private FilterRegistry filterRegistry = FilterRegistry.instance();
    static DynamicCodeCompiler COMPILER;
    static FilterFactory FILTER_FACTORY = new DefaultFilterFactory();

    public FilterLoader() {
    }

    public void setCompiler(DynamicCodeCompiler compiler) {
        COMPILER = compiler;
    }

    public void setFilterRegistry(FilterRegistry r) {
        this.filterRegistry = r;
    }

    public void setFilterFactory(FilterFactory factory) {
        FILTER_FACTORY = factory;
    }

    public static FilterLoader getInstance() {
        return INSTANCE;
    }

    public ZuulFilter getFilter(String sCode, String sName) throws Exception {
        if (this.filterCheck.get(sName) == null) {
            this.filterCheck.putIfAbsent(sName, sName);
            if (!sCode.equals(this.filterClassCode.get(sName))) {
                LOG.info("reloading code " + sName);
                this.filterRegistry.remove(sName);
            }
        }

        ZuulFilter filter = this.filterRegistry.get(sName);
        if (filter == null) {
            Class clazz = COMPILER.compile(sCode, sName);
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filter = FILTER_FACTORY.newInstance(clazz);
            }
        }

        return filter;
    }

    public int filterInstanceMapSize() {
        return this.filterRegistry.size();
    }

    public boolean putFilter(File file) throws Exception {
        String sName = file.getAbsolutePath() + file.getName();
        if (this.filterClassLastModified.get(sName) != null && file.lastModified() != ((Long)this.filterClassLastModified.get(sName)).longValue()) {
            LOG.debug("reloading filter " + sName);
            this.filterRegistry.remove(sName);
        }

        ZuulFilter filter = this.filterRegistry.get(sName);
        if (filter == null) {
            Class clazz = COMPILER.compile(file);
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filter = FILTER_FACTORY.newInstance(clazz);
                List<ZuulFilter> list = (List)this.hashFiltersByType.get(filter.filterType());
                if (list != null) {
                    this.hashFiltersByType.remove(filter.filterType());
                }

                this.filterRegistry.put(file.getAbsolutePath() + file.getName(), filter);
                this.filterClassLastModified.put(sName, file.lastModified());
                return true;
            }
        }

        return false;
    }

    public List<ZuulFilter> getFiltersByType(String filterType) {
        List<ZuulFilter> list = (List)this.hashFiltersByType.get(filterType);
        if (list != null) {
            return list;
        } else {
           list = new ArrayList<ZuulFilter> ();
            Collection<ZuulFilter> filters = this.filterRegistry.getAllFilters();
            Iterator iterator = filters.iterator();

            while(iterator.hasNext()) {
                ZuulFilter filter = (ZuulFilter)iterator.next();
                if (filter.filterType().equals(filterType)) {
                    list.add(filter);
                }
            }

            Collections.sort(list);
            this.hashFiltersByType.putIfAbsent(filterType, list);
            return list;
        }
    }
}
