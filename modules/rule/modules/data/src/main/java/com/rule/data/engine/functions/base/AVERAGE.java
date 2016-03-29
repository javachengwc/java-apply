package com.rule.data.engine.functions.base;

import com.rule.data.util.DataUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

public class AVERAGE extends Function {
    public static final String NAME = AVERAGE.class.getSimpleName();
    public static final Function SUM_F = getFunction("SUM");
    public static final Function COUNT_F = getFunction("COUNT");


    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        Object sumO = SUM_F.eval(calInfo, args);
        Object countO = COUNT_F.eval(calInfo, args);

        double count = DataUtil.getNumberValue(countO).doubleValue();
        double sum = DataUtil.getNumberValue(sumO).doubleValue();

        if (DataUtil.compare(count, NumberPool.DOUBLE_0) == 0) {
            return NumberPool.LONG_0;
        }

        return sum / count;
    }
}
