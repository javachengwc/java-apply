package com.rule.data.engine.functions.math;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

public final class PI extends Function {

    public static final String NAME = PI.class.getSimpleName();
    public static final double pi = Math.PI;

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        return pi;
    }
}
