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
 * <hr>分组求和</hr>
 */
public class _O_MINBYPARA extends Function {
    public static final String NAME = _O_MINBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);

        final Map currentParam = getParam(args, 2, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, currentParam, NAME);
        Double sum = (Double) cache.get(colCal);

        if (sum == null) {
            sum = _O_MIN.init(calInfo, serviceName, currentParam, colCal, NAME);
            cache.put(colCal, sum);
        }

        return sum;
    }


}