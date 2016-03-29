package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.util.Type;

public final class T extends Function {

    public static final String NAME = T.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] != null) {
            if (DataUtil.getType(args[0]) == Type.STRING) {
                return args[0];
            }
        }

        return StringPool.EMPTY;
    }
}
