package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

public final class SIGN extends Function {

    public static final String NAME = SIGN.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            final double d = DataUtil.getNumberValue(args[0]).doubleValue();
            return new Long(DataUtil.compare(d, NumberPool.DOUBLE_0));
        }

        throw new ArgsCountException(NAME);
    }
}
