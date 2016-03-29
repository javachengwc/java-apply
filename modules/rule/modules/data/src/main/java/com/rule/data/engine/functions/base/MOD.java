package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

public final class MOD extends Function {

    public static final String NAME = MOD.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        Number number = DataUtil.getNumberValue(args[0]);
        Number divisor = DataUtil.getNumberValue(args[1]);

        if (DataUtil.compare(divisor.doubleValue(), NumberPool.DOUBLE_0) == 0) {
            throw new CalculateException("除0错误");
        }

        return number.doubleValue() % divisor.doubleValue();
    }
}
