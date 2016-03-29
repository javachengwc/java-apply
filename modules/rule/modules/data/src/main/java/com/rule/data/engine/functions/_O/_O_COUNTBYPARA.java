package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions.Function;

import java.util.Map;

/**
 * <hr>分组求和</hr>
 */
public class _O_COUNTBYPARA extends Function {
    public static final String NAME = _O_COUNTBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);

        final Map<String, Object> param = getParam(args, 2, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, param, NAME);
        Long sum = (Long) cache.get(colCal);

        if (sum == null) {
            sum = _O_COUNT.init(calInfo, serviceName, param, colCal, NAME);
            cache.put(colCal, sum);
        }

        return sum;
    }


}