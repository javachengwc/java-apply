package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class FIND extends Function {

    public static final String NAME = FIND.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        final String target = DataUtil.getStringValue(args[0]);
        final String source = DataUtil.getStringValue(args[1]);
        int start = 1;

        if (args.length > 2 && args[2] instanceof Number) {
            start = ((Number) args[2]).intValue();
        }

        if (start < 1) {
            start = 1;
        }

        int ret = (source.indexOf(target, start - 1) + 1);

        if (ret == 0) {
            throw new CalculateException("FIND未找到对应字符串");
        }

        return new Long(ret);
    }
}
