package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

public final class CEILING extends Function {

    public static final String NAME = CEILING.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        double number = DataUtil.getNumberValue(args[0]).doubleValue();
        double significance = DataUtil.getNumberValue(args[1]).doubleValue();
        if (DataUtil.compare(significance, NumberPool.DOUBLE_0) == NumberPool.LONG_0) {
            return NumberPool.LONG_0;
        }

        return significance * (Math.ceil(number / significance));
    }
}
