package com.rule.data.engine.functions.type;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

import java.text.SimpleDateFormat;

public final class DATETIME extends Function {

    public static final String NAME = DATETIME.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        String source = DataUtil.getStringValue(args[0]);
        String format = DataUtil.getStringValue(args[1]);

        try {
            return new SimpleDateFormat(format).parse(source);
        } catch (Exception e) {
            return null;
        }
    }
}
