package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.functions.Function;

import java.util.Map;

public class _O_COUNTDISBYPARA extends Function {
    // 跨表可带参数去重计数
    public static final String NAME = _O_COUNTDISBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        // args: 数据源名称-计数列名称-是否去重-[参数值-参数名]
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }
        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);
        boolean needFilter = false;
        if (args[2] instanceof Boolean) {
            needFilter = (Boolean) args[2];
        } else {
            throw new RengineException(serviceName, NAME + "无法匹配是否去重");
        }
        final Map<String, Object> param = getParam(args, 3, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, param, NAME);
        __Key key = new __Key(colCal, needFilter);
        Long sum = (Long) cache.get(key);

        if (sum == null) {
            if (needFilter) {
                sum = _O_COUNTDIS.init2(calInfo, serviceName, param, colCal, NAME);
            } else {
                sum = _O_COUNT.init(calInfo, serviceName, param, colCal, NAME);
            }
            cache.put(key, sum);
        }
        return sum;
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
