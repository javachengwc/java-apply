package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class REPLACE extends Function {

    public static final String NAME = REPLACE.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 3) {
            final String source = DataUtil.getStringValue(args[0]);
            int start = DataUtil.getNumberValue(args[1]).intValue() - 1;
            int num = DataUtil.getNumberValue(args[2]).intValue();
            final String target = DataUtil.getStringValue(args[3]);

            start = start < 0 ? 0 : start;
            num = num < 0 ? 0 : num;

            int end = start + num;

            if (start > source.length()) {
                start = end = source.length();
            }

            if (end > source.length()) {
                end = source.length();
            }

            return source.substring(0, start) + target + source.substring(end);
        }

        throw new ArgsCountException(NAME);
    }
}
