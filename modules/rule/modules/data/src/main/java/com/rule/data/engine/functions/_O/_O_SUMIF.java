package com.rule.data.engine.functions._O;

import com.rule.data.exception.*;
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
 * <hr>求和</hr>
 */
public class _O_SUMIF extends Function {
    public static final String NAME = _O_SUMIF.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCriteria = DataUtil.getStringValue(args[1]);
        final String criteria = DataUtil.getStringValue(args[2]);
        String colSum = colCriteria;

        if (args.length > 3) {
            colSum = DataUtil.getStringValue(args[3]);
        }

        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        __Key key = new __Key(colCriteria, colSum, criteria);

        Double result = (Double) cache.get(key);
        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam(), colCriteria, colSum, criteria, NAME);
            cache.put(key, result);
        }

        return result;
    }

    class __Key {
        Object colCriteria, colSum, criteria;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCriteria != null ? !colCriteria.equals(key.colCriteria) : key.colCriteria != null)
                return false;
            if (colSum != null ? !colSum.equals(key.colSum) : key.colSum != null)
                return false;
            if (criteria != null ? !criteria.equals(key.criteria) : key.criteria != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCriteria != null ? colCriteria.hashCode() : 0;
            result = 31 * result + (colSum != null ? colSum.hashCode() : 0);
            result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
            return result;
        }

        __Key(Object colCriteria, Object colSum, Object criteria) {
            this.colCriteria = colCriteria;
            this.colSum = colSum;
            this.criteria = criteria;
        }
    }

    static Double init(CalInfo calInfo, String serviceName, Map<String, Object> currentParam
            , String colCriteria, String colSum, String criteria
            , String funcName)
            throws RengineException, CalculateException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, currentParam,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);

        final Object[][] value = d2Data.getData();
        int colCriteriaInt = DataUtil.getColumnIntIndex(colCriteria, d2Data.getColumnList());
        int colSumInt = DataUtil.getColumnIntIndex(colSum, d2Data.getColumnList());

        if (colCriteriaInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colCriteria);
        }
        if (colSumInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colSum);
        }

        CriteriaUtil.I_MatchPredicate m = CriteriaUtil.createCriteriaPredicate((criteria));

        double sum = NumberPool.DOUBLE_0;

        for (int i = 0; i < value.length; i++) {
            final Object colCriteriaValue = value[i][colCriteriaInt];
            final Object colSumValue = value[i][colSumInt];

            if (!m.matches(DataUtil.getValueEval(colCriteriaValue))) {
                continue;
            }

            if (canNumberOP(colSumValue)) {
                sum += DataUtil.getNumberValue(colSumValue).doubleValue();
            }
        }

        return sum;
    }
}