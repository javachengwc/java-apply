package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class SUBSTITUTE extends Function {

    public static final String NAME = SUBSTITUTE.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        String text = DataUtil.getStringValue(args[0]);
        String old = DataUtil.getStringValue(args[1]);
        String newT = DataUtil.getStringValue(args[2]);

        if (args.length > 3) {
            int index = DataUtil.getNumberValue(args[3]).intValue();

            if (index == 0) {
                index = 1;
            }


            int flag = text.indexOf(old);
            int count = 0;

            while (flag != -1) {
                count++;

                if (count == index) {
                    return text.substring(0, flag) + newT + text.substring(flag + old.length());
                }

                flag = text.indexOf(old, flag + 1);
            }

            return text;
        } else {
            return text.replace(old, newT);
        }
    }
}
