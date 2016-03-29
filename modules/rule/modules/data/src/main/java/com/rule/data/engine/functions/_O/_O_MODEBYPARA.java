package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.functions.Function;
import com.rule.data.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class _O_MODEBYPARA extends Function {
    public static final String NAME = _O_MODEBYPARA.class.getSimpleName();
    class _Key {
        List<String> columnNameList;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof _Key)) return false;

            _Key key = (_Key) o;

            if (columnNameList != null ? !columnNameList.equals(key.columnNameList) : key.columnNameList != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return columnNameList != null ? columnNameList.hashCode() : 0;
        }

        public _Key(List<String> columnNameList) {
            this.columnNameList = columnNameList;
        }
    }
    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 5) {
            throw new ArgsCountException(NAME);
        }
        final String serviceName = DataUtil.getServiceName(args[0]);
        boolean isByParam = false;
        int index = 0;
        for (int i = 1; i < args.length; i++) {
            if (args[i] instanceof Boolean) {
                isByParam = (Boolean) args[i];
                index = i;
                break;
            }
        }
        if(index<2){
            throw new IllegalArgumentException("参数输入错误，无法识别是否继承参数");
        }
        final Map currentParam = getParam(args, index+1, calInfo.getParam(), isByParam);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, currentParam, NAME);
        List<String> alist = new ArrayList<String>();

        for (int i = 1; i < index; i++) {
            alist.add(DataUtil.getStringValue(args[i]));
        }
        _Key Key = new _Key(alist);
        String result = (String) cache.get(Key);
        if (result == null) {
            result = _O_MODE.init(calInfo, serviceName, currentParam
                    , alist, NAME);
            cache.put(Key, result);
        }else{
            LogUtil.info("命中缓存");
        }

        return result;

    }
}
