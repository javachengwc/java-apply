package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class EVEN extends Function {

    public static final String NAME = EVEN.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            double d = DataUtil.getNumberValue(args[0]).doubleValue();
            int cmp = DataUtil.compare(d, 0D);

            if (cmp == 0) {
                return 0;
            } else if (d < 0) { // 负的
                d = Math.abs(d);
                int iValue = (int) d;

                if (DataUtil.compare(iValue, d) == 0) { // 本身就是整数
                    if (iValue % 2 == 0) { // 偶数
                        //
                    } else {
                        iValue += 1;
                    }
                } else {
                    if (iValue % 2 == 0) {
                        iValue += 2;
                    } else {
                        iValue += 1;
                    }
                }

                return new Long(0 - iValue);
            } else {
                int iValue = (int) d;

                if (DataUtil.compare(iValue, d) == 0) { // 本身就是整数
                    if (iValue % 2 == 0) { // 偶数
                        //
                    } else {
                        iValue += 1;
                    }
                } else {
                    if (iValue % 2 == 0) {
                        iValue += 2;
                    } else {
                        iValue += 1;
                    }
                }

                return new Long(iValue);
            }
        }

        throw new ArgsCountException(NAME);
    }
}
