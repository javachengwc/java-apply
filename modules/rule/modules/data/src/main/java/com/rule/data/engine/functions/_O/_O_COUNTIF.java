package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgColumnNotFoundException;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.exception.ServiceNotFoundException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.engine.functions.CriteriaUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;

import java.util.Map;

/**
 * <hr> 分组计数 </hr>
 */
public class _O_COUNTIF extends Function {
    public static final String NAME = _O_COUNTIF.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);
        final String criteria = DataUtil.getStringValue(args[2]);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);

        __Key key = new __Key(colCal, criteria);
        Long result = (Long) cache.get(key);

        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam(), colCal, criteria, NAME);
            cache.put(key, result);
        }

        return result;
    }

    class __Key {
        Object colCal, criteria;

        __Key(Object colCal, Object criteria) {
            this.colCal = colCal;
            this.criteria = criteria;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (criteria != null ? !criteria.equals(key.criteria) : key.criteria != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCal != null ? colCal.hashCode() : 0;
            result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
            return result;
        }
    }

    static long init(CalInfo calInfo, String serviceName, Map<String, Object> currentParam
            , String colCal, String criteria, String funcName)
            throws RengineException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, currentParam, calInfo.getCallLayer()
                        , calInfo.getServicePo(), calInfo.getParam(), funcName);

        final Object[][] value = d2Data.getData();
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colCal);
        }

        long sum = NumberPool.LONG_0;

        CriteriaUtil.I_MatchPredicate m = CriteriaUtil.createCriteriaPredicate((criteria));
        for (int i = 0; i < value.length; i++) {
            final Object colCalValue = value[i][colCalInt];

            if (!m.matches(DataUtil.getValueEval(colCalValue))) {
                continue;
            }

            sum++;
        }

        return sum;
    }

}
