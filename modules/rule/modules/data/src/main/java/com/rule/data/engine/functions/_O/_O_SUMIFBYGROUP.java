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

import java.util.HashMap;
import java.util.Map;

/**
 * <hr>分组求和</hr>
 * 分组依据列的值     e.g. 1<hr/>
 * 分组依据的列             e.g. H<hr/>
 * SUM的列                       e.g. I<hr/>
 * 依据的数据源                          e.g. 2<hr/>
 */
public class _O_SUMIFBYGROUP extends Function {
    public static final String NAME = _O_SUMIFBYGROUP.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 5) {
            throw new ArgsCountException(NAME);
        }

        final String reqKey = DataUtil.getStringValue(args[0]);
        final String serviceName = DataUtil.getServiceName(args[1]);
        final String colBy = DataUtil.getStringValue(args[2]);
        final String colCriteria = DataUtil.getStringValue(args[3]);
        final String criteria = DataUtil.getStringValue(args[4]);
        String colSum = colCriteria;

        if (args.length > 5) {
            colSum = DataUtil.getStringValue(args[5]);
        }


        final Map<String, Object> param = getParam(args, 6, calInfo.getParam(), false);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, param, NAME);
        __Key key = new __Key(colBy, colCriteria, colSum, criteria);
        Map<String, Double> result = (Map<String, Double>) cache.get(key);

        if (result == null) {
            result = init(calInfo, serviceName, param, colBy, colCriteria, criteria, colSum);
            cache.put(key, result);
        }

        Double ret = result.get(reqKey);
        if (ret == null) {
            return NumberPool.DOUBLE_0;
        }

        return ret;
    }

    class __Key {
        Object colBy, colCriteria, colSum, criteria;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colBy != null ? !colBy.equals(key.colBy) : key.colBy != null)
                return false;
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
            int result = colBy != null ? colBy.hashCode() : 0;
            result = 31 * result + (colCriteria != null ? colCriteria.hashCode() : 0);
            result = 31 * result + (colSum != null ? colSum.hashCode() : 0);
            result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
            return result;
        }

        __Key(Object colBy, Object colCriteria, Object colSum, Object criteria) {
            this.colBy = colBy;
            this.colCriteria = colCriteria;
            this.colSum = colSum;
            this.criteria = criteria;
        }
    }


    private Map<String, Double> init(CalInfo calInfo, String serviceName,
                                     Map<String, Object> param, String colBy, String colCriteria, String criteria, String colSum)
            throws RengineException, CalculateException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, param, calInfo.getCallLayer()
                        , calInfo.getServicePo(), calInfo.getParam(), NAME);

        Map<String, Double> result = new HashMap<String, Double>();
        final Object[][] value = d2Data.getData();
        int colByInt = DataUtil.getColumnIntIndex(colBy, d2Data.getColumnList());
        int colCriteriaInt = DataUtil.getColumnIntIndex(colCriteria, d2Data.getColumnList());
        int colSumInt = DataUtil.getColumnIntIndex(colSum, d2Data.getColumnList());

        if (colByInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colBy);
        }
        if (colCriteriaInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colCriteria);
        }
        if (colSumInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colSum);
        }

        CriteriaUtil.I_MatchPredicate m = CriteriaUtil.createCriteriaPredicate((criteria));

        for (int i = 0; i < value.length; i++) {
            final Object colByValue = value[i][colByInt];
            final Object colCriteriaValue = value[i][colCriteriaInt];
            final Object colSumValue = value[i][colSumInt];

            if (colByValue == null
                    || !m.matches(DataUtil.getValueEval(colCriteriaValue))) {
                continue;
            }

            if (canNumberOP(colSumValue)) {
                String key = DataUtil.getStringValue(colByValue);
                double __value = DataUtil.getNumberValue(colSumValue).doubleValue();
                Double sum = result.get(key);

                if (sum == null) {
                    result.put(key, __value);
                } else {
                    result.put(key, sum + __value);
                }
            }
        }

        return result;
    }

}