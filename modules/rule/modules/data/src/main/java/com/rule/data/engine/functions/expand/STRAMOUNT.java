package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

public final class STRAMOUNT extends Function {

    public static final String NAME = STRAMOUNT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 1) {
            String source = DataUtil.getStringValue(args[0]);
            String target = DataUtil.getStringValue(args[1]);

            long count = NumberPool.LONG_0;
            int startIndex = 0;
            int len = target.length();

            while ((startIndex = source.indexOf(target, startIndex)) != -1) {
                startIndex += len;
                count++;
            }

            return count;
        }

        throw new ArgsCountException(NAME);
    }
}
