package com.rule.data.engine.functions._O;

import com.rule.data.exception.*;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions.CriteriaUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <hr> 分组计数 </hr>
 */
public class _O_SUMIFS extends Function {
    public static final String NAME = _O_SUMIFS.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final List<String> colCals = new ArrayList<String>();
        final List<String> criterias = new ArrayList<String>();
        String colSum = null;

        for (int i = 1; i < args.length; i += 2) {
            if (i + 1 == args.length) {
                colSum = DataUtil.getStringValue(args[i]);
                break;
            }

            colCals.add(DataUtil.getStringValue(args[i]));
            criterias.add(DataUtil.getStringValue(args[i + 1]));
        }

        if (colSum == null) {
            colSum = colCals.get(0);
        }

        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        __Key key = new __Key(colCals, colSum, criterias);

        Double result = (Double) cache.get(key);
        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam(), colCals, criterias, colSum, NAME);
            cache.put(key, result);
        }

        return result;
    }


    class __Key {
        Object colCals, colSum, criterias;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCals != null ? !colCals.equals(key.colCals) : key.colCals != null)
                return false;
            if (colSum != null ? !colSum.equals(key.colSum) : key.colSum != null)
                return false;
            if (criterias != null ? !criterias.equals(key.criterias) : key.criterias != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCals != null ? colCals.hashCode() : 0;
            result = 31 * result + (colSum != null ? colSum.hashCode() : 0);
            result = 31 * result + (criterias != null ? criterias.hashCode() : 0);
            return result;
        }

        __Key(Object colCals, Object colSum, Object criterias) {
            this.colCals = colCals;
            this.colSum = colSum;
            this.criterias = criterias;
        }
    }


    static double init(CalInfo calInfo, String serviceName, Map<String, Object> currentParam
            , List<String> colCals, List<String> criterias, String colSum
            , String funcName) throws RengineException, CalculateException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, currentParam, calInfo.getCallLayer()
                        , calInfo.getServicePo(), calInfo.getParam(), funcName);

        final Object[][] value = d2Data.getData();
        final List<Integer> colCalInts = new ArrayList<Integer>();
        for (String str : colCals) {
            final int tmp = DataUtil.getColumnIntIndex(str, d2Data.getColumnList());
            if (tmp == -1) {
                throw new ArgColumnNotFoundException(NAME, str);
            }
            colCalInts.add(tmp);
        }

        final int colSumInt = DataUtil.getColumnIntIndex(colSum, d2Data.getColumnList());
        if (colSumInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colSum);
        }

        final List<CriteriaUtil.I_MatchPredicate> ms = new ArrayList<CriteriaUtil.I_MatchPredicate>();
        for (String criteria : criterias) {
            final CriteriaUtil.I_MatchPredicate m = CriteriaUtil.createCriteriaPredicate((criteria));
            ms.add(m);
        }

        double sum = 0;
        for (int i = 0; i < value.length; i++) {
            boolean isOk = true;

            for (int j = 0; j < ms.size(); j++) {
                final CriteriaUtil.I_MatchPredicate m = ms.get(j);
                final Object colCalValue = value[i][colCalInts.get(j)];

                if (!m.matches(DataUtil.getValueEval(colCalValue))) {
                    isOk = false;
                    break;
                }
            }

            if (isOk) {
                final Object colSumValue = value[i][colSumInt];

                if (colSumValue != null && canNumberOP(colSumValue)) {
                    sum += DataUtil.getNumberValue(colSumValue).doubleValue();
                }
            }
        }

        return sum;
    }

}
