package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions.Function;

import java.util.Map;

/**
 * <hr>求和</hr>
 */
public class _O_CONCATBYPARA extends Function {
    public static final String NAME = _O_CONCATBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 4) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);
        boolean needFilter = false;

        if (args[2] instanceof Boolean) {
            needFilter = (Boolean) args[2];
        }

        String flag = DataUtil.getStringValue(args[3]);
        if (flag.length() == 0) {
            flag = ",";
        }

        final Map<String, Object> param = getParam(args, 4, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, param, NAME);
        __Key key = new __Key(colCal, needFilter, flag);
        String result = (String) cache.get(key);

        if (result == null) {
            result = _O_CONCAT.init(calInfo, serviceName, param, colCal, needFilter, flag, NAME);
            cache.put(key, result);
        }

        return result;
    }

    class __Key {
        Object colCal, needFilter, flag;

        __Key(Object colCal, Object needFilter, Object flag) {
            this.colCal = colCal;
            this.flag = flag;
            this.needFilter = needFilter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (flag != null ? !flag.equals(key.flag) : key.flag != null)
                return false;
            if (needFilter != null ? !needFilter.equals(key.needFilter) : key.needFilter != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCal != null ? colCal.hashCode() : 0;
            result = 31 * result + (needFilter != null ? needFilter.hashCode() : 0);
            result = 31 * result + (flag != null ? flag.hashCode() : 0);
            return result;
        }
    }
}