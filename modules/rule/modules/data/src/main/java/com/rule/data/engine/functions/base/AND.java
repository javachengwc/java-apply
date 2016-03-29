package com.rule.data.engine.functions.base;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.CalculateException;
import com.rule.data.exception.RengineException;

import com.rule.data.util.DataUtil;

import com.rule.data.engine.functions.Function;

import com.rule.data.model.vo.CalInfo;

import com.rule.data.engine.excel.NumberPool;

public final class AND extends Function {

    public static final String NAME = AND.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length == 0) {
            throw new ArgsCountException(NAME);
        }

        for (Object obj : args) {
            if (DataUtil.compare(DataUtil.getNumberValue(obj).doubleValue(), NumberPool.DOUBLE_0)
                    == 0) {
                return false;
            }
        }

        return true;
    }
}
