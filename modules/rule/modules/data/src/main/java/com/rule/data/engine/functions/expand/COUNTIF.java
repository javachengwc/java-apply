package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.CriteriaUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.Iterator;
import java.util.Map;

public class COUNTIF extends Function {

    public static final String NAME = COUNTIF.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange) {
            ExcelRange range = (ExcelRange) args[0];
            String criteria = DataUtil.getStringValue(args[1]);

            Map<Object, Object> cache = calInfo.getCache(NAME);
            __Key key = new __Key(criteria, range);

            Long areaValue = (Long) cache.get(key);
            if (areaValue == null) {
                areaValue = NumberPool.LONG_0;

                CriteriaUtil.I_MatchPredicate mp = CriteriaUtil.createCriteriaPredicate(criteria);

                if (mp != null) {
                    Iterator<Object> ite = range.getIterator();

                    while (ite.hasNext()) {
                        Object tmp = ite.next();

                        if (!mp.matches(DataUtil.getValueEval(tmp))) {
                            continue;
                        }
                        areaValue++;
                    }
                }
                cache.put(key, areaValue);
            }

            return areaValue;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }

    class __Key {
        Object range, criteria;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (criteria != null ? !criteria.equals(key.criteria) : key.criteria != null)
                return false;
            if (range != null ? !range.equals(key.range) : key.range != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = range != null ? range.hashCode() : 0;
            result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
            return result;
        }

        __Key(Object criteria, Object range) {
            this.criteria = criteria;
            this.range = range;
        }
    }
}
