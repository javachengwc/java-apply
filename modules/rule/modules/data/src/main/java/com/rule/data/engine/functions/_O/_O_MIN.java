package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgColumnNotFoundException;
import com.rule.data.exception.RengineException;
import com.rule.data.exception.ServiceNotFoundException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;
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
public class _O_MIN extends Function {
    public static final String NAME = _O_MIN.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new IllegalArgumentException("参数个数不匹配");
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);

        Double result = (Double) cache.get(colCal);
        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam(), colCal, NAME);
            cache.put(colCal, result);
        }

        return result;
    }

    static Double init(CalInfo calInfo, String serviceName, Map<String, Object> currentparam
            , String colCal, String funcName) throws RengineException, CalculateException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, currentparam,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);

        final Object[][] value = d2Data.getData();
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colCal);
        }

        double minimum = Double.MAX_VALUE;
        boolean hasValue = false;

        for (int i = 0; i < value.length; i++) {
            final Object colCalValue = value[i][colCalInt];
            if (colCalValue == null) {
                continue;
            }

            if (canNumberOP(colCalValue)) {
                double dValue = DataUtil.getNumberValue(colCalValue).doubleValue();
                hasValue = true;
                minimum = dValue < minimum ? dValue : minimum;
            }
        }

        if (!hasValue) {
            return NumberPool.DOUBLE_0;
        }

        return minimum;
    }
}