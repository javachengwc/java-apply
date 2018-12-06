package com.shop.base.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class TransUtil {

    public static <R,P> List<R> transList(List<P> list, Class<R> clazz ) {
        if(list==null) {
            return Collections.EMPTY_LIST;
        }
        List<R> rtList = new ArrayList<R>();
        for(P per:list) {
            R entity= transEntity(per,clazz);
            rtList.add(entity);
        }
        return rtList;
    }

    public  static <R,P>  R transEntity(P data,Class<R> clazz ) {
        try {
            R rt = clazz.newInstance();
            BeanUtils.copyProperties(data, rt);
            return rt;
        }catch(Exception e) {
            return null;
        }
    }

    public static <R,P> List<R> transList(List<P> list,ITrans<R,P> trans) {
        if(list==null) {
            return Collections.EMPTY_LIST;
        }
        List<R> rtList = new ArrayList<R>();
        for(P per:list) {
            R entity= transEntity(per,trans);
            rtList.add(entity);
        }
        return rtList;
    }

    public  static <R,P>  R transEntity(P data,ITrans<R,P> trans ) {
        try {
            R rt = trans.doTrans(data);
            return rt;
        }catch(Exception e) {
            return null;
        }
    }

    public interface ITrans<R,P> {

        public R  doTrans(P data);
    }
}
