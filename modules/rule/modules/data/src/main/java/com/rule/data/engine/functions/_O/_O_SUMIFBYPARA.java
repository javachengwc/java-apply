package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.functions.Function;

import java.util.Map;

/**
 * <hr>求和</hr>
 */
public class _O_SUMIFBYPARA extends Function {
    public static final String NAME = _O_SUMIFBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 4) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCriteria = DataUtil.getStringValue(args[1]);
        final String criteria = DataUtil.getStringValue(args[2]);
        final String colSum = DataUtil.getStringValue(args[3]);

        final Map<String, Object> currentParam = getParam(args, 4, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, currentParam, NAME);
        __Key key = new __Key(colCriteria, colSum, criteria);

        Double result = (Double) cache.get(key);
        if (result == null) {
            result = _O_SUMIF.init(calInfo, serviceName, currentParam, colCriteria, colSum, criteria, NAME);
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

}