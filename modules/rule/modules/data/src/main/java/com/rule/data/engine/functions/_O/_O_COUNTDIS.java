package com.rule.data.engine.functions._O;

import com.rule.data.exception.*;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class _O_COUNTDIS extends Function {
    // 跨表可去重计数

    public static final String NAME = _O_COUNTDIS.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        boolean needFilter = false;
        if (args.length == 3) {
            if (args[2] instanceof Boolean) {
                needFilter = (Boolean) args[2];
            } else {
                throw new RengineException(serviceName, NAME + "无法匹配是否去重");
            }
        }

        __Key key = new __Key(colCal, needFilter);
        Long result = (Long) cache.get(key);
        if (result == null) {
            if (needFilter) {
                result = init2(calInfo, serviceName, calInfo.getParam(), colCal, NAME);
            } else {
                result = _O_COUNT.init(calInfo, serviceName, calInfo.getParam(), colCal, NAME);
            }


            cache.put(key, result);
        }

        return result;


    }

    static long init2(CalInfo calInfo, String serviceName, Map<String, Object> currentParam
            , String colCal, String funcName) throws RengineException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }
        D2Data d2Data = Cache4D2Data.getD2Data(servicePo, currentParam,
                calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);

        final int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colCal);
        }

        final Object[][] value = d2Data.getData();
        Set<Object> set = new HashSet<Object>();
        for (int i = 0; i < value.length; i++) {
            final Object colCalValue = value[i][colCalInt];
            if (colCalValue == null) {
                continue;
            }
            set.add(colCalValue);     // 去重
        }

        return set.size();
    }

    class __Key {
        Object colCal, needFilter;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (needFilter != null ? !needFilter.equals(key.needFilter) : key.needFilter != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCal != null ? colCal.hashCode() : 0;
            result = 31 * result + (needFilter != null ? needFilter.hashCode() : 0);
            return result;
        }

        public __Key(Object colCal, Object needFilter) {
            this.colCal = colCal;
            this.needFilter = needFilter;
        }
    }
}
