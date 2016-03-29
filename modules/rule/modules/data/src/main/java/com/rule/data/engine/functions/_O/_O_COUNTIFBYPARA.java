package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions.Function;

import java.util.Map;

/**
 * <hr> 分组计数 </hr>
 */
public class _O_COUNTIFBYPARA extends Function {
    public static final String NAME = _O_COUNTIFBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);
        final String criteria = DataUtil.getStringValue(args[2]);

        final Map<String, Object> currentParam = getParam(args, 3, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, currentParam, NAME);
        __Key key = new __Key(colCal, criteria);

        Long result = (Long) cache.get(key);
        if (result == null) {
            result = _O_COUNTIF.init(calInfo, serviceName, currentParam, colCal, criteria, NAME);
            cache.put(key, result);
        }

        return result;
    }


    class __Key {
        Object colCal, criteria;

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

        __Key(Object colCal, Object criteria) {
            this.colCal = colCal;
            this.criteria = criteria;
        }
    }

}
